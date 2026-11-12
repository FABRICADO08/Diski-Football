package com.example

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.data.database.DiskiDatabase
import com.example.data.model.LeagueTableEntity
import com.example.data.model.LeagueType
import com.example.data.model.StandingEntity
import com.example.data.model.TournamentType
import com.example.data.seed.SouthAfricanTeamsData
import com.example.data.seed.SouthAfricanTournamentsData
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class LeagueTableSchemaTest {

    private lateinit var db: DiskiDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, DiskiDatabase::class.java)
            .allowMainThreadQueries()
            .build()
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun testPslAndNfdLeagueTableSchemaStorage() = runBlocking {
        val teams = SouthAfricanTeamsData.allTeams
        val leagueTables = SouthAfricanTournamentsData.generateInitialLeagueTables(teams)

        // Verify counts: 16 PSL teams and 16 NFD teams = 32 entries
        assertEquals(32, leagueTables.size)

        db.leagueTableDao().insertAll(leagueTables)

        val pslCount = db.leagueTableDao().getTeamCount(LeagueType.PSL, 2026)
        val nfdCount = db.leagueTableDao().getTeamCount(LeagueType.NFD, 2026)
        assertEquals(16, pslCount)
        assertEquals(16, nfdCount)

        val totalCount = db.leagueTableDao().getTotalTableCount()
        assertEquals(32, totalCount)
    }

    @Test
    fun testLeagueTableEntityFieldsIncludingTeamNamesPointsAndGoalDifference() = runBlocking {
        val entry = LeagueTableEntity(
            id = "PSL_2026_mamelodi_sundowns",
            league = LeagueType.PSL,
            tournamentType = TournamentType.PSL,
            teamId = "mamelodi_sundowns",
            teamName = "Mamelodi Sundowns",
            teamShortName = "Sundowns",
            seasonYear = 2026,
            played = 5,
            won = 4,
            drawn = 1,
            lost = 0,
            goalsFor = 11,
            goalsAgainst = 2,
            goalDifference = 9,
            points = 13,
            position = 1,
            matchHistory = "W,W,D,W,W"
        )

        db.leagueTableDao().insertOrUpdate(entry)

        val fetched = db.leagueTableDao().getEntryForTeam(LeagueType.PSL, "mamelodi_sundowns", 2026)
        assertNotNull("Entry must be retrieved from database", fetched)
        assertEquals("Mamelodi Sundowns", fetched?.teamName)
        assertEquals("Sundowns", fetched?.teamShortName)
        assertEquals(13, fetched?.points)
        assertEquals(9, fetched?.goalDifference)
        assertEquals(5, fetched?.played)
        assertEquals(4, fetched?.won)
        assertEquals(1, fetched?.drawn)
        assertEquals(0, fetched?.lost)
        assertEquals(11, fetched?.goalsFor)
        assertEquals(2, fetched?.goalsAgainst)
        assertEquals("W,W,D,W,W", fetched?.matchHistory)

        // Verify formList helper
        val form = fetched?.formList ?: emptyList()
        assertEquals(5, form.size)
        assertEquals(listOf("W", "W", "D", "W", "W"), form)
    }

    @Test
    fun testPslTableSortingByPointsAndGoalDifference() = runBlocking {
        val sundowns = LeagueTableEntity(
            id = "PSL_2026_sundowns",
            league = LeagueType.PSL,
            tournamentType = TournamentType.PSL,
            teamId = "mamelodi_sundowns",
            teamName = "Mamelodi Sundowns",
            teamShortName = "Sundowns",
            played = 3,
            won = 3,
            drawn = 0,
            lost = 0,
            goalsFor = 8,
            goalsAgainst = 1,
            goalDifference = 7,
            points = 9,
            matchHistory = "W,W,W"
        )

        val pirates = LeagueTableEntity(
            id = "PSL_2026_pirates",
            league = LeagueType.PSL,
            tournamentType = TournamentType.PSL,
            teamId = "orlando_pirates",
            teamName = "Orlando Pirates",
            teamShortName = "Pirates",
            played = 3,
            won = 3,
            drawn = 0,
            lost = 0,
            goalsFor = 6,
            goalsAgainst = 1,
            goalDifference = 5,
            points = 9,
            matchHistory = "W,W,W"
        )

        val chiefs = LeagueTableEntity(
            id = "PSL_2026_chiefs",
            league = LeagueType.PSL,
            tournamentType = TournamentType.PSL,
            teamId = "kaizer_chiefs",
            teamName = "Kaizer Chiefs",
            teamShortName = "Chiefs",
            played = 3,
            won = 2,
            drawn = 1,
            lost = 0,
            goalsFor = 5,
            goalsAgainst = 2,
            goalDifference = 3,
            points = 7,
            matchHistory = "W,D,W"
        )

        db.leagueTableDao().insertAll(listOf(chiefs, pirates, sundowns))

        val table = db.leagueTableDao().getPslTableFlow(2026).first()
        assertEquals(3, table.size)
        // 1st: Sundowns (9 pts, +7 GD)
        assertEquals("Mamelodi Sundowns", table[0].teamName)
        assertEquals(9, table[0].points)
        assertEquals(7, table[0].goalDifference)

        // 2nd: Pirates (9 pts, +5 GD)
        assertEquals("Orlando Pirates", table[1].teamName)
        assertEquals(9, table[1].points)
        assertEquals(5, table[1].goalDifference)

        // 3rd: Chiefs (7 pts, +3 GD)
        assertEquals("Kaizer Chiefs", table[2].teamName)
        assertEquals(7, table[2].points)
        assertEquals(3, table[2].goalDifference)
    }

    @Test
    fun testNfdTableOperationsAndMatchHistoryProgression() = runBlocking {
        val durbanCity = LeagueTableEntity(
            id = "NFD_2026_durban_city",
            league = LeagueType.NFD,
            tournamentType = TournamentType.NFD,
            teamId = "durban_city",
            teamName = "Durban City",
            teamShortName = "Durban City",
            played = 1,
            won = 1,
            drawn = 0,
            lost = 0,
            goalsFor = 2,
            goalsAgainst = 0,
            goalDifference = 2,
            points = 3,
            matchHistory = "W"
        )

        db.leagueTableDao().insertOrUpdate(durbanCity)

        // Match 2: Durban City draws 1-1
        val updated = durbanCity.copy(
            played = 2,
            drawn = 1,
            goalsFor = 3,
            goalsAgainst = 1,
            goalDifference = 2,
            points = 4,
            matchHistory = "W,D"
        )
        db.leagueTableDao().insertOrUpdate(updated)

        val fetched = db.leagueTableDao().getEntryForTeam(LeagueType.NFD, "durban_city", 2026)
        assertNotNull(fetched)
        assertEquals(4, fetched?.points)
        assertEquals("W,D", fetched?.matchHistory)
        assertEquals(listOf("W", "D"), fetched?.formList)
    }

    @Test
    fun testStandingEntityContainsTeamNamesAndMatchHistory() = runBlocking {
        val standing = StandingEntity(
            id = "PSL_2026_stellenbosch_fc",
            teamId = "stellenbosch_fc",
            tournamentType = TournamentType.PSL,
            teamName = "Stellenbosch FC",
            teamShortName = "Stellies",
            points = 6,
            goalDifference = 4,
            matchHistory = "W,W"
        )

        db.standingDao().upsert(standing)

        val fetched = db.standingDao().getStandingForTeam(TournamentType.PSL, "stellenbosch_fc", 2026)
        assertNotNull(fetched)
        assertEquals("Stellenbosch FC", fetched?.teamName)
        assertEquals("Stellies", fetched?.teamShortName)
        assertEquals(6, fetched?.points)
        assertEquals(4, fetched?.goalDifference)
        assertEquals("W,W", fetched?.matchHistory)
        assertEquals(listOf("W", "W"), fetched?.formList)
    }
}
