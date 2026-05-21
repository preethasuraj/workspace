package com.example.myapplication.ui

import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
     savedStateHandle: SavedStateHandle,
) : ViewModel() {
    val id  = savedStateHandle.get<String>("orderId")


}