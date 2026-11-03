package com.example

import com.example.base.FirestoreEmulatorTestBase
import com.example.data.model.MatchRoom
import com.example.data.model.UserProfile
import com.example.data.repository.MultiplayerRepository
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Test

class MultiplayerRepositoryRuleTest : FirestoreEmulatorTestBase() {

    @Test
    fun testAuthenticatedUserCanCreateAndJoinMatchRoom() = runBlocking {
        val hostUid = signInTestUser("host_player@example.com")
        val repository = MultiplayerRepository(firestore)

        val room = MatchRoom(
            roomId = "test_room_1",
            roomCode = "DK-101",
            hostUserId = hostUid,
            hostDisplayName = "Host Star",
            hostTeamId = "kaizer_chiefs",
            status = "WAITING_FOR_PLAYER",
            currentTurn = "HOST_KICKING",
            hostScore = 0L,
            guestScore = 0L,
            round = 1L,
            hostKicks = emptyList(),
            guestKicks = emptyList()
        )

        val createdId = repository.createMatchRoom(room)
        assertEquals("test_room_1", createdId)

        // Guest player signs in
        val guestUid = signInTestUser("guest_player@example.com")
        repository.joinMatchRoom("test_room_1", guestUid, "Guest Star", "orlando_pirates")

        // Record a kick
        repository.recordPenaltyKick(
            roomId = "test_room_1",
            isHostKick = true,
            scored = true,
            commentary = "LADUMA! Host scores!"
        )

        val doc = firestore.collection("match_rooms").document("test_room_1").get().await()
        val updatedRoom = doc.toObject(MatchRoom::class.java)
        assertNotNull(updatedRoom)
        assertEquals(1L, updatedRoom?.hostScore)
        assertEquals("IN_PROGRESS", updatedRoom?.status)
    }

    @Test
    fun testUnauthenticatedUserCannotCreateMatchRoom() = runBlocking {
        auth.signOut()
        val repository = MultiplayerRepository(firestore)

        val room = MatchRoom(
            roomId = "unauth_room",
            roomCode = "DK-999",
            hostUserId = "some_fake_uid",
            hostDisplayName = "Nobody",
            hostTeamId = "kaizer_chiefs"
        )

        try {
            repository.createMatchRoom(room)
            fail("Unauthenticated room creation must fail with PERMISSION_DENIED")
        } catch (e: Exception) {
            val firestoreException = e.unwrapFirestoreException()
            assertTrue(
                "Exception should be PERMISSION_DENIED: ${e.message}",
                firestoreException?.code == FirebaseFirestoreException.Code.PERMISSION_DENIED ||
                        e.message?.contains("PERMISSION_DENIED") == true ||
                        e is FirebaseFirestoreException
            )
        }
    }

    @Test
    fun testUserProfileSaveAndMatchResultIncrement() = runBlocking {
        val uid = signInTestUser("player_stats@example.com")
        val repository = MultiplayerRepository(firestore)

        val profile = UserProfile(
            userId = uid,
            displayName = "Bafana Bafana Striker",
            favoriteTeamId = "kaizer_chiefs",
            wins = 0L,
            losses = 0L
        )
        repository.saveUserProfile(profile)

        // Record a win
        repository.recordMatchResult(uid, won = true)

        val doc = firestore.collection("users").document(uid).get().await()
        val updated = doc.toObject(UserProfile::class.java)
        assertNotNull(updated)
        assertEquals(1L, updated?.wins)
        assertEquals(0L, updated?.losses)
    }
}
