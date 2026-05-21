package com.example.test7.database

import android.content.Context
import androidx.room.Room
import androidx.room.TypeConverter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import kotlin.jvm.java

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

   @Provides
   @Singleton
   fun provideDatabase(@ApplicationContext context: Context): HotelDao {
       return Room.databaseBuilder(
           context = context,
           klass = Database::class.java,
       name = "HotelDatabase"
       )
       .fallbackToDestructiveMigration(false)
           .build()
           .hotelDao()
   }
    class Converters {
        @TypeConverter
        fun fromList(list: List<String>): String {
            return list.joinToString(",")
        }

        @TypeConverter
        fun toList(value: String): List<String> {
            return value.split(",")
        }
    }
}