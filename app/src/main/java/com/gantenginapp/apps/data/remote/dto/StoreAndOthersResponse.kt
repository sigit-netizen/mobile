package com.gantenginapp.apps.data.remote.dto
import com.gantenginapp.apps.domain.model.Antrian
import com.gantenginapp.apps.domain.model.Store

class StoreAndOthersResponse (
    val status: Boolean,
    val store: Store,
    val antrian: List<Antrian>
)