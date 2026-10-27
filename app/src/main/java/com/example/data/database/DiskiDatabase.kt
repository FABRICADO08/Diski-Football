package com.example.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.data.dao.CareerSaveDao
import com.example.data.dao.MatchDao
import com.example.data.dao.PlayerDao
import com.example.data.dao.StandingDao
import com.example.data.dao.TeamDao
import com.example.data.dao.TournamentDao
import com.example.data.model.CareerSaveEntity
import com.example.data.model.MatchEntity
import com.example.data.model.PlayerEntity
import com.example.data.model.StandingEntity
import com.example.data.model.TeamEntity
import com.example.data.model.TournamentEntity

@Database(
    entities = [
        TeamEntity::class,
        PlayerEntity::class,
        MatchEntity::class,
        TournamentEntity::class,
        StandingEntity::class,
        CareerSaveEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class DiskiDatabase : RoomDatabase() {
    abstract fun teamDao(): TeamDao
    abstract fun playerDao(): PlayerDao
    abstract fun matchDao(): MatchDao
    abstract fun tournamentDao(): TournamentDao
    abstract fun standingDao(): StandingDao
    abstract fun careerSaveDao(): CareerSaveDao

    companion object {
        @Volatile
        private var INSTANCE: DiskiDatabase? = null

        fun getInstance(context: Context): DiskiDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DiskiDatabase::class.java,
                    "diski_football.db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
