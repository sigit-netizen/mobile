package com.gantenginapp.apps.data.repository

import com.gantenginapp.apps.data.remote.ApiService
import com.gantenginapp.apps.data.remote.dto.*

class StoreRepository(
    private val api: ApiService
) {
    suspend fun registerStore(request: RegistStoreReq) = api.registerStore(request)
}
