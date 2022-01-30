package com.example.suduka


import com.example.suduka.DataClasses.CreateRoomEntity
import com.example.suduka.DataClasses.CreateRoomRequest
import com.example.suduka.DataClasses.UserEntity
import com.example.suduka.DataClasses.UserRequest
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.engine.cio.*
import io.ktor.client.features.*
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.features.websocket.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.http.cio.websocket.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.security.Security

private const val TIME_OUT = 60_000

private const val baseUrl = "https://sudokos.herokuapp.com"

private val ktorHttpClient = HttpClient(Android) {
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

val ktorSocketClient = HttpClient(CIO) {
    install(WebSockets)
}

class UserApi(private val client: HttpClient) {
    init {
        System.setProperty("io.ktor.random.secure.random.provider", "DRBG")
        Security.setProperty("securerandom.drbg.config", "HMAC_DBRG,SHA-512,256,pr_and_reseed")
    }
    suspend fun Register() : UserEntity = client.post("$baseUrl/register")
    suspend fun Login(user : UserRequest) : UserEntity = client.post("$baseUrl/login") {
        body = user
    }
    suspend fun createRoom(size : CreateRoomRequest, token : String) : CreateRoomEntity = client.post("$baseUrl/create") {
        body = size
        headers["Authorization"] = "Bearer $token"
    }
    suspend fun getGames(token: String) : HashMap<String, Any> = client.get("$baseUrl/games") {
        headers["Authorization"] = "Bearer $token"
    }
}

val userApi = UserApi(ktorHttpClient)