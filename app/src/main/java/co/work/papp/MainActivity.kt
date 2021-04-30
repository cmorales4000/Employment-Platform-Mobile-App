package co.work.papp

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import co.work.PostApi
import co.work.papp.data.LoginData
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : AppCompatActivity() {

    lateinit var sharedp : SharedPreferences

    override fun onBackPressed() {
        //Do Nothing
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val aRegister = Intent(this, RegisterActivity::class.java)
        val aLobby = Intent(this, LobbyActivity::class.java)


        val sharedp = getSharedPreferences("logged", MODE_PRIVATE)
        val id = sharedp.getInt("id", 0)

        if (id != 0) {
            startActivity(aLobby)
        } else { }

            enviarBut.setOnClickListener { logindata() }
            resetBut.setOnClickListener { resetDatos() }
            registeractivity.setOnClickListener { startActivity(aRegister) }


    }


    fun logindata(){
        val campouser = userBox.text.toString()
        val campopass = passBox.text.toString()

        val sharedp = getSharedPreferences("logged", MODE_PRIVATE)
        val aLobby = Intent(this, LobbyActivity::class.java)
        val retrofitBuilder = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
                .create(PostApi::class.java)

        retrofitBuilder.loginUser(LoginData(campouser, campopass)).enqueue(
                object : Callback<Any> {
                    override fun onFailure(call: Call<Any>, t: Throwable) {
                        Log.d("loginfail", t.message.toString())
                    }
                    override fun onResponse(call: Call<Any>, response: Response<Any>) {
                        val catchToken = response.body()

                        val tokenmap = catchToken as Map<*, *>?

                        if (tokenmap == null) {
                            recreate()
                            val toast=Toast.makeText(applicationContext, "Usuario Invalido", Toast.LENGTH_LONG)
                            toast.show()


                        } else {
                            val token = catchToken!!["token"]

                            val editor = sharedp.edit()
                            editor.putString("token", token.toString())
                            editor.apply()


                            val retrofitBuilder = Retrofit.Builder()
                                    .addConverterFactory(GsonConverterFactory.create())
                                    .baseUrl(BASE_URL)
                                    .build()
                                    .create(PostApi::class.java)


                            val retrofitData = retrofitBuilder.loguser(username = campouser)

                            retrofitData.enqueue(object : Callback<List<Map<String, Any>>> {
                                override fun onResponse(call: Call<List<Map<String, Any>>>, response: Response<List<Map<String, Any>>>) {
                                    val catchid = response!!.body()!!


                                    for (i in catchid) {

                                        val id = (i["id"] as Double).toInt()

                                        val editor = sharedp.edit()

                                        editor.putInt("id", id)
                                        editor.apply()

                                        Log.d("id", id.toString())

                                    }

                                }

                                override fun onFailure(call: Call<List<Map<String, Any>>>?, t: Throwable?) {

                                }
                            })
                            startActivity(aLobby)
                        }
                    }
                }
        )
    }



    fun resetDatos(){
        userBox.text.clear()
        passBox.text.clear()
    }

}