package com.example.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.StandingEntity
import com.example.data.model.TournamentType
import kotlinx.coroutines.flow.Flow

@Dao
interface StandingDao {
    @Query("""
        SELECT * FROM standings 
        WHERE tournamentType = :tournamentType AND season = :season 
        ORDER BY points DESC, goalDifference DESC, goalsFor DESC, teamId ASC
    """)
    fun getStandingsFlow(tournamentType: TournamentType, season: Int = 2026): Flow<List<StandingEntity>>

    @Query("""
        SELECT * FROM standings 
        WHERE tournamentType = :tournamentType AND season = :season 
        ORDER BY points DESC, goalDifference DESC, goalsFor DESC, teamId ASC
    """)
    suspend fun getStandings(tournamentType: TournamentType, season: Int = 2026): List<StandingEntity>

    @Query("SELECT * FROM standings WHERE tournamentType = :tournamentType AND season = :season AND teamId = :teamId LIMIT 1")
    suspend fun getStandingForTeam(tournamentType: TournamentType, teamId: String, season: Int = 2026): StandingEntity?

    @Query("SELECT COUNT(*) FROM standings")
    suspend fun getStandingCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(standings: List<StandingEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(standing: StandingEntity)

    @Update
    suspend fun update(standing: StandingEntity)
}
