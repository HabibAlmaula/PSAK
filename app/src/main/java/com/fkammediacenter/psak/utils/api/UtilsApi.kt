package com.fkammediacenter.psak.utils.api

object UtilsApi {

    // 10.0.2.2 ini adalah localhost.
    val BASE_URL_API = "http://192.168.100.58/psak/"
    //makamhaji 192.168.100.58
    //ponsel 192.168.43.100

    // Mendeklarasikan Interface BaseApiService
    fun getAPIService(): BaseApiService {
        return RetrofitClient.getClient(BASE_URL_API).create(BaseApiService::class.java)
    }
}
