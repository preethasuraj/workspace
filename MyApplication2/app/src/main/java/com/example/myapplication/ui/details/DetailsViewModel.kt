package com.example.myapplication.ui.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class DetailsViewModel @Inject constructor(
    val savedStateHandle: SavedStateHandle
): ViewModel() {
    val id = savedStateHandle.get<String>("id")
   init {
       android.util.Log.i("Preetha", "${id}")
   }
}