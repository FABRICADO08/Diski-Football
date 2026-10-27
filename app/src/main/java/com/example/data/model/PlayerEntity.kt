package com.example.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "players",
    foreignKeys = [
        ForeignKey(
            entity = TeamEntity::class,
            parentColumns = ["id"],
            childColumns = ["teamId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["teamId"])]
)
data class PlayerEntity(
    @PrimaryKey
    val id: String,                    // e.g. "player_ronwen_williams"
    val teamId: String,                // FK to TeamEntity.id
    val name: String,                  // Full display name
    val position: PlayerPosition,      // GK, CB, CM, ST, etc.
    val jerseyNumber: Int,             // Shirt number
    val age: Int,
    val nationality: String = "South Africa",
    val overall: Int,                  // 1-99
    val pace: Int,                     // Speed / Sprinting
    val shooting: Int,                 // Shot power / Finishing
    val passing: Int,                  // Short / Long Passing
    val dribbling: Int,                // Ball control / Agility
    val defending: Int,                // Tackling / Interceptions
    val physical: Int,                 // Strength / Jumping
    val stamina: Int,                  // In-game endurance (1-99)
    val marketValue: Long,             // ZAR currency
    val weeklyWage: Long,              // ZAR
    val contractYearsRemaining: Int,   // e.g. 2
    val isCaptain: Boolean = false,
    val isStartingEleven: Boolean = false,
    val preferredFoot: String = "Right", // "Right" or "Left"
    val goalsScored: Int = 0,
    val assists: Int = 0,
    val cleanSheets: Int = 0,
    val yellowCards: Int = 0,
    val redCards: Int = 0
)
