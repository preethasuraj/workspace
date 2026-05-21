package com.example.hotels2.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataBaseModule {

    @Provides
    @Singleton
    fun provideRoom(@ApplicationContext context: Context): HotelDatabase {
        return Room.databaseBuilder(
                    context = context,
                    klass = HotelDatabase::class.java,
                    name = "hotel database"
                )
                    .fallbackToDestructiveMigration(false)
                    .build()
    }

    @Provides
    @Singleton
    fun provideHotelDao(room: HotelDatabase): HotelDao {
        return room.hotelDao()
    }
}
