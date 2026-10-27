package com.example.data.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "matches",
    indices = [
        Index(value = ["homeTeamId"]),
        Index(value = ["awayTeamId"]),
        Index(value = ["tournamentType", "seasonYear"])
    ]
)
data class MatchEntity(
    @PrimaryKey
    val id: String,                    // e.g. "match_psl_2026_gw1_sundowns_pirates"
    val homeTeamId: String,
    val awayTeamId: String,
    val homeGoals: Int? = null,
    val awayGoals: Int? = null,
    val homePenalties: Int? = null,
    val awayPenalties: Int? = null,
    val tournamentType: TournamentType,
    val status: MatchStatus = MatchStatus.SCHEDULED,
    val gameWeek: Int,                 // 1 to 30 for leagues, 1..4 for cups
    val stage: String,                 // "GameWeek 1", "Round of 16", "Quarter-Final", "Semi-Final", "Final"
    val seasonYear: Int = 2026,
    val matchSummary: String = ""      // Goalscorers and events summary
)
