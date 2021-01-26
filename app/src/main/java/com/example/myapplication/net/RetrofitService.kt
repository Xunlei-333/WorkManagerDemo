package com.example.myapplication.net

import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException


object RetrofitService {

    var client: OkHttpClient.Builder = OkHttpClient.Builder().addInterceptor(BaseUrlInterceptor())
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.ixiaowai.cn/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(client.build())
        .build()

    fun createService(): RetrofitApi {
        return retrofit.create(RetrofitApi::class.java)
    }
}
class BaseUrlInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val builder = request.newBuilder()
        val headerValues = request.headers("newUrl")
        if (headerValues.size > 0) {
            builder.removeHeader("newUrl")
            val headerValue = headerValues[0]

            val newUrl = HttpUrl.parse(headerValue)

            return chain.proceed(builder.url(newUrl!!).build())
        }
        return chain.proceed(request)
    }
}
