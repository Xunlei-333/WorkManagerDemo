package com.example.myapplication.net

class NetRepository private constructor() {

    private val retrofitApi by lazy {
        RetrofitService.createService()
    }

    fun getApi(): RetrofitApi {
        return retrofitApi
    }

    companion object {
        @Volatile
        private var ins: NetRepository? = null

        fun getInstance(): NetRepository {
            return ins ?: synchronized(this) {
                ins ?: NetRepository().also {
                    ins = it
                }
            }
        }
    }
}