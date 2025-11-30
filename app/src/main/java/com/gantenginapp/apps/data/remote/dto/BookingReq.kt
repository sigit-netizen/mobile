package com.gantenginapp.apps.data.remote.dto

data class BookingReq(
    val idStore: Int,
    val idUser : String,
    val customerName: String,
    val noHp: String,
    val waktu: String
)


