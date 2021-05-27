package co.work.papp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import co.work.PostApi
import co.work.papp.data.ApplyData
import co.work.papp.data.OfertaDato
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_detalle.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DetalleActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle)

        val sharedp = getSharedPreferences("logged", MODE_PRIVATE)
        val id = sharedp.getInt("id", 0)


        Log.d("aplicante", id.toString())

        val oferta1: OfertaDato = getIntent().getSerializableExtra("oferta") as OfertaDato

        val titulo = findViewById<TextView>(R.id.titulo_detalle)
        val salario = findViewById<TextView>(R.id.salario_detalle)
        val ciudad = findViewById<TextView>(R.id.ciudad_detalle)
        val contenido = findViewById<TextView>(R.id.contenido_detalle)
        val horario = findViewById<TextView>(R.id.horario_detalle)
        var imagen_detalle = findViewById<ImageView>(R.id.imagen_detalle)
        var imagebox =findViewById<ImageView>(R.id.profileimage)
        val empresa = findViewById<TextView>(R.id.empresa_detalle)
        val upload = findViewById<Button>(R.id.uploadimage)


        val url = "http://192.168.1.2:8000"
        val logodeempresa = url.plus(oferta1.logoempresa)
        Log.d("logo", logodeempresa)

        titulo.text = oferta1.titulo
        salario.text = "Salario:              " + oferta1.salario
        ciudad.text = "Ciudad:      " + oferta1.ciudad.toString()
        horario.text = "Horario:      " + oferta1.horario.toString()
        contenido.text = oferta1.contenido.toString()
        empresa.text = oferta1.nombreempresa.toString()
        //image.setImageResource(oferta1.logoempresa)
        //Picasso.with(this).load(oferta1.logoempresa).into(image);
        Glide.with(getApplicationContext()).load(logodeempresa).into(imagen_detalle)


        apply.setOnClickListener {

            val retrofitBuilder = Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(BASE_URL)
                    .build()
                    .create(PostApi::class.java)

            Log.d("idoferta", oferta1.id.toString())

            val applydata: ApplyData = ApplyData(oferta1.id, id)



            retrofitBuilder.applyoffer(applydata).enqueue(
                    object : Callback<Any> {
                        override fun onFailure(call: Call<Any>, t: Throwable) {
                            Log.d("failapply", t.message.toString())
                        }

                        override fun onResponse(call: Call<Any>, response: Response<Any>) {
                            val applieduser = response.body()

                            Log.d("applypost", applieduser.toString())

                            if (applieduser == null) {

                                Toast.makeText(applicationContext, "Ya has aplicado a esta oferta", Toast.LENGTH_LONG).show()

                                apply.visibility= View.GONE

                            } else {
                                Toast.makeText(applicationContext, "Aplicación Exitosa", Toast.LENGTH_LONG).show()


                            }

                        }
                    }

            )

        }


    }


}