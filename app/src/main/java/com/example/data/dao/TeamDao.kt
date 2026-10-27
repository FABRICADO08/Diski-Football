package com.example.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.LeagueType
import com.example.data.model.TeamEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TeamDao {
    @Query("SELECT * FROM teams ORDER BY ratingOverall DESC")
    fun getAllTeamsFlow(): Flow<List<TeamEntity>>

    @Query("SELECT * FROM teams WHERE league = :league ORDER BY ratingOverall DESC")
    fun getTeamsByLeagueFlow(league: LeagueType): Flow<List<TeamEntity>>

    @Query("SELECT * FROM teams WHERE league = :league ORDER BY ratingOverall DESC")
    suspend fun getTeamsByLeague(league: LeagueType): List<TeamEntity>

    @Query("SELECT * FROM teams WHERE id = :teamId LIMIT 1")
    fun getTeamByIdFlow(teamId: String): Flow<TeamEntity?>

    @Query("SELECT * FROM teams WHERE id = :teamId LIMIT 1")
    suspend fun getTeamById(teamId: String): TeamEntity?

    @Query("SELECT COUNT(*) FROM teams")
    suspend fun getTeamCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(teams: List<TeamEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(team: TeamEntity)

    @Update
    suspend fun updateTeam(team: TeamEntity)
}
