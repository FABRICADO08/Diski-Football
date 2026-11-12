package com.example.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.LeagueTableEntity
import com.example.data.model.LeagueType
import kotlinx.coroutines.flow.Flow

@Dao
interface LeagueTableDao {
    @Query("""
        SELECT * FROM league_tables 
        WHERE league = :league AND seasonYear = :season 
        ORDER BY points DESC, goalDifference DESC, goalsFor DESC, teamName ASC
    """)
    fun getTableFlow(league: LeagueType, season: Int = 2026): Flow<List<LeagueTableEntity>>

    @Query("""
        SELECT * FROM league_tables 
        WHERE league = 'PSL' AND seasonYear = :season 
        ORDER BY points DESC, goalDifference DESC, goalsFor DESC, teamName ASC
    """)
    fun getPslTableFlow(season: Int = 2026): Flow<List<LeagueTableEntity>>

    @Query("""
        SELECT * FROM league_tables 
        WHERE league = 'NFD' AND seasonYear = :season 
        ORDER BY points DESC, goalDifference DESC, goalsFor DESC, teamName ASC
    """)
    fun getNfdTableFlow(season: Int = 2026): Flow<List<LeagueTableEntity>>

    @Query("""
        SELECT * FROM league_tables 
        WHERE league = :league AND seasonYear = :season 
        ORDER BY points DESC, goalDifference DESC, goalsFor DESC, teamName ASC
    """)
    suspend fun getTable(league: LeagueType, season: Int = 2026): List<LeagueTableEntity>

    @Query("SELECT * FROM league_tables WHERE league = :league AND teamId = :teamId AND seasonYear = :season LIMIT 1")
    suspend fun getEntryForTeam(league: LeagueType, teamId: String, season: Int = 2026): LeagueTableEntity?

    @Query("SELECT * FROM league_tables WHERE teamId = :teamId AND seasonYear = :season LIMIT 1")
    suspend fun getEntryByTeamId(teamId: String, season: Int = 2026): LeagueTableEntity?

    @Query("SELECT COUNT(*) FROM league_tables WHERE league = :league AND seasonYear = :season")
    suspend fun getTeamCount(league: LeagueType, season: Int = 2026): Int

    @Query("SELECT COUNT(*) FROM league_tables")
    suspend fun getTotalTableCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(entries: List<LeagueTableEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(entry: LeagueTableEntity)

    @Update
    suspend fun update(entry: LeagueTableEntity)

    @Query("DELETE FROM league_tables WHERE league = :league AND seasonYear = :season")
    suspend fun deleteByLeague(league: LeagueType, season: Int = 2026)
}
