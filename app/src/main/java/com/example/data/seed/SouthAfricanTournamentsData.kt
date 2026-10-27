package com.example.data.seed

import com.example.data.model.MatchEntity
import com.example.data.model.MatchStatus
import com.example.data.model.StandingEntity
import com.example.data.model.TeamEntity
import com.example.data.model.TournamentEntity
import com.example.data.model.TournamentType

object SouthAfricanTournamentsData {

    fun generateInitialTournaments(): List<TournamentEntity> = listOf(
        TournamentEntity(
            id = "psl_2026",
            type = TournamentType.PSL,
            name = "Betway Premiership",
            season = 2026,
            currentRound = "GameWeek 1",
            bracketData = """{"rounds": 30, "teams": 16, "promotionSpots": 0, "relegationSpots": 1, "playoffSpots": 1}"""
        ),
        TournamentEntity(
            id = "nfd_2026",
            type = TournamentType.NFD,
            name = "Motsepe Foundation Championship",
            season = 2026,
            currentRound = "GameWeek 1",
            bracketData = """{"rounds": 30, "teams": 16, "autoPromotion": 1, "playoffPromotion": 2, "relegationSpots": 2}"""
        ),
        TournamentEntity(
            id = "mtn8_2026",
            type = TournamentType.MTN8,
            name = "MTN8 Cup (Wafa Wafa)",
            season = 2026,
            currentRound = "Quarter-Finals",
            bracketData = """{"participants": 8, "format": "Single Elimination + 2-leg Semis", "prizePool": "R10,000,000"}"""
        ),
        TournamentEntity(
            id = "carling_2026",
            type = TournamentType.CARLING_CKO,
            name = "Carling Knockout Cup",
            season = 2026,
            currentRound = "Round of 16",
            bracketData = """{"participants": 16, "format": "Knockout + Carling All-Star Finale"}"""
        ),
        TournamentEntity(
            id = "nedbank_2026",
            type = TournamentType.NEDBANK_CUP,
            name = "Nedbank Cup",
            season = 2026,
            currentRound = "Round of 32",
            bracketData = """{"participants": 32, "format": "FA Cup Style National Knockout"}"""
        ),
        TournamentEntity(
            id = "playoff_2026",
            type = TournamentType.PLAYOFF,
            name = "PSL / NFD Promotion Play-offs",
            season = 2026,
            currentRound = "Play-off Mini-League",
            bracketData = """{"format": "3-Team Round Robin (15th PSL vs 2nd & 3rd NFD)"}"""
        )
    )

    fun generateInitialStandings(teams: List<TeamEntity>): List<StandingEntity> {
        val pslTeams = teams.filter { it.league == com.example.data.model.LeagueType.PSL }
        val nfdTeams = teams.filter { it.league == com.example.data.model.LeagueType.NFD }

        val pslStandings = pslTeams.mapIndexed { index, team ->
            StandingEntity(
                id = "PSL_2026_${team.id}",
                teamId = team.id,
                tournamentType = TournamentType.PSL,
                season = 2026,
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

        val nfdStandings = nfdTeams.mapIndexed { index, team ->
            StandingEntity(
                id = "NFD_2026_${team.id}",
                teamId = team.id,
                tournamentType = TournamentType.NFD,
                season = 2026,
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

        return pslStandings + nfdStandings
    }

    fun generateInitialFixtures(teams: List<TeamEntity>): List<MatchEntity> {
        val matches = mutableListOf<MatchEntity>()

        // PSL GameWeek 1
        val pslMatches = listOf(
            Pair("mamelodi_sundowns", "supersport_united"), // Tshwane Derby
            Pair("orlando_pirates", "polokwane_city"),
            Pair("kaizer_chiefs", "marumo_gallants"),
            Pair("stellenbosch_fc", "golden_arrows"),
            Pair("sekhukhune_united", "magesi_fc"),           // Limpopo Derby
            Pair("ts_galaxy", "richards_bay_fc"),
            Pair("amazulu_fc", "chippa_united"),
            Pair("cape_town_city", "royal_am")
        )

        pslMatches.forEachIndexed { idx, pair ->
            matches.add(
                MatchEntity(
                    id = "match_psl_gw1_${idx + 1}",
                    homeTeamId = pair.first,
                    awayTeamId = pair.second,
                    tournamentType = TournamentType.PSL,
                    gameWeek = 1,
                    stage = "GameWeek 1",
                    seasonYear = 2026,
                    status = MatchStatus.SCHEDULED
                )
            )
        }

        // MTN8 Quarter Finals (1 vs 8, 2 vs 7, 3 vs 6, 4 vs 5)
        val mtn8Matches = listOf(
            Pair("mamelodi_sundowns", "polokwane_city"),
            Pair("orlando_pirates", "supersport_united"),
            Pair("stellenbosch_fc", "ts_galaxy"),
            Pair("sekhukhune_united", "cape_town_city")
        )

        mtn8Matches.forEachIndexed { idx, pair ->
            matches.add(
                MatchEntity(
                    id = "match_mtn8_qf_${idx + 1}",
                    homeTeamId = pair.first,
                    awayTeamId = pair.second,
                    tournamentType = TournamentType.MTN8,
                    gameWeek = 1,
                    stage = "Quarter-Final",
                    seasonYear = 2026,
                    status = MatchStatus.SCHEDULED
                )
            )
        }

        return matches
    }
}
