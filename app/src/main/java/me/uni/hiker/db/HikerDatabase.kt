package me.uni.hiker.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import me.uni.hiker.db.dao.LocalUserDAO
import me.uni.hiker.db.entity.LocalUser
import me.uni.hiker.db.entity.Point
import me.uni.hiker.db.entity.Track
import me.uni.hiker.utils.DateFormatter
import java.time.LocalDate
import java.time.LocalDateTime

@Database(
    entities = [
        LocalUser::class,
        Point::class,
        Track::class,
   ],
    version = 1,
)
@TypeConverters(DatabaseTypeConverters::class)
abstract class HikerDatabase : RoomDatabase() {
    abstract fun localUserDAO(): LocalUserDAO
}

class DatabaseTypeConverters {
    @TypeConverter
    fun fromLocalDate(date: LocalDate?): String? {
        if (date == null) return null

        return DateFormatter.format(date)
    }

    @TypeConverter
    fun toLocalDate(text: String?): LocalDate? {
        if (text == null) return null

        return try {
            DateFormatter.format(text)
        } catch (nfe: NumberFormatException) {
            println("Couldn't convert date to Local Date when taken out of DB!")

            LocalDate.now()
        }
    }

    @TypeConverter
    fun fromLocalDateTime(dateTime: LocalDateTime?): String? {
        if (dateTime == null) return null

        return DateFormatter.formatTime(dateTime)
    }

    @TypeConverter
    fun toLocalDateTime(text: String?): LocalDateTime? {
        if (text == null) return null

        return try {
            DateFormatter.formatTime(text)
        } catch (nfe: RuntimeException) {
            println("Couldn't convert date to Local Date when taken out of DB!")

            LocalDateTime.now()
        }
    }
}