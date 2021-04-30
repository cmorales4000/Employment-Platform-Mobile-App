package co.work.papp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import co.work.PostApi
import co.work.papp.data.UserData
import com.bumptech.glide.Glide
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.activity_profile.dni
import kotlinx.android.synthetic.main.activity_profile.email
import kotlinx.android.synthetic.main.activity_profile.name
import kotlinx.android.synthetic.main.activity_profile.username
import kotlinx.android.synthetic.main.activity_register.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val sharedp = getSharedPreferences("logged", MODE_PRIVATE)
        val id = sharedp.getInt("id", 0)
        val mLobby = Intent(this, MainActivity::class.java)



        logout.setOnClickListener {
            val editor = sharedp.edit()
            editor.putInt("id", 0)
            editor.putInt("token", 0)
            editor.apply()
            startActivity(mLobby)

        }


        var password = ""


        var usernamebox =findViewById<EditText>(R.id.username)
        var namebox =findViewById<EditText>(R.id.name)
        var dnibox =findViewById<EditText>(R.id.dni)
        var emailbox =findViewById<EditText>(R.id.email)
        //var passbox =findViewById<EditText>(R.id.pass)
        var contactobox =findViewById<EditText>(R.id.contact)
        var imagebox =findViewById<ImageView>(R.id.profileimage)


        val retrofitBuilder = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
                .create(PostApi::class.java)


        retrofitBuilder.getprofile(id).enqueue(
                object : Callback<Map<String, Any>> {
                    override fun onResponse(call: Call<Map<String, Any>>, response: Response<Map<String, Any>>) {
                        var actualuser = response.body()



                        Log.d("actualuser", actualuser.toString())

                        val username = actualuser?.get("username") as String
                        val nombre = actualuser?.get("nombre") as String
                        val dni = (((actualuser?.get("dni") as Double).toInt()).toString())
                        val email = actualuser?.get("email") as String
                        var contacto = (((actualuser?.get("contacto") as? Double)?.toInt()).toString())
                        password = actualuser?.get("password") as String
                        val imagen = actualuser?.get("imagen") as? String



                        usernamebox.setText(username)
                        namebox.setText(nombre)
                        dnibox.setText(dni)
                        emailbox.setText(email)
                        if (contacto != "null"){
                            contactobox.setText(contacto)
                        }
                        //passbox.setText(password)
                        //Picasso.with(getApplicationContext()).load(imagen).into(imagebox);
                        Glide.with(getApplicationContext()).load(imagen).into(imagebox)

                    }

                    override fun onFailure(call: Call<Map<String, Any>>, t: Throwable) {
                    }
                }
        )



        editBut.setOnClickListener {

            var usernamedata = username.text.toString().trim()
            var namedata = name.text.toString().trim()
            var dnidata = dni.text.toString().trim()
            var emaildata = email.text.toString().trim()
            //var passdata = pass.text.toString().trim()
            var contactodata = contact.text.toString().trim()




            Log.d("contacto", contactodata.toString())


            var actualdata: UserData = UserData(usernamedata!! , namedata!! , dnidata!! , emaildata!! , password, contactodata)

            retrofitBuilder.update(id, actualdata).enqueue(
                object : Callback<Map<String, Any>> {
                    override fun onResponse(call: Call<Map<String, Any>>, response: Response<Map<String, Any>>) {
                        var modification = response.body()

                        Log.d("modificacion", modification.toString())

                    }

                    override fun onFailure(call: Call<Map<String, Any>>, t: Throwable) {

                        Log.d("modificacionfail", t.message.toString())
                    }
                }
            )

        }


    }
}



