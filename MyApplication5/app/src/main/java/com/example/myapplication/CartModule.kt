package com.example.myapplication

import android.content.Context
import androidx.room.Room
import androidx.room.TypeConverter
import com.example.myapplication.database.CartDao
import com.example.myapplication.database.CartDatabase
import com.example.myapplication.remote.ApiService
import com.example.myapplication.remote.CartDetails
import com.example.myapplication.remote.Product
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.internal.Contexts
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)

class CartModule {
    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://dummyjson.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService  {
        return  retrofit.create(ApiService::class.java)
    }

   @Provides
   @Singleton
   fun provideRoom(@ApplicationContext context: Context): CartDao {
       return Room.databaseBuilder(
                   context = context,
                   klass = CartDatabase::class.java,
                   name = "CartDatabase"
               )
                   .fallbackToDestructiveMigration(false)
                   .build()
           .cartDao()
   }

    class Converters {
        @TypeConverter
        fun fromList(list: List<Product>): String {
            return list.joinToString(",")
        }

        @TypeConverter
        fun toList(value: String): List<String> {
            return value.split(",")
        }
    }
}