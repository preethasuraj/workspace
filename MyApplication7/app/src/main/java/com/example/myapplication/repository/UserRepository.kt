package com.example.myapplication.repository

import com.example.myapplication.remote.RemoteApi
import com.example.myapplication.remote.User
import java.util.concurrent.CancellationException

interface Repository{
    suspend fun getUsers(): Result<List<User>>
}
class UserRepository(
    val api: RemoteApi,
): Repository {
    override suspend fun getUsers(): Result<List<User>>{

        return try {
            Result.success(api.getUsers().users)
        } catch (e: Exception) {
            if(e is CancellationException) {
                throw e
            } else {
                Result.failure(e)
            }
        }
    }
}