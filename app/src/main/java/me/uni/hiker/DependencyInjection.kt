package me.uni.hiker

import android.content.Context
import androidx.room.Room
import me.uni.hiker.db.HikerDatabase

interface DependencyInjection {
    val db: HikerDatabase
}

class DependencyInjectionImpl (
    private val appContext: Context,
): DependencyInjection {
    override val db: HikerDatabase by lazy {
        Room.databaseBuilder(
            appContext.applicationContext,
            HikerDatabase::class.java,
            name = "hiker"
        ).fallbackToDestructiveMigration().build()
    }
}