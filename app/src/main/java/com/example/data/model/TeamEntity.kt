package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "teams")
data class TeamEntity(
    @PrimaryKey
    val id: String,                    // e.g. "mamelodi_sundowns"
    val name: String,                  // e.g. "Mamelodi Sundowns"
    val shortName: String,             // e.g. "Sundowns"
    val abbreviation: String,          // e.g. "MSFC"
    val nickname: String,              // e.g. "The Brazilians" / "Ka Bo Yellow"
    val league: LeagueType,            // PSL or NFD
    val stadiumName: String,           // e.g. "Loftus Versfeld Stadium"
    val stadiumCapacity: Int,          // e.g. 51762
    val city: String,                  // e.g. "Pretoria"
    val province: String,              // e.g. "Gauteng"
    val primaryColorHex: String,       // e.g. "#FFD700" (Gold/Yellow)
    val secondaryColorHex: String,     // e.g. "#0033A0" (Royal Blue)
    val kitHomeJerseyHex: String,      // Primary jersey color
    val kitHomeShortsHex: String,      // Primary shorts color
    val kitAwayJerseyHex: String,      // Away jersey color
    val kitAwayShortsHex: String,      // Away shorts color
    val ratingOverall: Int,            // 1-99
    val ratingAttack: Int,             // 1-99
    val ratingMidfield: Int,           // 1-99
    val ratingDefense: Int,            // 1-99
    val transferBudget: Long,          // ZAR currency
    val wageBudget: Long,              // ZAR weekly
    val prestige: Int,                 // 1-5 stars
    val isPlayable: Boolean = true
)
