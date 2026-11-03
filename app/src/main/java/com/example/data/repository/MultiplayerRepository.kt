package com.example.data.repository

import android.content.Context
import com.example.R
import com.example.data.model.MatchRoom
import com.example.data.model.UserProfile
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await

class MultiplayerRepository(
    private val db: FirebaseFirestore
) {
    constructor(context: Context) : this(
        FirebaseFirestore.getInstance(
            context.applicationContext.getString(R.string.firestore_database_id)
        )
    )

    fun observeUserProfile(userId: String): Flow<UserProfile?> {
        return db.collection("users").document(userId)
            .snapshots()
            .map { it.toObject(UserProfile::class.java) }
    }

    suspend fun saveUserProfile(profile: UserProfile) {
        val docRef = db.collection("users").document(profile.userId)
        val data = hashMapOf<String, Any>(
            "userId" to profile.userId,
            "displayName" to profile.displayName,
            "favoriteTeamId" to profile.favoriteTeamId,
            "wins" to profile.wins,
            "losses" to profile.losses,
            "createdAt" to (profile.createdAt ?: FieldValue.serverTimestamp()),
            "updatedAt" to FieldValue.serverTimestamp()
        )
        if (!profile.email.isNullOrBlank()) {
            data["email"] = profile.email
        }
        docRef.set(data).await()
    }

    suspend fun recordMatchResult(userId: String, won: Boolean) {
        val docRef = db.collection("users").document(userId)
        val snapshot = docRef.get().await()
        if (snapshot.exists()) {
            val incrementField = if (won) "wins" else "losses"
            docRef.update(
                incrementField, FieldValue.increment(1),
                "updatedAt", FieldValue.serverTimestamp()
            ).await()
        }
    }

    fun observeMatchRoom(roomId: String): Flow<MatchRoom?> {
        return db.collection("match_rooms").document(roomId)
            .snapshots()
            .map { it.toObject(MatchRoom::class.java) }
    }

    suspend fun createMatchRoom(room: MatchRoom): String {
        val docRef = if (room.roomId.isNotBlank()) {
            db.collection("match_rooms").document(room.roomId)
        } else {
            db.collection("match_rooms").document()
        }
        val generatedId = docRef.id

        val payload = hashMapOf<String, Any>(
            "roomId" to generatedId,
            "roomCode" to room.roomCode,
            "hostUserId" to room.hostUserId,
            "hostDisplayName" to room.hostDisplayName,
            "hostTeamId" to room.hostTeamId,
            "status" to room.status,
            "currentTurn" to room.currentTurn,
            "hostScore" to room.hostScore,
            "guestScore" to room.guestScore,
            "round" to room.round,
            "hostKicks" to room.hostKicks,
            "guestKicks" to room.guestKicks,
            "createdAt" to FieldValue.serverTimestamp(),
            "updatedAt" to FieldValue.serverTimestamp()
        )
        if (room.guestUserId != null) payload["guestUserId"] = room.guestUserId
        if (room.guestDisplayName != null) payload["guestDisplayName"] = room.guestDisplayName
        if (room.guestTeamId != null) payload["guestTeamId"] = room.guestTeamId
        if (room.lastAction != null) payload["lastAction"] = room.lastAction

        docRef.set(payload).await()
        return generatedId
    }

    suspend fun findRoomByCode(code: String): MatchRoom? {
        val querySnapshot = db.collection("match_rooms")
            .whereEqualTo("roomCode", code.trim().uppercase())
            .whereEqualTo("status", "WAITING_FOR_PLAYER")
            .limit(1)
            .get()
            .await()

        return querySnapshot.documents.firstOrNull()?.toObject(MatchRoom::class.java)
    }

    suspend fun joinMatchRoom(roomId: String, guestUserId: String, guestDisplayName: String, guestTeamId: String) {
        val docRef = db.collection("match_rooms").document(roomId)
        docRef.update(
            mapOf(
                "guestUserId" to guestUserId,
                "guestDisplayName" to guestDisplayName,
                "guestTeamId" to guestTeamId,
                "status" to "IN_PROGRESS",
                "lastAction" to "$guestDisplayName joined the clash!",
                "updatedAt" to FieldValue.serverTimestamp()
            )
        ).await()
    }

    suspend fun recordPenaltyKick(
        roomId: String,
        isHostKick: Boolean,
        scored: Boolean,
        commentary: String,
        isGameOver: Boolean = false,
        winnerUserId: String? = null
    ) {
        val docRef = db.collection("match_rooms").document(roomId)
        val roomSnapshot = docRef.get().await()
        val currentRoom = roomSnapshot.toObject(MatchRoom::class.java) ?: return

        val newHostKicks = currentRoom.hostKicks.toMutableList()
        val newGuestKicks = currentRoom.guestKicks.toMutableList()
        var newHostScore = currentRoom.hostScore
        var newGuestScore = currentRoom.guestScore
        var newRound = currentRoom.round

        val nextTurn: String
        if (isHostKick) {
            newHostKicks.add(scored)
            if (scored) newHostScore++
            nextTurn = "GUEST_KICKING"
        } else {
            newGuestKicks.add(scored)
            if (scored) newGuestScore++
            nextTurn = "HOST_KICKING"
            newRound++
        }

        val updates = mutableMapOf<String, Any>(
            "hostScore" to newHostScore,
            "guestScore" to newGuestScore,
            "round" to newRound,
            "hostKicks" to newHostKicks,
            "guestKicks" to newGuestKicks,
            "currentTurn" to nextTurn,
            "lastAction" to commentary,
            "updatedAt" to FieldValue.serverTimestamp()
        )

        if (isGameOver) {
            updates["status"] = "COMPLETED"
            if (winnerUserId != null) {
                updates["winnerUserId"] = winnerUserId
            }
        }

        docRef.update(updates).await()
    }

    suspend fun closeRoom(roomId: String) {
        try {
            db.collection("match_rooms").document(roomId).delete().await()
        } catch (_: Exception) {}
    }
}
