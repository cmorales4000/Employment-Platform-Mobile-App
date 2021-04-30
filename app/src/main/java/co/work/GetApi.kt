package co.work


import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*


interface GetApi {



    //@Headers("Authorization: Token 9a3c2c370aeef82a9b68c1b6558a982ef39a226a")
    @GET("api/oferta/")
    fun getData(): Call<List<Map<String, Any>>>



    //?titulo={titulo}&area={area}&ciudad={ciudad}&salario={titulo}"
    @GET("api/oferta/")
    fun getDataBusqueda(@Query("titulo") titulo: String?, @Query("area") area: String?, @Query("ciudad") ciudad: String?, @Query("salario") salario: String?): Call<List<Map<String, Any>>>


    @GET("api/area/")
    fun getArea(): Call<List<Map<String, Any>>>

    @GET("api/ciudad/")
    fun getCity(): Call<List<Map<String, Any>>>

}