package com.example.data.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "standings",
    indices = [
        Index(value = ["tournamentType", "season", "teamId"], unique = true)
    ]
)
data class StandingEntity(
    @PrimaryKey
    val id: String,                    // "${tournamentType}_${season}_${teamId}"
    val teamId: String,
    val tournamentType: TournamentType,
    val season: Int = 2026,
    val played: Int = 0,
    val won: Int = 0,
    val drawn: Int = 0,
    val lost: Int = 0,
    val goalsFor: Int = 0,
    val goalsAgainst: Int = 0,
    val goalDifference: Int = 0,
    val points: Int = 0,
    val position: Int = 1
)
