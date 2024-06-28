package cr.una.pawsitive.service

import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import cr.una.pawsitive.BuildConfig
import cr.una.pawsitive.utils.AuthorizationInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * We create a builder of the retrofit object which can be reused for all method calls
 * declared in the RestApi interface.
 */
object ServiceBuilder {
    var gson: Gson = GsonBuilder()
        .setDateFormat(BuildConfig.DATE_FORMAT)
        .create()

    // If you need to check your request change the Level
    var loggingInterceptor: HttpLoggingInterceptor = HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
        override fun log(message: String) {
            Log.d("OkHttp", message) // Etiqueta 'OkHttp' para identificar los mensajes en Logcat
        }
    }).setLevel(HttpLoggingInterceptor.Level.BODY)


    // Increased the time out to the server
    private val client =
        OkHttpClient.Builder().addInterceptor(loggingInterceptor)
            .connectTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(AuthorizationInterceptor()).build()

    private var retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL) // change this IP for testing by your actual machine IP
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(client)
        .build()

    fun <T> buildService(service: Class<T>): T {
        return retrofit.create(service)
    }

    // fun to update the base URL
    fun updateBaseUrl(newBaseUrl: String) {
        retrofit = Retrofit.Builder()
            .baseUrl(newBaseUrl)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build()
    }
}