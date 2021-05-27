package co.work.papp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.work.GetApi
import co.work.papp.data.AreaDato
import co.work.papp.data.CityDato
import co.work.papp.data.OfertaDato
import kotlinx.android.synthetic.main.activity_lobby.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val BASE_URL = "http://192.168.1.2:8000/"

class LobbyActivity : AppCompatActivity(), OnItemClickListener {


    var datos2 = arrayListOf<OfertaDato>()
    var citydatos = arrayListOf<CityDato>()
    var areadatos = arrayListOf<AreaDato>()

    override fun onBackPressed() {
        //Do Nothing
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lobby)


        val lista =findViewById<RecyclerView>(R.id.lista)
        val search = findViewById<Button>(R.id.search)
        val aProfile = Intent(this, ProfileActivity::class.java)


        perfilBut.setOnClickListener { startActivity(aProfile) }
        search.setOnClickListener { getMyDataBusqueda() }


        val primercampoarea = "Areas"
        val datanull: AreaDato = AreaDato(primercampoarea)
        areadatos.add(datanull)

        val primercampocity = "Ciudades"
        val datanull2: CityDato = CityDato(primercampocity)
        citydatos.add(datanull2)


        getMyData()
        getCity()
        getArea()

        val sharedp = getSharedPreferences("logged", MODE_PRIVATE)
        val token = sharedp.getString("token", "nope")
        Log.d("token", token.toString())




        lista.layoutManager = LinearLayoutManager(this)
        lista.adapter = RecyclerAdapter(datos2, this)



    }


    private fun getMyData() {


        val retrofitBuilder = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
                .create(GetApi::class.java)


        val retrofitData = RetrofitInstance.api.getData()

        retrofitData.enqueue(object : Callback<List<Map<String, Any>>?> {
            override fun onResponse(
                call: Call<List<Map<String, Any>>?>?,
                response: Response<List<Map<String, Any>>?>?
            ) {
                val responseBody = response!!.body()!!

                Log.d("datos", responseBody.toString())

                for (i in responseBody) {
                    val id = (i["id"] as Double).toInt()
                    val titulo = i["titulo"]
                    val contenido = i["contenido"]
                    val salario = (i["salario"] as Double).toInt()
                    val horario = i["horario"]
                    val logoempresa = i["logoempresa"]
                    val nombreempresa = i["nombreempresa"]
                    val nombrearea = i["nombrearea"]
                    val nombreciudad = i["nombreciudad"]

                    val oferta: OfertaDato = OfertaDato(
                        titulo as String,
                        salario as Int,
                        nombreciudad as String,
                        id as Int,
                        logoempresa as String,
                        nombreempresa as String,
                        nombrearea as String,
                        contenido as String,
                        horario as String
                    )
                    datos2.add(oferta)
                }


                lista.adapter = RecyclerAdapter(datos2, object : OnItemClickListener {

                    override fun OnItemClick(item: OfertaDato, position: Int) {
                        val intent = Intent(baseContext, DetalleActivity::class.java)
                        intent.putExtra("oferta", item)
                        startActivity(intent)
                    }

                })
            }

            override fun onFailure(call: Call<List<Map<String, Any>>?>?, t: Throwable?) {

            }
        })


    }


    private fun getMyDataBusqueda() {

        val retrofitBuilder = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
                .create(GetApi::class.java)


        Log.d("area", spinnerArea.selectedItemId.toString())



        val retrofitData = RetrofitInstance.api.getDataBusqueda(
            titulo = buscartitulo.text.toString(),
            area = arealogic(),
            ciudad = citylogic(),
            salario = buscarsalario.text.toString()
        )
        //area=spinnerArea.id!!.toString()

        retrofitData.enqueue(object : Callback<List<Map<String, Any>>?> {
            override fun onResponse(
                call: Call<List<Map<String, Any>>?>?,
                response: Response<List<Map<String, Any>>?>?
            ) {
                val responseBody = response!!.body()!!


                Log.d("datos", responseBody.toString())
                datos2.clear()

                for (i in responseBody) {
                    val id = (i["id"] as Double).toInt()
                    val titulo = i["titulo"]
                    val contenido = i["contenido"]
                    val salario = (i["salario"] as Double).toInt()
                    val horario = i["horario"]
                    val logoempresa = i["logoempresa"]
                    val nombreempresa = i["nombreempresa"]
                    val nombrearea = i["nombrearea"]
                    val nombreciudad = i["nombreciudad"]

                    val oferta: OfertaDato = OfertaDato(
                        titulo as String,
                        salario as Int,
                        nombreciudad as String,
                        id as Int,
                        logoempresa as String,
                        nombreempresa as String,
                        nombrearea as String,
                        contenido as String,
                        horario as String
                    )

                    datos2.add(oferta)
                }


                lista.adapter = RecyclerAdapter(datos2, object : OnItemClickListener {

                    override fun OnItemClick(item: OfertaDato, position: Int) {
                        val intent = Intent(baseContext, DetalleActivity::class.java)
                        intent.putExtra("oferta", item)
                        startActivity(intent)

                    }

                })
            }

            override fun onFailure(call: Call<List<Map<String, Any>>?>?, t: Throwable?) {

            }
        })


    }




    private fun arealogic(): String {
        var selected = spinnerArea.selectedItemId.toString()
        var arealogic = ""

        if ( selected == "0") {

        } else {
            arealogic = selected
        }

        return arealogic
    }

    private fun citylogic(): String {
        var selected = spinnerCity.selectedItemId.toString()
        var citylogic = ""

        if ( selected == "0") {

        } else {
            citylogic = selected
        }

        return citylogic
    }



    private fun getCity() {
        val retrofitBuilder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(GetApi::class.java)

        val retrofitData = retrofitBuilder.getCity()

        retrofitData.enqueue(object : Callback<List<Map<String, Any>>?> {
            override fun onResponse(
                call: Call<List<Map<String, Any>>?>,
                response: Response<List<Map<String, Any>>?>
            ) {
                val responseBody = response!!.body()!!

                Log.d("city", responseBody.toString())

                for (i in responseBody) {

                    val nombre = i["nombre"]

                    val dataCity: CityDato = CityDato(nombre as String)

                    citydatos.add(dataCity)

                }

                val spinner = findViewById<Spinner>(R.id.spinnerCity)
                val adaptador = ArrayAdapter(
                    applicationContext,
                    android.R.layout.simple_spinner_item,
                    citydatos
                )
                spinner.adapter = adaptador


            }

            override fun onFailure(call: Call<List<Map<String, Any>>?>, t: Throwable) {
            }


        })


    }

    private fun getArea() {
        val retrofitBuilder = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
                .create(GetApi::class.java)

        val retrofitData = retrofitBuilder.getArea()

        retrofitData.enqueue(object : Callback<List<Map<String, Any>>?> {
            override fun onResponse(
                call: Call<List<Map<String, Any>>?>,
                response: Response<List<Map<String, Any>>?>
            ) {
                val responseBody = response!!.body()!!

                Log.d("area", responseBody.toString())



                for (i in responseBody) {
                    val nombre = i["nombre"]

                    val dataArea: AreaDato = AreaDato(nombre as String)


                    areadatos.add(dataArea)


                }

                val spinner2 = findViewById<Spinner>(R.id.spinnerArea)
                val adaptador2 = ArrayAdapter(
                    applicationContext,
                    android.R.layout.simple_spinner_item,
                    areadatos
                )
                spinner2.adapter = adaptador2

            }

            override fun onFailure(call: Call<List<Map<String, Any>>?>, t: Throwable) {
            }


        })

    }





    override fun OnItemClick(item: OfertaDato, position: Int) {
        val intent = Intent(this, DetalleActivity::class.java)
        intent.putExtra("oferta", item)

        startActivity(intent)
    }

}

