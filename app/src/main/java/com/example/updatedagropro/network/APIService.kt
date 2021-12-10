package com.example.updatedagropro.network

import android.service.controls.templates.TemperatureControlTemplate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import java.util.*
import java.util.concurrent.TimeUnit


interface APIService {
    @FormUrlEncoded
    @POST("slave_sensor")
    suspend fun getSensorData(
        @Field("slave_id") id: String
    ): SensorData
}

object API {
    var okHttpClient = OkHttpClient.Builder()
        .readTimeout(2, TimeUnit.SECONDS)
        .writeTimeout(5, TimeUnit.SECONDS)
        .build()

    private val retrofit: APIService by lazy {
        Retrofit.Builder()
            .baseUrl("http://192.168.4.1/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(APIService::class.java)
    }

    suspend fun getSensorData(id: String): Result<SensorData> {
        return try {
            Result.success(retrofit.getSensorData(id))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    @Serializable
    data class SlaveReading(
        @SerialName("id")
        val id: String,
        @SerialName("sm")
        val soilMoisture: Float,
        @SerialName("st")
        val soilTemperature: Float,
        @SerialName("ah")
        val atmosphericHumidity: Float,
        @SerialName("at")
        val atmosphericTemperature: Float,
        @SerialName("ts")
        val time: Long,
        @SerialName("valve")
        val valve: Boolean,
        @SerialName("th")
        val threshold: List<Float>
    )

    @Serializable
    data class RealtimeData(
        @SerialName("readings")
        val readings: List<SlaveReading>,
        @SerialName("motor")
        val motor: Boolean,
        @SerialName("ts")
        val time: Long,
        @SerialName("wind")
        val windSpeed: Float,
        @SerialName("sun")
        val sunlightIntensity: Float
    )

    /**
     * {
     *   readings: [
     *      {
     *          id: 1,
     *          sm: 23,
     *          st: 22,
     *          ah: 40,
     *          at: 45,
     *          ts: 133435345,
     *          valve: true,
     *          th: [40, 50]
     *      },
     *      {
     *          id: 2,
     *          sm: 24,
     *          st: 25,
     *          ah: 44,
     *          at: 43,
     *          ts: 133435355,
     *          valve: false,
     *          th: [45, 55]
     *      },
     *      ...
     *   ],
     *   motor: true,
     *   wind: 2.6,
     *   sun: 34
     * }
     */

    fun realtimeData(){

    }


    fun timeSeries(){

    }
}