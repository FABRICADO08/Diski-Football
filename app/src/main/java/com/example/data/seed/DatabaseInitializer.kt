package com.example.data.seed

import android.util.Log
import com.example.data.database.DiskiDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DatabaseInitializer(private val database: DiskiDatabase) {

    private val tag = "DatabaseInitializer"

    suspend fun populateIfNeeded() = withContext(Dispatchers.IO) {
        val teamCount = database.teamDao().getTeamCount()
        Log.d(tag, "Current team count in database: $teamCount")

        if (teamCount == 0) {
            Log.d(tag, "Seeding South African football database...")
            seedAllData()
            Log.d(tag, "Database seeding complete!")
        } else if (database.leagueTableDao().getTotalTableCount() == 0) {
            val teams = SouthAfricanTeamsData.allTeams
            val leagueTables = SouthAfricanTournamentsData.generateInitialLeagueTables(teams)
            database.leagueTableDao().insertAll(leagueTables)
        }
    }

    suspend fun resetAndRebuild() = withContext(Dispatchers.IO) {
        Log.d(tag, "Resetting and rebuilding database...")
        seedAllData()
    }

    private suspend fun seedAllData() {
        // 1. Teams
        val teams = SouthAfricanTeamsData.allTeams
        database.teamDao().insertAll(teams)

        // 2. Players
        val players = SouthAfricanPlayersData.generateAllPlayers(teams)
        database.playerDao().insertAll(players)

        // 3. Tournaments
        val tournaments = SouthAfricanTournamentsData.generateInitialTournaments()
        database.tournamentDao().insertAll(tournaments)

        // 4. Standings
        val standings = SouthAfricanTournamentsData.generateInitialStandings(teams)
        database.standingDao().insertAll(standings)

        // 4b. PSL and NFD League Tables
        val leagueTables = SouthAfricanTournamentsData.generateInitialLeagueTables(teams)
        database.leagueTableDao().insertAll(leagueTables)

        // 5. Initial Fixtures
        val fixtures = SouthAfricanTournamentsData.generateInitialFixtures(teams)
        database.matchDao().insertAll(fixtures)
    }
}
