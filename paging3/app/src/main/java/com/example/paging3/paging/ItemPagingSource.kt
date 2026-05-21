package com.example.paging3.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.paging3.remote.Item
import com.example.paging3.remote.ItemService

class ItemPagingSource(
    private val apiService: ItemService
) : PagingSource<Int, Item>() {
    override fun getRefreshKey(state: PagingState<Int, Item>): Int? {
        return ((state.anchorPosition ?: 0) - state.config.initialLoadSize / 2)
            .coerceAtLeast(0)
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Item> {
        return try {
            val data = apiService.getItems(params.key ?: 0, params.loadSize)

            val currentPage = params.key ?: 0
            LoadResult.Page(
                data = data.items,
                prevKey = if (currentPage == 0) null else currentPage - 1,
                nextKey = if (data.hasNext) currentPage + 1 else null
            )
        } catch (e: Exception) {
            LoadResult.Error(
                e
            )
        }
    }

}