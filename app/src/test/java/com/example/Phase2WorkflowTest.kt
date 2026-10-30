package com.example

import com.example.data.model.TournamentType
import com.example.data.seed.SouthAfricanTeamsData
import com.example.data.seed.SouthAfricanTournamentsData
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class Phase2WorkflowTest {

    @Test
    fun testPslStandingsInitialState() {
        val teams = SouthAfricanTeamsData.pslTeams
        val standings = SouthAfricanTournamentsData.generateInitialStandings(teams)

        assertEquals(16, standings.size)
        standings.forEach { s ->
            assertEquals(0, s.played)
            assertEquals(0, s.points)
            assertEquals(0, s.goalDifference)
            assertEquals(TournamentType.PSL, s.tournamentType)
        }
    }

    @Test
    fun testDerbyMatchupsExistInFixtures() {
        val teams = SouthAfricanTeamsData.allTeams
        val fixtures = SouthAfricanTournamentsData.generateInitialFixtures(teams)

        // Tshwane Derby in GW1
        val tshwaneDerby = fixtures.find {
            (it.homeTeamId == "mamelodi_sundowns" && it.awayTeamId == "supersport_united") ||
                    (it.homeTeamId == "supersport_united" && it.awayTeamId == "mamelodi_sundowns")
        }
        assertNotNull("Tshwane Derby fixture must be scheduled in GW1", tshwaneDerby)

        // Limpopo Derby in GW1
        val limpopoDerby = fixtures.find {
            (it.homeTeamId == "sekhukhune_united" && it.awayTeamId == "magesi_fc")
        }
        assertNotNull("Limpopo Derby must be scheduled in GW1", limpopoDerby)
    }

    @Test
    fun testMtn8QuarterFinalFixturesCount() {
        val teams = SouthAfricanTeamsData.allTeams
        val fixtures = SouthAfricanTournamentsData.generateInitialFixtures(teams)
        val mtn8Matches = fixtures.filter { it.tournamentType == TournamentType.MTN8 }

        assertEquals("MTN8 must have 4 Quarter-Final fixtures", 4, mtn8Matches.size)
    }
}
