package me.uni.hiker

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import me.uni.hiker.api.service.CommonService
import me.uni.hiker.api.service.TrackService
import me.uni.hiker.api.service.UserService
import me.uni.hiker.db.HikerDatabase
import me.uni.hiker.db.dao.LocalUserDAO
import me.uni.hiker.db.dao.PointDAO
import me.uni.hiker.db.dao.RecordedLocationDAO
import me.uni.hiker.db.dao.TrackDAO
import me.uni.hiker.ui.screen.auth.login.LoginUseCases
import me.uni.hiker.utils.encrypter.Hasher
import me.uni.hiker.utils.encrypter.PBKDF2Hasher
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DependencyInjection {
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
    @Singleton
    fun provideLocalUserDAO(db: HikerDatabase): LocalUserDAO {
        return db.localUserDAO()
    }

    @Provides
    @Singleton
    fun providePointDAO(db: HikerDatabase): PointDAO {
        return db.pointDAO()
    }

    @Provides
    @Singleton
    fun provideTrackDAO(db: HikerDatabase): TrackDAO {
        return db.trackDAO()
    }

    @Provides
    @Singleton
    fun provideRecordedLocationDAO(db: HikerDatabase): RecordedLocationDAO {
        return db.recordedLocationDAO()
    }

    @Provides
    @Singleton
    fun provideHasher(): Hasher {
        return PBKDF2Hasher()
    }

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl("http://192.168.0.104:3000/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    fun provideUserService(retrofit: Retrofit): UserService = retrofit.create(UserService::class.java)

    @Provides
    @Singleton
    fun provideCommonService(retrofit: Retrofit): CommonService = retrofit.create(CommonService::class.java)

    @Provides
    @Singleton
    fun provideTrackService(retrofit: Retrofit): TrackService = retrofit.create(TrackService::class.java)

    @Provides
    @Singleton
    fun provideLoginUseCases(
        userDAO: LocalUserDAO,
        userService: UserService,
        hasher: Hasher,
    ): LoginUseCases {
        return LoginUseCases(userDAO, userService, hasher)
    }
}