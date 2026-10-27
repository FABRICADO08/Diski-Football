package com.example.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.TournamentEntity
import com.example.data.model.TournamentType
import kotlinx.coroutines.flow.Flow

@Dao
interface TournamentDao {
    @Query("SELECT * FROM tournaments ORDER BY season DESC, type ASC")
    fun getAllTournamentsFlow(): Flow<List<TournamentEntity>>

    @Query("SELECT * FROM tournaments WHERE type = :type AND season = :season LIMIT 1")
    fun getTournamentFlow(type: TournamentType, season: Int = 2026): Flow<TournamentEntity?>

    @Query("SELECT * FROM tournaments WHERE type = :type AND season = :season LIMIT 1")
    suspend fun getTournament(type: TournamentType, season: Int = 2026): TournamentEntity?

    @Query("SELECT COUNT(*) FROM tournaments")
    suspend fun getTournamentCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(tournaments: List<TournamentEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(tournament: TournamentEntity)

    @Update
    suspend fun update(tournament: TournamentEntity)
}
