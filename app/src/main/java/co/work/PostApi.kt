package co.work


import co.work.papp.data.ApplyData
import co.work.papp.data.LoginData
import co.work.papp.data.PostData
import co.work.papp.data.UserData
import retrofit2.Call
import retrofit2.http.*

interface PostApi {

    @Headers("Content-Type: application/json")
    @POST("api/usuarios/")
    fun createUser(
            @Body Data1: PostData
    ): Call<Any>


    @Headers("Content-Type: application/json")
    @POST("api/aplicantes/")
    fun applyoffer(
            @Body Data2: ApplyData
    ): Call<Any>


    @Headers("Content-Type: application/json")
    @POST("api-token-auth/")
    fun loginUser(
            @Body login: LoginData
    ): Call<Any>

    @GET("api/loguser/")
    fun loguser(@Query("username") username: String?): Call<List<Map<String, Any>>>


    @Headers("Content-Type: application/json")
    @PUT("api/usuarios/{id}/")
    fun update(@Path("id") id: Int, @Body Data3: UserData): Call<Map<String, Any>>

    @GET("api/usuarios/{id}/")
    fun getprofile(@Path("id") id: Int): Call<Map<String, Any>>






}