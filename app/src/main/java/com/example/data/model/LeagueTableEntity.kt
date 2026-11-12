package com.example.data.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Room database entity storing official PSL (Betway Premiership) and NFD (Motsepe Foundation Championship)
 * league table standings, including team names, points, goal difference, and match history.
 */
@Entity(
    tableName = "league_tables",
    indices = [
        Index(value = ["league", "seasonYear", "teamId"], unique = true),
        Index(value = ["league", "seasonYear", "points", "goalDifference", "goalsFor"])
    ]
)
data class LeagueTableEntity(
    @PrimaryKey
    val id: String,                    // e.g. "PSL_2026_mamelodi_sundowns" or "NFD_2026_durban_city"
    val league: LeagueType,            // PSL or NFD
    val tournamentType: TournamentType, // TournamentType.PSL or TournamentType.NFD
    val teamId: String,                // FK to teams.id
    val teamName: String,              // e.g. "Mamelodi Sundowns"
    val teamShortName: String,         // e.g. "Sundowns"
    val seasonYear: Int = 2026,
    val played: Int = 0,
    val won: Int = 0,
    val drawn: Int = 0,
    val lost: Int = 0,
    val goalsFor: Int = 0,
    val goalsAgainst: Int = 0,
    val goalDifference: Int = 0,
    val points: Int = 0,
    val position: Int = 1,
    val matchHistory: String = ""      // Comma-separated recent match results, e.g. "W,D,W,L,W" (last 5 matches)
) {
    val formList: List<String>
        get() = if (matchHistory.isBlank()) emptyList() else matchHistory.split(",").map { it.trim() }.filter { it.isNotEmpty() }
}
