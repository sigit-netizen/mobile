package com.gantenginapp.apps.data.remote.dto

data class StoreUpdateRequest(
    val storeName: String,
    val price: Int,
    val alamat: String,
    val openingHours: String,
    val closingTime: String,
    val durasi: Int
)
