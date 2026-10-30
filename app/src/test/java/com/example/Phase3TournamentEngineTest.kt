package com.example

import com.example.data.model.MatchEntity
import com.example.data.model.MatchStatus
import com.example.data.model.TournamentType
import com.example.domain.tournament.TournamentEngine
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class Phase3TournamentEngineTest {

    @Test
    fun testMtn8DrawSeeding() {
        val top8 = listOf(
            "sundowns", "pirates", "chiefs", "stellies",
            "supersport", "sekhukhune", "ts_galaxy", "polokwane"
        )
        val qfs = TournamentEngine.generateMtn8QuarterFinals(top8, 2026)
        assertEquals(4, qfs.size)

        // 1st vs 8th
        assertEquals("sundowns", qfs[0].homeTeamId)
        assertEquals("polokwane", qfs[0].awayTeamId)

        // 2nd vs 7th
        assertEquals("pirates", qfs[1].homeTeamId)
        assertEquals("ts_galaxy", qfs[1].awayTeamId)

        // 3rd vs 6th
        assertEquals("chiefs", qfs[2].homeTeamId)
        assertEquals("sekhukhune", qfs[2].awayTeamId)

        // 4th vs 5th
        assertEquals("stellies", qfs[3].homeTeamId)
        assertEquals("supersport", qfs[3].awayTeamId)
    }

    @Test
    fun testMtn8SemiFinalsTwoLegs() {
        val winners = listOf("sundowns", "pirates", "chiefs", "stellies")
        val semis = TournamentEngine.generateMtn8SemiFinals(winners, 2026)
        assertEquals(4, semis.size) // 2 ties * 2 legs

        assertTrue(semis.any { it.homeTeamId == "sundowns" && it.awayTeamId == "stellies" && it.stage.contains("Leg 1") })
        assertTrue(semis.any { it.homeTeamId == "stellies" && it.awayTeamId == "sundowns" && it.stage.contains("Leg 2") })
    }

    @Test
    fun testCarlingKnockoutR16Count() {
        val pslTeams = (1..16).map { "psl_team_$it" }
        val r16 = TournamentEngine.generateCarlingKnockoutR16(pslTeams, 2026)
        assertEquals(8, r16.size) // 16 teams / 2 = 8 fixtures
        r16.forEach {
            assertEquals(TournamentType.CARLING_CKO, it.tournamentType)
            assertEquals("Round of 16", it.stage)
        }
    }

    @Test
    fun testNedbankCup32TeamDraw() {
        val psl = (1..16).map { "psl_$it" }
        val nfd = (1..8).map { "nfd_$it" }
        val r32 = TournamentEngine.generateNedbankCupRoundOf32(psl, nfd, 2026)
        assertEquals(16, r32.size) // 32 teams / 2 = 16 fixtures
        r32.forEach {
            assertEquals(TournamentType.NEDBANK_CUP, it.tournamentType)
            assertEquals("Round of 32", it.stage)
        }
    }

    @Test
    fun testPromotionPlayoffStructure() {
        val psl15th = "richards_bay_fc"
        val nfd2nd = "casric_stars"
        val nfd3rd = "hungry_lions"

        val fixtures = TournamentEngine.generatePromotionPlayoffFixtures(psl15th, nfd2nd, nfd3rd, 2026)
        assertEquals(6, fixtures.size) // 3 teams home and away round robin = 6 matches

        val standings = TournamentEngine.generatePlayoffStandings(psl15th, nfd2nd, nfd3rd, 2026)
        assertEquals(3, standings.size)
        standings.forEach {
            assertEquals(TournamentType.PLAYOFF, it.tournamentType)
            assertEquals(0, it.played)
            assertEquals(0, it.points)
        }
    }

    @Test
    fun testResolveMatchWinner() {
        val homeWin = MatchEntity(
            id = "m1", homeTeamId = "teamA", awayTeamId = "teamB",
            homeGoals = 2, awayGoals = 1, tournamentType = TournamentType.MTN8,
            gameWeek = 1, stage = "Final"
        )
        assertEquals("teamA", TournamentEngine.resolveMatchWinner(homeWin))

        val awayWin = MatchEntity(
            id = "m2", homeTeamId = "teamA", awayTeamId = "teamB",
            homeGoals = 0, awayGoals = 3, tournamentType = TournamentType.MTN8,
            gameWeek = 1, stage = "Final"
        )
        assertEquals("teamB", TournamentEngine.resolveMatchWinner(awayWin))

        val drawPens = MatchEntity(
            id = "m3", homeTeamId = "teamA", awayTeamId = "teamB",
            homeGoals = 1, awayGoals = 1, homePenalties = 5, awayPenalties = 4,
            tournamentType = TournamentType.MTN8, gameWeek = 1, stage = "Final"
        )
        assertEquals("teamA", TournamentEngine.resolveMatchWinner(drawPens))
    }
}
