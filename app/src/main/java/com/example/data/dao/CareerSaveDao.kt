package com.example.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.CareerSaveEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CareerSaveDao {
    @Query("SELECT * FROM career_saves WHERE id = 1 LIMIT 1")
    fun getActiveSaveFlow(): Flow<CareerSaveEntity?>

    @Query("SELECT * FROM career_saves WHERE id = 1 LIMIT 1")
    suspend fun getActiveSave(): CareerSaveEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSave(save: CareerSaveEntity)

    @Update
    suspend fun updateSave(save: CareerSaveEntity)

    @Query("DELETE FROM career_saves WHERE id = 1")
    suspend fun deleteSave()
}
