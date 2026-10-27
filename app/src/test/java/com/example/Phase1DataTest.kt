package com.example

import com.example.data.model.LeagueType
import com.example.data.model.TournamentType
import com.example.data.seed.SouthAfricanPlayersData
import com.example.data.seed.SouthAfricanTeamsData
import com.example.data.seed.SouthAfricanTournamentsData
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class Phase1DataTest {

    @Test
    fun testSouthAfricanTeamsCount() {
        val pslTeams = SouthAfricanTeamsData.pslTeams
        val nfdTeams = SouthAfricanTeamsData.nfdTeams
        val allTeams = SouthAfricanTeamsData.allTeams

        assertEquals("PSL must have exactly 16 teams", 16, pslTeams.size)
        assertEquals("NFD must have exactly 16 teams", 16, nfdTeams.size)
        assertEquals("Total South African clubs must be 32", 32, allTeams.size)

        // Verify key flagship clubs exist
        val sundowns = allTeams.find { it.id == "mamelodi_sundowns" }
        assertNotNull("Mamelodi Sundowns must exist", sundowns)
        assertEquals(LeagueType.PSL, sundowns?.league)

        val pirates = allTeams.find { it.id == "orlando_pirates" }
        assertNotNull("Orlando Pirates must exist", pirates)
        assertEquals(LeagueType.PSL, pirates?.league)

        val chiefs = allTeams.find { it.id == "kaizer_chiefs" }
        assertNotNull("Kaizer Chiefs must exist", chiefs)
        assertEquals(LeagueType.PSL, chiefs?.league)

        val amatuks = allTeams.find { it.id == "university_of_pretoria" }
        assertNotNull("AmaTuks must exist in NFD", amatuks)
        assertEquals(LeagueType.NFD, amatuks?.league)
    }

    @Test
    fun testSquadsGeneration() {
        val allTeams = SouthAfricanTeamsData.allTeams
        val allPlayers = SouthAfricanPlayersData.generateAllPlayers(allTeams)

        assertTrue("Players count must be at least 480 (15 players * 32 teams)", allPlayers.size >= 480)

        // Verify Sundowns squad contains Peter Shalulile and Ronwen Williams
        val shalulile = allPlayers.find { it.name == "Peter Shalulile" }
        assertNotNull("Peter Shalulile must exist in squad", shalulile)
        assertEquals("mamelodi_sundowns", shalulile?.teamId)
        assertTrue("Shalulile overall rating should be elite (>=80)", (shalulile?.overall ?: 0) >= 80)

        val ronwen = allPlayers.find { it.name == "Ronwen Williams" }
        assertNotNull("Ronwen Williams must exist in squad", ronwen)
        assertEquals("mamelodi_sundowns", ronwen?.teamId)
    }

    @Test
    fun testSouthAfricanTournaments() {
        val tournaments = SouthAfricanTournamentsData.generateInitialTournaments()

        assertEquals("Tournaments count should be 6", 6, tournaments.size)
        assertTrue(tournaments.any { it.type == TournamentType.PSL })
        assertTrue(tournaments.any { it.type == TournamentType.NFD })
        assertTrue(tournaments.any { it.type == TournamentType.MTN8 })
        assertTrue(tournaments.any { it.type == TournamentType.CARLING_CKO })
        assertTrue(tournaments.any { it.type == TournamentType.NEDBANK_CUP })
        assertTrue(tournaments.any { it.type == TournamentType.PLAYOFF })
    }

    @Test
    fun testInitialStandingsAndFixtures() {
        val allTeams = SouthAfricanTeamsData.allTeams
        val standings = SouthAfricanTournamentsData.generateInitialStandings(allTeams)
        val fixtures = SouthAfricanTournamentsData.generateInitialFixtures(allTeams)

        assertEquals("32 teams must have standings entries", 32, standings.size)
        assertTrue("Fixtures should include PSL GameWeek 1 and MTN8 QFs", fixtures.size >= 12)
    }
}
