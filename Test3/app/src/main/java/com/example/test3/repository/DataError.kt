package com.example.test3.repository

sealed class DataError: Exception() {
    data object Empty: DataError()
    data object NetworkError: DataError()
}