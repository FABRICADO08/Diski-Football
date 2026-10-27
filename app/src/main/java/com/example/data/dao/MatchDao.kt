package com.example.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.MatchEntity
import com.example.data.model.MatchStatus
import com.example.data.model.TournamentType
import kotlinx.coroutines.flow.Flow

@Dao
interface MatchDao {
    @Query("SELECT * FROM matches WHERE tournamentType = :type AND seasonYear = :season ORDER BY gameWeek ASC, id ASC")
    fun getMatchesByTournamentFlow(type: TournamentType, season: Int = 2026): Flow<List<MatchEntity>>

    @Query("SELECT * FROM matches WHERE tournamentType = :type AND gameWeek = :gameWeek AND seasonYear = :season")
    fun getMatchesByGameWeekFlow(type: TournamentType, gameWeek: Int, season: Int = 2026): Flow<List<MatchEntity>>

    @Query("SELECT * FROM matches WHERE (homeTeamId = :teamId OR awayTeamId = :teamId) ORDER BY gameWeek ASC")
    fun getTeamMatchesFlow(teamId: String): Flow<List<MatchEntity>>

    @Query("SELECT * FROM matches WHERE (homeTeamId = :teamId OR awayTeamId = :teamId) AND status = :status ORDER BY gameWeek ASC LIMIT 1")
    suspend fun getNextMatchForTeam(teamId: String, status: MatchStatus = MatchStatus.SCHEDULED): MatchEntity?

    @Query("SELECT * FROM matches WHERE id = :matchId LIMIT 1")
    suspend fun getMatchById(matchId: String): MatchEntity?

    @Query("SELECT COUNT(*) FROM matches")
    suspend fun getMatchCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(matches: List<MatchEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(match: MatchEntity)

    @Update
    suspend fun updateMatch(match: MatchEntity)
}
