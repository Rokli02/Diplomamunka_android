package me.uni.hiker

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import me.uni.hiker.db.HikerDatabase
import me.uni.hiker.db.dao.LocalUserDAO
import me.uni.hiker.db.dao.PointDAO
import me.uni.hiker.db.dao.RecordedLocationDAO
import me.uni.hiker.db.dao.TrackDAO
import me.uni.hiker.utils.encrypter.Hasher
import me.uni.hiker.utils.encrypter.PBKDF2Hasher
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DependencyInjection () {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): HikerDatabase {
        return Room.databaseBuilder(
            context,
            HikerDatabase::class.java,
            name = "hiker"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    fun provideLocalUserDAO(db: HikerDatabase): LocalUserDAO {
        return db.localUserDAO()
    }

    @Provides
    fun providePointDAO(db: HikerDatabase): PointDAO {
        return db.pointDAO()
    }

    @Provides
    fun provideTrackDAO(db: HikerDatabase): TrackDAO {
        return db.trackDAO()
    }

    @Provides
    fun provideRecordedLocationDAO(db: HikerDatabase): RecordedLocationDAO {
        return db.recordedLocationDAO()
    }

    @Provides
    fun provideHasher(): Hasher {
        return PBKDF2Hasher()
    }
}