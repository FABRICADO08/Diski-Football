package com.example.data.database

import androidx.room.TypeConverter
import com.example.data.model.LeagueType
import com.example.data.model.MatchStatus
import com.example.data.model.PlayerPosition
import com.example.data.model.TournamentType

class Converters {
    @TypeConverter
    fun fromLeagueType(value: LeagueType?): String? = value?.name

    @TypeConverter
    fun toLeagueType(value: String?): LeagueType? = value?.let {
        try { LeagueType.valueOf(it) } catch (e: Exception) { LeagueType.PSL }
    }

    @TypeConverter
    fun fromTournamentType(value: TournamentType?): String? = value?.name

    @TypeConverter
    fun toTournamentType(value: String?): TournamentType? = value?.let {
        try { TournamentType.valueOf(it) } catch (e: Exception) { TournamentType.PSL }
    }

    @TypeConverter
    fun fromPlayerPosition(value: PlayerPosition?): String? = value?.name

    @TypeConverter
    fun toPlayerPosition(value: String?): PlayerPosition? = value?.let {
        try { PlayerPosition.valueOf(it) } catch (e: Exception) { PlayerPosition.CM }
    }

    @TypeConverter
    fun fromMatchStatus(value: MatchStatus?): String? = value?.name

    @TypeConverter
    fun toMatchStatus(value: String?): MatchStatus? = value?.let {
        try { MatchStatus.valueOf(it) } catch (e: Exception) { MatchStatus.SCHEDULED }
    }
}
