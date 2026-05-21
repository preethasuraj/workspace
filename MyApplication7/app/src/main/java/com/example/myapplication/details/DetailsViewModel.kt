package com.example.myapplication.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

class DetailsViewModel(
    val savedStateHandle: SavedStateHandle
) : ViewModel() {
    val id = savedStateHandle.get<Int>("id")
    init {

    }
}