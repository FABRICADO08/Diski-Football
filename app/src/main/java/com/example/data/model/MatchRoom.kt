package com.example.data.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.ServerTimestamp

@IgnoreExtraProperties
data class MatchRoom(
    val roomId: String = "",
    val roomCode: String = "",
    val hostUserId: String = "",
    val hostDisplayName: String = "",
    val hostTeamId: String = "kaizer_chiefs",
    val guestUserId: String? = null,
    val guestDisplayName: String? = null,
    val guestTeamId: String? = null,
    val status: String = "WAITING_FOR_PLAYER", // WAITING_FOR_PLAYER, IN_PROGRESS, COMPLETED
    val currentTurn: String = "HOST_KICKING",  // HOST_KICKING, GUEST_KICKING
    val hostScore: Long = 0L,
    val guestScore: Long = 0L,
    val round: Long = 1L,
    val hostKicks: List<Boolean> = emptyList(),
    val guestKicks: List<Boolean> = emptyList(),
    val lastAction: String? = null,
    val winnerUserId: String? = null,
    @ServerTimestamp val createdAt: Timestamp? = null,
    @ServerTimestamp val updatedAt: Timestamp? = null
)
