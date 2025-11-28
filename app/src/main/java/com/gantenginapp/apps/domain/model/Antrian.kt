package com.gantenginapp.apps.domain.model
import com.google.gson.annotations.SerializedName


data class Antrian(
    @SerializedName("id_antrian")
    val idAntrian: Int = 0,

    @SerializedName("id_store")
    val idStore: Int = 0,

    @SerializedName("customer_name")
    val customerName: String = "",

    val noHp: String = "",

    val waktu: String = "",

    val status: Int = 0
)