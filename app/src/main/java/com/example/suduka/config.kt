package com.example.suduka


import com.example.suduka.DataClasses.CreateRoomEntity
import com.example.suduka.DataClasses.CreateRoomRequest
import com.example.suduka.DataClasses.UserEntity
import com.example.suduka.DataClasses.UserRequest
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.features.DefaultRequest
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.*
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import kotlinx.serialization.json.Json

private const val TIME_OUT = 60_000

private const val baseUrl = "https://sudokos.herokuapp.com"

val ktorHttpClient = HttpClient(Android) {
    install(JsonFeature) {
        serializer = KotlinxSerializer(Json {
            isLenient = true
            ignoreUnknownKeys = true
        })

        engine {
            connectTimeout = TIME_OUT
            socketTimeout = TIME_OUT
        }
    }

    install(DefaultRequest) {
        header(HttpHeaders.ContentType, ContentType.Application.Json)
    }
}

class UserApi(private val client: HttpClient) {
    suspend fun Register() : UserEntity = client.post("$baseUrl/register")
    suspend fun Login(user : UserRequest) : UserEntity = client.post("$baseUrl/login") {
        body = user
    }
    suspend fun createRoom(data : CreateRoomRequest) : CreateRoomEntity = client.post("$baseUrl/create") {
        body = data
        headers["Authorization"] = "Bearer ${data.token}"
    }

}

val userApi = UserApi(ktorHttpClient)