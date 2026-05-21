package com.example.dogbreed.remote

data class RemoteEntity (
    val message: Map<String, List<String>>
)

data class BreedDetailsRemote(
    val message: List<String>
)
