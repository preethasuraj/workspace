package com.example.paging3.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.paging3.paging.ItemPagingSource
import com.example.paging3.remote.Item
import com.example.paging3.remote.ItemService
import com.example.paging3.remote.NetworkModule
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ItemRepository @Inject constructor(
    val service: ItemService
) {


     fun getItems(): Flow<PagingData<Item>> {

         return Pager(
             config = PagingConfig(
                 pageSize = 20
             ),
             pagingSourceFactory = { ItemPagingSource(service) },
         ).flow
    }

}