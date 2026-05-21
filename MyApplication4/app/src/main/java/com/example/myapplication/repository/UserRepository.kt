package com.example.myapplication.repository

import com.example.myapplication.network.ApiService
import java.util.concurrent.CancellationException

class UserRepository(
    val apiService: ApiService
) {
    suspend fun getUsers(): Result<List<DomainUser>> {
        try {
            val result = apiService.getUsers(100)
            return Result.success(
                result.users.map {
                    DomainUser(
                        id = it.id,
                        first = it.first,
                        last = it.last,
                    )
                }
            )
        } catch (e: Exception) {
            if(e == CancellationException()) {
                throw e
            } else {
                return Result.failure(e)
            }
        }
    }
}

data class DomainUser(
    val id: Int,
    val first:String,
    val last:String,
)