package com.luisptapia.rftarea2modulovi.data.remote

import com.luisptapia.rftarea2modulovi.utils.Constants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitHelper {

    val interceptor = HttpLoggingInterceptor().apply {
        level =
            HttpLoggingInterceptor.Level.BODY //respuesta al nivel del body en la operación de red
    }

    val client = OkHttpClient.Builder().apply {
        addInterceptor(interceptor)

    }.build()

    fun getRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}