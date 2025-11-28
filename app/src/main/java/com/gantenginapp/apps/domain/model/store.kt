package com.gantenginapp.apps.domain.model
import com.google.gson.annotations.SerializedName

data class Store(
    @SerializedName("id_store")
    val idStore: Int = 0,

    @SerializedName("id_user")
    val idUser: Int = 0,

    @SerializedName("store_name")
    val storeName: String = "",

    val price: Int = 0,

    val alamat: String = "",

    @SerializedName("opening_hours")
    val openingHours: String = "",

    @SerializedName("closing_time")
    val closingTime: String = "",

    @SerializedName("total_antrian")
    val totalAntrian: Int = 0,

    val status: Int = 0
)