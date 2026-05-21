package com.example.test6.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabaseConstructor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {
    @Provides
    @Singleton
    fun providesDao(@ApplicationContext context: Context): PropertyDao {
       return Room.databaseBuilder(
            context = context,
            klass = PropertyDatabase::class.java,
            name ="Property"
        ).build()
            .propertyDao()
    }
}
