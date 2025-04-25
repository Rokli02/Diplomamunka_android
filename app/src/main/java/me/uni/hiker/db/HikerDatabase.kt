package me.uni.hiker.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import me.uni.hiker.db.dao.LocalUserDAO
import me.uni.hiker.db.dao.PointDAO
import me.uni.hiker.db.dao.RecordedLocationDAO
import me.uni.hiker.db.dao.TrackDAO
import me.uni.hiker.db.entity.LocalUser
import me.uni.hiker.db.entity.Point
import me.uni.hiker.db.entity.RecordedLocation
import me.uni.hiker.db.entity.Track
import me.uni.hiker.utils.DateFormatter
import java.time.LocalDate
import java.time.LocalDateTime

@Database(
    entities = [
        LocalUser::class,
        Point::class,
        Track::class,
        RecordedLocation::class,
   ],
    version = 6,
)
@TypeConverters(DatabaseTypeConverters::class)
abstract class HikerDatabase : RoomDatabase() {
    abstract fun localUserDAO(): LocalUserDAO
    abstract fun pointDAO(): PointDAO
    abstract fun trackDAO(): TrackDAO
    abstract fun recordedLocationDAO(): RecordedLocationDAO
}

class DatabaseTypeConverters {
    @TypeConverter
    fun fromLocalDate(date: LocalDate?): String? {
        if (date == null) return null

        return DateFormatter.formatDate(date)
    }

    @TypeConverter
    fun toLocalDate(text: String?): LocalDate? {
        if (text == null) return null

        return DateFormatter.formatDate(text)
    }

    @TypeConverter
    fun fromLocalDateTime(dateTime: LocalDateTime?): String? {
        if (dateTime == null) return null

        return DateFormatter.formatTime(dateTime)
    }

    @TypeConverter
    fun toLocalDateTime(text: String?): LocalDateTime? {
        if (text == null) return null

        return DateFormatter.formatTime(text)
    }
}