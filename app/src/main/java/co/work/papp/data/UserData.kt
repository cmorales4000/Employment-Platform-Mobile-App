package co.work.papp.data

import android.net.Uri
import java.io.File
import java.io.Serializable

class UserData(
    var username: String, var nombre:String, var dni:String, var email:String, var password:String,
    var contacto: String?): Serializable

//,var pdf:File var contentType: String