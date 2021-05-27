package co.work

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface MyAPI {


    @Multipart
    @PATCH("api/usuarios/{id}/")
    fun uploadImage(
            @Path("id") id: Int,
            @Part image: MultipartBody.Part,
            @Part("desc") desc: RequestBody
    ): Call<Any>

    @Multipart
    @PATCH("api/usuarios/{id}/")
    fun uploadFile(
            @Path("id") id: Int,
            @Part hdv: MultipartBody.Part,
            @Part("desc") desc: RequestBody
    ): Call<Any>



    companion object{
        operator fun invoke(): MyAPI {
            return Retrofit.Builder()
                    .baseUrl("http://192.168.1.2:8000/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(MyAPI::class.java)
        }
    }


}