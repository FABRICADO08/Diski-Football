package com.example.domain.tournament

import com.example.data.model.MatchEntity
import com.example.data.model.MatchStatus
import com.example.data.model.StandingEntity
import com.example.data.model.TournamentType
import kotlin.random.Random

data class PromotionRelegationResult(
    val autoPromotedNfdTeamId: String,
    val autoRelegatedPslTeamId: String,
    val playoffWinnerTeamId: String,
    val playoffLosersTeamIds: List<String>,
    val pslTop8TeamIds: List<String>,
    val summary: String
)

object TournamentEngine {

    /**
     * Generates MTN8 Quarter-Final fixtures from Top 8 PSL finishers:
     * 1st vs 8th, 2nd vs 7th, 3rd vs 6th, 4th vs 5th
     */
    fun generateMtn8QuarterFinals(top8Teams: List<String>, season: Int = 2026): List<MatchEntity> {
        if (top8Teams.size < 8) return emptyList()

        val seeds = listOf(
            Pair(top8Teams[0], top8Teams[7]), // 1 vs 8
            Pair(top8Teams[1], top8Teams[6]), // 2 vs 7
            Pair(top8Teams[2], top8Teams[5]), // 3 vs 6
            Pair(top8Teams[3], top8Teams[4])  // 4 vs 5
        )

        return seeds.mapIndexed { idx, (home, away) ->
            MatchEntity(
                id = "mtn8_${season}_qf_${idx + 1}",
                homeTeamId = home,
                awayTeamId = away,
                tournamentType = TournamentType.MTN8,
                gameWeek = 1,
                stage = "Quarter-Final",
                seasonYear = season,
                status = MatchStatus.SCHEDULED
            )
        }
    }

    /**
     * Advances MTN8 from QFs to 2-Leg Semi-Finals
     */
    fun generateMtn8SemiFinals(qfWinners: List<String>, season: Int = 2026): List<MatchEntity> {
        if (qfWinners.size < 4) return emptyList()

        val matches = mutableListOf<MatchEntity>()
        // Semi 1: Leg 1 and Leg 2
        matches.add(
            MatchEntity(
                id = "mtn8_${season}_sf1_leg1",
                homeTeamId = qfWinners[0],
                awayTeamId = qfWinners[3],
                tournamentType = TournamentType.MTN8,
                gameWeek = 2,
                stage = "Semi-Final (Leg 1)",
                seasonYear = season
            )
        )
        matches.add(
            MatchEntity(
                id = "mtn8_${season}_sf1_leg2",
                homeTeamId = qfWinners[3],
                awayTeamId = qfWinners[0],
                tournamentType = TournamentType.MTN8,
                gameWeek = 3,
                stage = "Semi-Final (Leg 2)",
                seasonYear = season
            )
        )

        // Semi 2: Leg 1 and Leg 2
        matches.add(
            MatchEntity(
                id = "mtn8_${season}_sf2_leg1",
                homeTeamId = qfWinners[1],
                awayTeamId = qfWinners[2],
                tournamentType = TournamentType.MTN8,
                gameWeek = 2,
                stage = "Semi-Final (Leg 1)",
                seasonYear = season
            )
        )
        matches.add(
            MatchEntity(
                id = "mtn8_${season}_sf2_leg2",
                homeTeamId = qfWinners[2],
                awayTeamId = qfWinners[1],
                tournamentType = TournamentType.MTN8,
                gameWeek = 3,
                stage = "Semi-Final (Leg 2)",
                seasonYear = season
            )
        )

        return matches
    }

    /**
     * Generates MTN8 Grand Final fixture at Moses Mabhida Stadium
     */
    fun generateMtn8Final(finalists: List<String>, season: Int = 2026): MatchEntity {
        val home = finalists.getOrElse(0) { "mamelodi_sundowns" }
        val away = finalists.getOrElse(1) { "orlando_pirates" }
        return MatchEntity(
            id = "mtn8_${season}_final",
            homeTeamId = home,
            awayTeamId = away,
            tournamentType = TournamentType.MTN8,
            gameWeek = 4,
            stage = "Grand Final (Moses Mabhida Stadium)",
            seasonYear = season
        )
    }

    /**
     * Generates Carling Knockout Cup Round of 16 (all 16 PSL clubs)
     */
    fun generateCarlingKnockoutR16(pslTeamIds: List<String>, season: Int = 2026): List<MatchEntity> {
        val shuffled = pslTeamIds.shuffled(Random(season))
        val matches = mutableListOf<MatchEntity>()

        for (i in 0 until (shuffled.size / 2)) {
            matches.add(
                MatchEntity(
                    id = "carling_${season}_r16_${i + 1}",
                    homeTeamId = shuffled[i * 2],
                    awayTeamId = shuffled[i * 2 + 1],
                    tournamentType = TournamentType.CARLING_CKO,
                    gameWeek = 1,
                    stage = "Round of 16",
                    seasonYear = season
                )
            )
        }
        return matches
    }

    /**
     * Generates Carling All-Star Exhibition match:
     * Carling Knockout Champion vs Carling All-Star XI (represented by league runner-up/all-stars)
     */
    fun generateCarlingAllStarMatch(championId: String, allStarOpponentId: String, season: Int = 2026): MatchEntity {
        return MatchEntity(
            id = "carling_${season}_allstar_match",
            homeTeamId = championId,
            awayTeamId = allStarOpponentId,
            tournamentType = TournamentType.CARLING_CKO,
            gameWeek = 5,
            stage = "Carling All-Star Match (Peter Mokaba Stadium)",
            seasonYear = season
        )
    }

    /**
     * Generates Nedbank Cup Round of 32 (16 PSL + 8 NFD + 8 Amateur/Regional Clubs)
     */
    fun generateNedbankCupRoundOf32(
        pslTeamIds: List<String>,
        nfdTeamIds: List<String>,
        season: Int = 2026
    ): List<MatchEntity> {
        // Pool of 32 teams: 16 PSL + 8 NFD + 8 Regional Amateurs
        val amateurClubs = listOf(
            "amateur_dondol_stars",
            "amateur_clarewood_fc",
            "amateur_liverpool_btx",
            "amateur_fc_ravens",
            "amateur_sp_united",
            "amateur_green_lovers",
            "amateur_african_allstars",
            "amateur_moria_city"
        )

        val fullPool = (pslTeamIds + nfdTeamIds.take(8) + amateurClubs).shuffled(Random(season + 42))
        val matches = mutableListOf<MatchEntity>()

        for (i in 0 until 16) {
            matches.add(
                MatchEntity(
                    id = "nedbank_${season}_r32_${i + 1}",
                    homeTeamId = fullPool[i * 2],
                    awayTeamId = fullPool[i * 2 + 1],
                    tournamentType = TournamentType.NEDBANK_CUP,
                    gameWeek = 1,
                    stage = "Round of 32",
                    seasonYear = season
                )
            )
        }
        return matches
    }

    /**
     * Generates the official 3-Team Promotion/Relegation Play-off Mini-League
     * 15th PSL vs 2nd NFD vs 3rd NFD
     * 6 total round-robin home-and-away matches
     */
    fun generatePromotionPlayoffFixtures(
        psl15thTeamId: String,
        nfd2ndTeamId: String,
        nfd3rdTeamId: String,
        season: Int = 2026
    ): List<MatchEntity> {
        val pairs = listOf(
            Pair(psl15thTeamId, nfd2ndTeamId),
            Pair(nfd3rdTeamId, psl15thTeamId),
            Pair(nfd2ndTeamId, nfd3rdTeamId),
            Pair(nfd2ndTeamId, psl15thTeamId),
            Pair(psl15thTeamId, nfd3rdTeamId),
            Pair(nfd3rdTeamId, nfd2ndTeamId)
        )

        return pairs.mapIndexed { idx, (home, away) ->
            MatchEntity(
                id = "playoff_${season}_match_${idx + 1}",
                homeTeamId = home,
                awayTeamId = away,
                tournamentType = TournamentType.PLAYOFF,
                gameWeek = idx + 1,
                stage = "Play-off Match ${idx + 1}",
                seasonYear = season
            )
        }
    }

    /**
     * Initial standings for the 3-team Play-off mini-league
     */
    fun generatePlayoffStandings(
        psl15thTeamId: String,
        nfd2ndTeamId: String,
        nfd3rdTeamId: String,
        season: Int = 2026
    ): List<StandingEntity> {
        return listOf(psl15thTeamId, nfd2ndTeamId, nfd3rdTeamId).mapIndexed { index, teamId ->
            StandingEntity(
                id = "PLAYOFF_${season}_$teamId",
                teamId = teamId,
                tournamentType = TournamentType.PLAYOFF,
                season = season,
                played = 0,
                won = 0,
                drawn = 0,
                lost = 0,
                goalsFor = 0,
                goalsAgainst = 0,
                goalDifference = 0,
                points = 0,
                position = index + 1
            )
        }
    }

    /**
     * Determines match winner, resolving ties with extra time / penalties
     */
    fun resolveMatchWinner(match: MatchEntity): String {
        val homeG = match.homeGoals ?: 0
        val awayG = match.awayGoals ?: 0

        return if (homeG > awayG) {
            match.homeTeamId
        } else if (awayG > homeG) {
            match.awayTeamId
        } else {
            // Decided by penalties if draw in knockout
            val homePens = match.homePenalties ?: (4..5).random()
            val awayPens = match.awayPenalties ?: if (homePens == 5) 4 else 5
            if (homePens > awayPens) match.homeTeamId else match.awayTeamId
        }
    }
}
