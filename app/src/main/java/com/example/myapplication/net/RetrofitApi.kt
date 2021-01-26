package com.example.myapplication.net

import com.example.myapplication.bean.ApiBean
import okhttp3.Response
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Path

interface RetrofitApi {
    @GET("api/api.php?return=json")
    suspend fun getJson(): ApiBean

    @GET("dismiss_the_value")
    suspend fun getImage(@Header("newUrl") url: String): ResponseBody
}