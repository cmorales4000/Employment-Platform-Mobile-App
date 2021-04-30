package co.work.papp

import co.work.GetApi
import co.work.PostApi
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create


object RetrofitInstance {


    private val client = OkHttpClient.Builder().apply {
        addInterceptor(MyInterceptor())
    }.build()

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: GetApi by lazy {
        retrofit.create(GetApi::class.java)
    }

    val apip: PostApi by lazy {
        retrofit.create(PostApi::class.java)
    }

}