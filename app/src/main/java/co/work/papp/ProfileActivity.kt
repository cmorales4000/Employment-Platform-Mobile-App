package co.work.papp

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import co.work.MyAPI
import co.work.PostApi
import co.work.papp.data.UploadRequestBody
import co.work.papp.data.UserData
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_profile.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream


class ProfileActivity : AppCompatActivity(), UploadRequestBody.UploadCallback {

    private var selectedImage: Uri? = null
    private var selectedFile: Uri? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val sharedp = getSharedPreferences("logged", MODE_PRIVATE)
        val id = sharedp.getInt("id", 0)
        val mLobby = Intent(this, MainActivity::class.java)



        uploadimage.setOnClickListener { openImage() }
        pdf.setOnClickListener {
            openFile()
        }



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
                    override fun onResponse(
                            call: Call<Map<String, Any>>,
                            response: Response<Map<String, Any>>
                    ) {
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
                        if (contacto != "null") {
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
            var contactodata = contact.text.toString().trim()

            uploadFile()
            uploadImage()

            var actualdata: UserData = UserData(usernamedata!!, namedata!!, dnidata!!, emaildata!!, password, contactodata)

            retrofitBuilder.update(id, actualdata).enqueue(
                    object : Callback<Map<String, Any>> {
                        override fun onResponse(
                                call: Call<Map<String, Any>>,
                                response: Response<Map<String, Any>>
                        ) {
                            var modification = response.body()

                            Log.d("modificacion", modification.toString())
                        }

                        override fun onFailure(call: Call<Map<String, Any>>, t: Throwable) {
                        }
                    }
            )
        }

    }


    private fun openImage() {
        Intent(Intent.ACTION_PICK).also{
            it.type = "image/*"
            startActivityForResult(it, REQUEST_CODE_IMAGE_PICKER)
        }
    }

    private fun openFile() {
        Intent(Intent.ACTION_PICK).also{

            it.type = "image/*|application/*|text/*"
            //it.type = "application/pdf"
            startActivityForResult(it, REQUEST_CODE_FILE_PICKER)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK){
            when(requestCode){
                REQUEST_CODE_IMAGE_PICKER -> {
                    selectedImage = data?.data
                    profileimage.setImageURI(selectedImage)

                }
                REQUEST_CODE_FILE_PICKER -> {
                    selectedFile = data?.data


                }
            }
        }
    }

    override fun onProgressUpdate(percentage: Int) {}

    companion object{
        private const val REQUEST_CODE_IMAGE_PICKER = 100
        private const val REQUEST_CODE_FILE_PICKER = 110
    }

    private fun uploadImage(){

        val sharedp = getSharedPreferences("logged", MODE_PRIVATE)
        val id = sharedp.getInt("id", 0)

        if(selectedImage == null){
            //Toast.makeText(this, "No Image", Toast.LENGTH_LONG).show()

        } else {

            val parceFileDescriptor =
                    contentResolver.openFileDescriptor(selectedImage!!, "r", null) ?: return
            val file = File(cacheDir, contentResolver.getFileName(selectedImage!!))
            val inputStream = FileInputStream(parceFileDescriptor.fileDescriptor)
            val outputStream = FileOutputStream(file)
            inputStream.copyTo(outputStream)


            val body = UploadRequestBody(file, "image", this)

            MyAPI().uploadImage(
                    id,
                    MultipartBody.Part.createFormData("imagen", file.name, body),
                    file.asRequestBody("image/*".toMediaTypeOrNull())
            ).enqueue(object : Callback<Any> {
                override fun onResponse(call: Call<Any>, response: Response<Any>) {

                    Log.d("subidaimagen", response.toString())
                    val toast = Toast.makeText(applicationContext, "Imagen subida", Toast.LENGTH_LONG)
                    toast.show()
                }

                override fun onFailure(call: Call<Any>, t: Throwable) {
                }
            })
        }
    }

    private fun uploadFile(){

        val sharedp = getSharedPreferences("logged", MODE_PRIVATE)
        val id = sharedp.getInt("id", 0)

        if(selectedFile == null){


        } else {

            val parceFileDescriptor =
                    contentResolver.openFileDescriptor(selectedFile!!, "r", null) ?: return
            val file = File(cacheDir, contentResolver.getFileName(selectedFile!!))
            val inputStream = FileInputStream(parceFileDescriptor.fileDescriptor)
            val outputStream = FileOutputStream(file)
            inputStream.copyTo(outputStream)


            val body2 = UploadRequestBody(file, "application/pdf", this)

            MyAPI().uploadFile(
                    id,
                    MultipartBody.Part.createFormData("hdv", file.name, body2),
                    file.asRequestBody("application/pdf".toMediaTypeOrNull())
            ).enqueue(object : Callback<Any> {
                override fun onResponse(call: Call<Any>, response: Response<Any>) {

                    Log.d("subidapdf", response.toString())
                    val toast = Toast.makeText(applicationContext, "Documento Subido", Toast.LENGTH_LONG)
                    toast.show()
                }

                override fun onFailure(call: Call<Any>, t: Throwable) {
                }
            })
        }
    }


}



