package com.example.data.repository

import com.example.data.database.DiskiDatabase
import com.example.data.model.CareerSaveEntity
import com.example.data.model.LeagueType
import com.example.data.model.MatchEntity
import com.example.data.model.PlayerEntity
import com.example.data.model.StandingEntity
import com.example.data.model.TeamEntity
import com.example.data.model.TournamentEntity
import com.example.data.model.TournamentType
import com.example.data.seed.DatabaseInitializer
import kotlinx.coroutines.flow.Flow

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

    // Players
    fun getSquadForTeam(teamId: String): Flow<List<PlayerEntity>> = database.playerDao().getPlayersByTeamFlow(teamId)

    fun getPlayerById(playerId: String): Flow<PlayerEntity?> = database.playerDao().getPlayerByIdFlow(playerId)

    // Tournaments
    fun getAllTournaments(): Flow<List<TournamentEntity>> = database.tournamentDao().getAllTournamentsFlow()

    fun getTournament(type: TournamentType): Flow<TournamentEntity?> = database.tournamentDao().getTournamentFlow(type)

    // Standings
    fun getStandings(type: TournamentType): Flow<List<StandingEntity>> = database.standingDao().getStandingsFlow(type)

    // Matches
    fun getMatches(type: TournamentType): Flow<List<MatchEntity>> = database.matchDao().getMatchesByTournamentFlow(type)

    // Career
    fun getActiveCareerSave(): Flow<CareerSaveEntity?> = database.careerSaveDao().getActiveSaveFlow()

    suspend fun saveCareer(save: CareerSaveEntity) {
        database.careerSaveDao().insertSave(save)
    }
}
