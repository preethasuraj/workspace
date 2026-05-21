package com.example.hitels5.database

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class LocalModule {
    @Provides
    @Singleton
    fun provideDao(@ApplicationContext context: Context): HotelDao {
        return Room.databaseBuilder(
            context = context,
            klass = HotelDatabase::class.java,
            name = "Hotel Database"
        )
            .fallbackToDestructiveMigration(false)
            .build()
            .getDao()
    }
}