package com.example.dogbreed.remote

data class RemoteEntity(
    val message: Map<String, List<String>>
)

data class RemoteResponse(
    val message: RemoteEntity
)

data class BreedDetails(
    val message: List<String>
)