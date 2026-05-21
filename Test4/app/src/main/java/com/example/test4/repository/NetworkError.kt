package com.example.test4.repository

sealed class NetworkError: Exception() {
    data object EmptyResponse: NetworkError()
    data object FetchError: NetworkError()
}