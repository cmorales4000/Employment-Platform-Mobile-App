package co.work.papp

import android.content.SharedPreferences
import android.media.session.MediaSession
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import okhttp3.Interceptor
import okhttp3.Response


class MyInterceptor:Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        class llamada : AppCompatActivity() {
            override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)

                val sharedp = getSharedPreferences("logged", MODE_PRIVATE)
                val token = sharedp.getString("token", "nope")
                Log.d("token", token.toString())

            }
        }


        val request = chain.request()
            .newBuilder()
            .addHeader("Authorization", "Token " + "9a3c2c370aeef82a9b68c1b6558a982ef39a226a")
            .build()

        //"9a3c2c370aeef82a9b68c1b6558a982ef39a226a"
        return chain.proceed(request)

    }


}

