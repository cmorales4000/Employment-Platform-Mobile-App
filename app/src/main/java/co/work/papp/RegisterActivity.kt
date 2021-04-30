package co.work.papp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import co.work.PostApi
import co.work.papp.data.PostData
import kotlinx.android.synthetic.main.activity_register.*
import retrofit2.Retrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.converter.gson.GsonConverterFactory


class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)


        registerBut.setOnClickListener {

            val usernamedata = username.text.toString().trim()
            val namedata = name.text.toString().trim()
            val dnidata = dni.text.toString().trim()
            val emaildata = email.text.toString().trim()
            val passdata = pass.text.toString().trim()


            if(usernamedata.isEmpty()){
                username.error = "Nombre de Usuario requerido"
                username.requestFocus()
                return@setOnClickListener
            }

            if(namedata.isEmpty()){
                name.error = "Nombre requerido"
                name.requestFocus()
                return@setOnClickListener
            }

            if(dnidata.isEmpty()){
                dni.error = "Cédula requerida"
                dni.requestFocus()
                return@setOnClickListener
            }

            if(emaildata.isEmpty()){
                email.error = "Email requerido"
                email.requestFocus()
                return@setOnClickListener
            }

            if(passdata.isEmpty()){
                pass.error = "Password requerido"
                pass.requestFocus()
                return@setOnClickListener
            }



            val retrofitBuilder = Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(BASE_URL)
                    .build()
                    .create(PostApi::class.java)


            val actualdata: PostData = PostData(usernamedata as String, namedata as String, dnidata as String, emaildata as String, passdata as String)


            retrofitBuilder.createUser(actualdata).enqueue(
                    object : Callback<Any> {
                        override fun onFailure(call: Call<Any>, t: Throwable) {
                            Log.d("postfail", t.message.toString())
                        }
                        override fun onResponse( call: Call<Any>, response: Response<Any>) {
                            val addedUser = response.body()


                            Log.d("post", addedUser.toString())

                        }
                    }

            )


        }

    }


}