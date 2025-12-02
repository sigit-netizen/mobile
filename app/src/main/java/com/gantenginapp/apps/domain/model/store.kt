package com.gantenginapp.apps.domain.model
import com.google.gson.annotations.SerializedName

data class Store(
    @SerializedName("id_store")
    val idStore: Int? = null,

    @SerializedName("id_user")
    val idUser: Int? = null,

    @SerializedName("store_name")
    val storeName: String? = null,

    val price: Int? = null,

    val alamat: String? = null,

    @SerializedName("opening_hours")
    val openingHours: String? = null,

    @SerializedName("closing_time")
    val closingTime: String? = null,

    @SerializedName("total_antrian")
    val totalAntrian: Int? = null,

    val durasi : Int? = null,

    val status: Int? = null,

    val imageUrl: String? = null
)