package com.gantenginapp.apps.data.repository

import com.gantenginapp.apps.data.remote.ApiService
import com.gantenginapp.apps.data.remote.dto.*
import retrofit2.Response
class StoreRepository(
    private val api: ApiService
) {
    suspend fun getAllStores() : StoresResponse {
        return api.getAllStore()
    }

    suspend fun getStoreAndOthers(storeId: Int)  : StoreAndOthersResponse {
        return api.getStoreAndOtherById(storeId)
    }

    suspend fun getStoreAdmin(userId: Int) : Response<StoreAndOthersResponse> {
        return api.getAdminStore(userId)
    }

    suspend fun ngantri(request: BookingReq) : Response<ApiResponse> {
        return api.ngantri(request)
    }
    suspend fun registerStore(request: RegistStoreReq) = api.registerStore(request)

    suspend fun updateStore(storeId: Int, request: StoreUpdateRequest): Response<ApiResponse> {
        return api.updateStore(storeId, request)
    }

    suspend fun generate(
        storeId: Int,
        request: GenerateSlotRequest
    ): Response<ApiResponse> {
        return api.generate(storeId, request)
    }

    suspend fun deleteAntrian (idAntrian: Int) = api.deleteAntrian(idAntrian)

    suspend fun batalAntrian (idAntrian: Int) = api.batalAntrian(idAntrian)



}
