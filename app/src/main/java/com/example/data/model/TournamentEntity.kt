package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tournaments")
data class TournamentEntity(
    @PrimaryKey
    val id: String,                    // e.g. "psl_2026", "mtn8_2026", "carling_2026", "nedbank_2026"
    val type: TournamentType,
    val name: String,
    val season: Int = 2026,
    val isCompleted: Boolean = false,
    val currentRound: String = "Round of 16",
    val winnerTeamId: String? = null,
    val bracketData: String = ""       // JSON representation of tournament tree
)
