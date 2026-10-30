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
    fun testPslTeamsCountAndDetails() {
        val psl = SouthAfricanTeamsData.pslTeams
        assertEquals("PSL must have exactly 16 teams", 16, psl.size)

        val sundowns = psl.find { it.id == "mamelodi_sundowns" }
        assertNotNull("Sundowns must exist", sundowns)
        assertEquals("Sundowns", sundowns?.shortName)
        assertEquals(LeagueType.PSL, sundowns?.league)

        val pirates = psl.find { it.id == "orlando_pirates" }
        assertNotNull("Pirates must exist", pirates)

        val chiefs = psl.find { it.id == "kaizer_chiefs" }
        assertNotNull("Chiefs must exist", chiefs)
    }

    @Test
    fun testNfdTeamsCount() {
        val nfd = SouthAfricanTeamsData.nfdTeams
        assertEquals("NFD must have exactly 16 teams", 16, nfd.size)

        val amatuks = nfd.find { it.id == "university_of_pretoria" }
        assertNotNull("University of Pretoria (AmaTuks) must exist", amatuks)
        assertEquals(LeagueType.NFD, amatuks?.league)
    }

    @Test
    fun testTotalTeamsAndPlayerGeneration() {
        val allTeams = SouthAfricanTeamsData.allTeams
        assertEquals("Total South African teams must be 32", 32, allTeams.size)

        val players = SouthAfricanPlayersData.generateAllPlayers(allTeams)
        assertTrue("Every team must have players (at least 32 * 15 = 480)", players.size >= 480)

        val rWilliams = players.find { it.name == "Ronwen Williams" }
        assertNotNull("Ronwen Williams should exist in Sundowns", rWilliams)
        assertEquals("mamelodi_sundowns", rWilliams?.teamId)
        assertTrue("Williams rating must reflect star keeper status", (rWilliams?.overall ?: 0) >= 84)

        val pMaswanganyi = players.find { it.name == "Patrick Maswanganyi" }
        assertNotNull("Patrick Maswanganyi should exist in Pirates", pMaswanganyi)
        assertEquals("orlando_pirates", pMaswanganyi?.teamId)
    }

    @Test
    fun testTournamentsAndFixtures() {
        val tournaments = SouthAfricanTournamentsData.generateInitialTournaments()
        assertEquals(6, tournaments.size)

        val types = tournaments.map { it.type }
        assertTrue(types.contains(TournamentType.PSL))
        assertTrue(types.contains(TournamentType.NFD))
        assertTrue(types.contains(TournamentType.MTN8))
        assertTrue(types.contains(TournamentType.CARLING_CKO))
        assertTrue(types.contains(TournamentType.NEDBANK_CUP))
        assertTrue(types.contains(TournamentType.PLAYOFF))

        val allTeams = SouthAfricanTeamsData.allTeams
        val fixtures = SouthAfricanTournamentsData.generateInitialFixtures(allTeams)
        assertTrue("Fixtures must be generated for GW1 and MTN8", fixtures.isNotEmpty())

        val standings = SouthAfricanTournamentsData.generateInitialStandings(allTeams)
        assertEquals("Standings must cover 16 PSL + 16 NFD = 32 entries", 32, standings.size)
    }
}
