package com.example.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.PlayerEntity
import com.example.data.model.PlayerPosition
import kotlinx.coroutines.flow.Flow

@Dao
interface PlayerDao {
    @Query("SELECT * FROM players WHERE teamId = :teamId ORDER BY isStartingEleven DESC, overall DESC")
    fun getPlayersByTeamFlow(teamId: String): Flow<List<PlayerEntity>>

    @Query("SELECT * FROM players WHERE teamId = :teamId ORDER BY isStartingEleven DESC, overall DESC")
    suspend fun getPlayersByTeam(teamId: String): List<PlayerEntity>

    @Query("SELECT * FROM players WHERE id = :playerId LIMIT 1")
    fun getPlayerByIdFlow(playerId: String): Flow<PlayerEntity?>

    @Query("SELECT * FROM players WHERE id = :playerId LIMIT 1")
    suspend fun getPlayerById(playerId: String): PlayerEntity?

    @Query("SELECT * FROM players WHERE position = :position ORDER BY overall DESC LIMIT :limit")
    fun getTopPlayersByPosition(position: PlayerPosition, limit: Int = 10): Flow<List<PlayerEntity>>

    @Query("SELECT * FROM players ORDER BY goalsScored DESC LIMIT 10")
    fun getTopScorers(): Flow<List<PlayerEntity>>

    @Query("SELECT COUNT(*) FROM players")
    suspend fun getPlayerCount(): Int

    @Query("SELECT * FROM players WHERE teamId != :excludeTeamId ORDER BY overall DESC LIMIT 60")
    fun getTransferMarketPlayers(excludeTeamId: String): Flow<List<PlayerEntity>>

    @Query("SELECT * FROM players ORDER BY overall DESC LIMIT 60")
    fun getAllMarketPlayers(): Flow<List<PlayerEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(players: List<PlayerEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(player: PlayerEntity)

    @Update
    suspend fun updatePlayer(player: PlayerEntity)
}
