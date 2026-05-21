package com.example.restaurantmenuapp.local

import android.content.Context
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class CategoriesRepository @Inject constructor(
    private val gson: Gson,
    private @ApplicationContext val context: Context,
) {
    private val json = context.assets.open("menu.json")
        .bufferedReader()
        .use { it.readText() }

    fun getCategories(): Result<DomainCategoryList> {
        return Result.success(
            DomainCategoryList(
            try {
                gson.fromJson(json, LocalEntity::class.java).categories
                    .map { category ->
                        DomainCategory(
                            category.id,
                            category.name,
                            category.items.map {
                                DomainItem(
                                    id = it.id,
                                    name = it.name,
                                    price = it.price,
                                    image = it.image
                                )
                            }
                        )
                    }
            } catch (e: Exception) {
                return Result.failure(e)
            }
        )
        )

    }
}

data class DomainCategoryList(
    val categories: List<DomainCategory>
)

data class DomainCategory(
    val id: String,
    val name: String,
    val items: List<DomainItem>,
)

data class DomainItem(
    val id: String,
    val name: String,
    val price: String,
    val image: String,
)