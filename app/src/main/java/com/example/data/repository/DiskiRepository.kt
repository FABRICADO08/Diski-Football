package com.example.data.repository

import com.example.data.database.DiskiDatabase
import com.example.data.model.CareerSaveEntity
import com.example.data.model.LeagueType
import com.example.data.model.MatchEntity
import com.example.data.model.MatchStatus
import com.example.data.model.PlayerEntity
import com.example.data.model.StandingEntity
import com.example.data.model.TeamEntity
import com.example.data.model.TournamentEntity
import com.example.data.model.TournamentType
import com.example.data.seed.DatabaseInitializer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import kotlin.random.Random

data class DatabaseStats(
    val teamCount: Int,
    val playerCount: Int,
    val tournamentCount: Int,
    val matchCount: Int,
    val standingCount: Int
)

class DiskiRepository(private val database: DiskiDatabase) {

    private val initializer = DatabaseInitializer(database)

    suspend fun initialize() {
        initializer.populateIfNeeded()
    }

    suspend fun reseedDatabase() {
        initializer.resetAndRebuild()
    }

    suspend fun getStats(): DatabaseStats {
        return DatabaseStats(
            teamCount = database.teamDao().getTeamCount(),
            playerCount = database.playerDao().getPlayerCount(),
            tournamentCount = database.tournamentDao().getTournamentCount(),
            matchCount = database.matchDao().getMatchCount(),
            standingCount = database.standingDao().getStandingCount()
        )
    }

    // Teams
    fun getAllTeams(): Flow<List<TeamEntity>> = database.teamDao().getAllTeamsFlow()

    fun getPslTeams(): Flow<List<TeamEntity>> = database.teamDao().getTeamsByLeagueFlow(LeagueType.PSL)

    fun getNfdTeams(): Flow<List<TeamEntity>> = database.teamDao().getTeamsByLeagueFlow(LeagueType.NFD)

    fun getTeamById(teamId: String): Flow<TeamEntity?> = database.teamDao().getTeamByIdFlow(teamId)

    suspend fun getTeamDirect(teamId: String): TeamEntity? = database.teamDao().getTeamById(teamId)

    // Players
    fun getSquadForTeam(teamId: String): Flow<List<PlayerEntity>> = database.playerDao().getPlayersByTeamFlow(teamId)

    fun getPlayerById(playerId: String): Flow<PlayerEntity?> = database.playerDao().getPlayerByIdFlow(playerId)

    fun getTransferMarketPlayers(excludeTeamId: String): Flow<List<PlayerEntity>> =
        database.playerDao().getTransferMarketPlayers(excludeTeamId)

    suspend fun swapStarterStatus(starterId: String, subId: String) = withContext(Dispatchers.IO) {
        val starter = database.playerDao().getPlayerById(starterId)
        val sub = database.playerDao().getPlayerById(subId)
        if (starter != null && sub != null) {
            database.playerDao().updatePlayer(starter.copy(isStartingEleven = false))
            database.playerDao().updatePlayer(sub.copy(isStartingEleven = true))
        }
    }

    suspend fun buyPlayer(playerId: String, buyerTeamId: String) = withContext(Dispatchers.IO) {
        val player = database.playerDao().getPlayerById(playerId)
        val career = database.careerSaveDao().getActiveSave()
        if (player != null && career != null && career.transferBudgetRemaining >= player.marketValue) {
            val updatedCareer = career.copy(
                transferBudgetRemaining = career.transferBudgetRemaining - player.marketValue
            )
            database.careerSaveDao().updateSave(updatedCareer)
            database.playerDao().updatePlayer(player.copy(teamId = buyerTeamId, isStartingEleven = false))
        }
    }

    // Tournaments
    fun getAllTournaments(): Flow<List<TournamentEntity>> = database.tournamentDao().getAllTournamentsFlow()

    fun getTournament(type: TournamentType): Flow<TournamentEntity?> = database.tournamentDao().getTournamentFlow(type)

    // Standings
    fun getStandings(type: TournamentType): Flow<List<StandingEntity>> = database.standingDao().getStandingsFlow(type)

    // Matches
    fun getMatches(type: TournamentType): Flow<List<MatchEntity>> = database.matchDao().getMatchesByTournamentFlow(type)

    fun getTeamMatches(teamId: String): Flow<List<MatchEntity>> = database.matchDao().getTeamMatchesFlow(teamId)

    suspend fun getNextMatchForTeam(teamId: String): MatchEntity? =
        database.matchDao().getNextMatchForTeam(teamId)

    // Match Simulation Engine
    suspend fun simulateMatch(matchId: String): MatchEntity? = withContext(Dispatchers.IO) {
        val match = database.matchDao().getMatchById(matchId) ?: return@withContext null
        if (match.status == MatchStatus.FINISHED) return@withContext match

        val homeTeam = database.teamDao().getTeamById(match.homeTeamId)
        val awayTeam = database.teamDao().getTeamById(match.awayTeamId)

        val homeRating = homeTeam?.ratingOverall ?: 75
        val awayRating = awayTeam?.ratingOverall ?: 75

        // Rating differential with home advantage
        val diff = (homeRating + 3) - awayRating

        val homeGoalsBase = when {
            diff > 5 -> Random.nextInt(1, 4)
            diff < -5 -> Random.nextInt(0, 2)
            else -> Random.nextInt(0, 3)
        }
        val awayGoalsBase = when {
            diff < -5 -> Random.nextInt(1, 4)
            diff > 5 -> Random.nextInt(0, 2)
            else -> Random.nextInt(0, 3)
        }

        val completedMatch = match.copy(
            homeGoals = homeGoalsBase,
            awayGoals = awayGoalsBase,
            status = MatchStatus.FINISHED,
            matchSummary = "FT: ${homeTeam?.shortName ?: "Home"} $homeGoalsBase - $awayGoalsBase ${awayTeam?.shortName ?: "Away"}"
        )
        database.matchDao().updateMatch(completedMatch)

        // Update Standings if League Match
        if (match.tournamentType == TournamentType.PSL || match.tournamentType == TournamentType.NFD) {
            updateLeagueTableAfterMatch(match.tournamentType, match.homeTeamId, match.awayTeamId, homeGoalsBase, awayGoalsBase)
        }

        // Advance Career GameWeek if played by manager's team
        val career = database.careerSaveDao().getActiveSave()
        if (career != null && (career.selectedTeamId == match.homeTeamId || career.selectedTeamId == match.awayTeamId)) {
            val nextGw = (career.currentGameWeek + 1).coerceAtMost(30)
            val updatedCareer = career.copy(currentGameWeek = nextGw)
            database.careerSaveDao().updateSave(updatedCareer)
        }

        completedMatch
    }

    suspend fun recordPlayedMatchResult(
        matchId: String,
        homeGoals: Int,
        awayGoals: Int,
        matchSummary: String
    ): MatchEntity? = withContext(Dispatchers.IO) {
        val match = database.matchDao().getMatchById(matchId) ?: return@withContext null
        val completedMatch = match.copy(
            homeGoals = homeGoals,
            awayGoals = awayGoals,
            status = MatchStatus.FINISHED,
            matchSummary = matchSummary
        )
        database.matchDao().updateMatch(completedMatch)

        if (match.tournamentType == TournamentType.PSL || match.tournamentType == TournamentType.NFD || match.tournamentType == TournamentType.PLAYOFF) {
            updateLeagueTableAfterMatch(match.tournamentType, match.homeTeamId, match.awayTeamId, homeGoals, awayGoals)
        }

        val career = database.careerSaveDao().getActiveSave()
        if (career != null && (career.selectedTeamId == match.homeTeamId || career.selectedTeamId == match.awayTeamId)) {
            val nextGw = (career.currentGameWeek + 1).coerceAtMost(30)
            database.careerSaveDao().updateSave(career.copy(currentGameWeek = nextGw))
        }

        completedMatch
    }

    private suspend fun updateLeagueTableAfterMatch(
        type: TournamentType,
        homeTeamId: String,
        awayTeamId: String,
        homeGoals: Int,
        awayGoals: Int
    ) {
        val homeStanding = database.standingDao().getStandingForTeam(type, homeTeamId)
        val awayStanding = database.standingDao().getStandingForTeam(type, awayTeamId)

        if (homeStanding != null) {
            val won = if (homeGoals > awayGoals) 1 else 0
            val drawn = if (homeGoals == awayGoals) 1 else 0
            val lost = if (homeGoals < awayGoals) 1 else 0
            val points = (won * 3) + drawn

            val updatedHome = homeStanding.copy(
                played = homeStanding.played + 1,
                won = homeStanding.won + won,
                drawn = homeStanding.drawn + drawn,
                lost = homeStanding.lost + lost,
                goalsFor = homeStanding.goalsFor + homeGoals,
                goalsAgainst = homeStanding.goalsAgainst + awayGoals,
                goalDifference = (homeStanding.goalsFor + homeGoals) - (homeStanding.goalsAgainst + awayGoals),
                points = homeStanding.points + points
            )
            database.standingDao().upsert(updatedHome)
        }

        if (awayStanding != null) {
            val won = if (awayGoals > homeGoals) 1 else 0
            val drawn = if (homeGoals == awayGoals) 1 else 0
            val lost = if (awayGoals < homeGoals) 1 else 0
            val points = (won * 3) + drawn

            val updatedAway = awayStanding.copy(
                played = awayStanding.played + 1,
                won = awayStanding.won + won,
                drawn = awayStanding.drawn + drawn,
                lost = awayStanding.lost + lost,
                goalsFor = awayStanding.goalsFor + awayGoals,
                goalsAgainst = awayStanding.goalsAgainst + homeGoals,
                goalDifference = (awayStanding.goalsFor + awayGoals) - (awayStanding.goalsAgainst + homeGoals),
                points = awayStanding.points + points
            )
            database.standingDao().upsert(updatedAway)
        }
    }

    // Career
    fun getActiveCareerSave(): Flow<CareerSaveEntity?> = database.careerSaveDao().getActiveSaveFlow()

    suspend fun createNewCareer(managerName: String, teamId: String, difficulty: String): CareerSaveEntity = withContext(Dispatchers.IO) {
        val team = database.teamDao().getTeamById(teamId)
        val budget = team?.transferBudget ?: 40_000_000L
        val save = CareerSaveEntity(
            id = 1,
            managerName = managerName,
            selectedTeamId = teamId,
            currentSeason = 2026,
            currentGameWeek = 1,
            managerReputation = 60,
            transferBudgetRemaining = budget,
            difficulty = difficulty,
            formation = "4-3-3",
            teamMorale = 85
        )
        database.careerSaveDao().insertSave(save)
        save
    }

    suspend fun updateCareerFormation(formation: String) = withContext(Dispatchers.IO) {
        val career = database.careerSaveDao().getActiveSave()
        if (career != null) {
            database.careerSaveDao().updateSave(career.copy(formation = formation))
        }
    }

    suspend fun resetCareer() = withContext(Dispatchers.IO) {
        database.careerSaveDao().deleteSave()
    }

    // --- TOURNAMENT PROGRESSION & BRACKET GENERATION (PHASE 3) ---

    suspend fun generateCarlingKnockoutTournament(season: Int = 2026) = withContext(Dispatchers.IO) {
        val pslTeams = database.teamDao().getTeamsByLeague(LeagueType.PSL)
        val fixtures = com.example.domain.tournament.TournamentEngine.generateCarlingKnockoutR16(
            pslTeams.map { it.id },
            season
        )
        database.matchDao().insertAll(fixtures)
        val tournament = database.tournamentDao().getTournament(TournamentType.CARLING_CKO, season)
        if (tournament != null) {
            database.tournamentDao().update(tournament.copy(currentRound = "Round of 16"))
        }
    }

    suspend fun generateNedbankCupTournament(season: Int = 2026) = withContext(Dispatchers.IO) {
        val pslTeams = database.teamDao().getTeamsByLeague(LeagueType.PSL)
        val nfdTeams = database.teamDao().getTeamsByLeague(LeagueType.NFD)
        val fixtures = com.example.domain.tournament.TournamentEngine.generateNedbankCupRoundOf32(
            pslTeams.map { it.id },
            nfdTeams.map { it.id },
            season
        )
        database.matchDao().insertAll(fixtures)
        val tournament = database.tournamentDao().getTournament(TournamentType.NEDBANK_CUP, season)
        if (tournament != null) {
            database.tournamentDao().update(tournament.copy(currentRound = "Round of 32"))
        }
    }

    suspend fun startPromotionPlayoffTournament(season: Int = 2026) = withContext(Dispatchers.IO) {
        val pslStandings = database.standingDao().getStandings(TournamentType.PSL, season)
        val nfdStandings = database.standingDao().getStandings(TournamentType.NFD, season)

        val psl15th = pslStandings.getOrNull(14)?.teamId ?: "richards_bay_fc"
        val nfd2nd = nfdStandings.getOrNull(1)?.teamId ?: "casric_stars"
        val nfd3rd = nfdStandings.getOrNull(2)?.teamId ?: "hungry_lions"

        val fixtures = com.example.domain.tournament.TournamentEngine.generatePromotionPlayoffFixtures(
            psl15th, nfd2nd, nfd3rd, season
        )
        val standings = com.example.domain.tournament.TournamentEngine.generatePlayoffStandings(
            psl15th, nfd2nd, nfd3rd, season
        )

        database.matchDao().insertAll(fixtures)
        database.standingDao().insertAll(standings)

        val tournament = database.tournamentDao().getTournament(TournamentType.PLAYOFF, season)
        if (tournament != null) {
            database.tournamentDao().update(tournament.copy(currentRound = "Play-off Mini League"))
        }
    }

    suspend fun executeSeasonEndPromotionRelegation(season: Int = 2026): com.example.domain.tournament.PromotionRelegationResult = withContext(Dispatchers.IO) {
        val pslStandings = database.standingDao().getStandings(TournamentType.PSL, season)
        val nfdStandings = database.standingDao().getStandings(TournamentType.NFD, season)
        val playoffStandings = database.standingDao().getStandings(TournamentType.PLAYOFF, season)

        // 1. Automatic Relegation: 16th PSL -> NFD
        val autoRelegatedPslId = pslStandings.lastOrNull()?.teamId ?: "marumo_gallants"
        val autoRelegatedTeam = database.teamDao().getTeamById(autoRelegatedPslId)
        if (autoRelegatedTeam != null) {
            database.teamDao().updateTeam(autoRelegatedTeam.copy(league = LeagueType.NFD))
        }

        // 2. Automatic Promotion: 1st NFD -> PSL
        val autoPromotedNfdId = nfdStandings.firstOrNull()?.teamId ?: "university_of_pretoria"
        val autoPromotedTeam = database.teamDao().getTeamById(autoPromotedNfdId)
        if (autoPromotedTeam != null) {
            database.teamDao().updateTeam(autoPromotedTeam.copy(league = LeagueType.PSL))
        }

        // 3. Play-off winner claims PSL spot
        val playoffWinnerId = playoffStandings.firstOrNull()?.teamId ?: pslStandings.getOrNull(14)?.teamId ?: "richards_bay_fc"
        val playoffWinnerTeam = database.teamDao().getTeamById(playoffWinnerId)
        if (playoffWinnerTeam != null) {
            database.teamDao().updateTeam(playoffWinnerTeam.copy(league = LeagueType.PSL))
        }

        val playoffLosers = playoffStandings.drop(1).map { it.teamId }
        for (loserId in playoffLosers) {
            val loserTeam = database.teamDao().getTeamById(loserId)
            if (loserTeam != null && loserTeam.id != autoPromotedNfdId) {
                database.teamDao().updateTeam(loserTeam.copy(league = LeagueType.NFD))
            }
        }

        // 4. MTN8 Qualifiers for Next Season (Top 8 PSL clubs)
        val top8 = pslStandings.take(8).map { it.teamId }
        val mtn8NextSeasonFixtures = com.example.domain.tournament.TournamentEngine.generateMtn8QuarterFinals(top8, season + 1)
        database.matchDao().insertAll(mtn8NextSeasonFixtures)

        val summary = "Season $season Complete!\n" +
                "• Auto-Promoted to PSL: ${autoPromotedTeam?.name ?: autoPromotedNfdId}\n" +
                "• Auto-Relegated to NFD: ${autoRelegatedTeam?.name ?: autoRelegatedPslId}\n" +
                "• Play-off Winner (Earns PSL berth): ${playoffWinnerTeam?.name ?: playoffWinnerId}\n" +
                "• Next Season MTN8 Qualifiers seeded in database."

        com.example.domain.tournament.PromotionRelegationResult(
            autoPromotedNfdTeamId = autoPromotedNfdId,
            autoRelegatedPslTeamId = autoRelegatedPslId,
            playoffWinnerTeamId = playoffWinnerId,
            playoffLosersTeamIds = playoffLosers,
            pslTop8TeamIds = top8,
            summary = summary
        )
    }
}
