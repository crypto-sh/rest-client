package com.github.restclient

class WebResult(
        val title: String,
        val data : ArrayList<Product>
)

class Product(val id: Long,
              val businessType: Int,
              val distance: Int,
              val hasDistance: Boolean,
              val isBookmark: Boolean,
              val name: String,
              val subTitle: String,
              val imageUrl: String,
              val rating: Double,
              val rateCount: Int)
