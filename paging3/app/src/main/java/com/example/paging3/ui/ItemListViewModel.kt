package com.example.paging3.ui

import android.media.Rating
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.paging3.repository.ItemRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import javax.inject.Inject


@HiltViewModel
class ItemListViewModel @Inject constructor(
    private val repository: ItemRepository
)
    : ViewModel(){
    
    val items = repository.getItems()
        .map{ pagingData -> pagingData.map { item ->
            UiEntity(
                id = item.id,
                title = item.title,
                description = item.description,
                category = item.category,
                rating = "${item.rating} *"
            )
        }

        }
        .cachedIn(viewModelScope)
}

data class UiEntity(
    val id: Int,
    val title : String,
    val description: String,
    val category: String,
    val rating: String
)