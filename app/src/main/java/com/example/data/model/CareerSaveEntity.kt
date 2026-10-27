package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "career_saves")
data class CareerSaveEntity(
    @PrimaryKey
    val id: Int = 1,
    val managerName: String,
    val selectedTeamId: String,
    val currentSeason: Int = 2026,
    val currentGameWeek: Int = 1,
    val managerReputation: Int = 50,   // 0 to 100
    val transferBudgetRemaining: Long, // ZAR Rands
    val difficulty: String = "Medium", // Easy, Medium, Hard, Legendary
    val formation: String = "4-3-3",
    val teamMorale: Int = 80,
    val isJobSecure: Boolean = true,
    val lastSavedTimestamp: Long = System.currentTimeMillis()
)
