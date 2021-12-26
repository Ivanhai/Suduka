package com.example.suduka.DataClasses

import kotlinx.serialization.Serializable

@Serializable
data class UserRequest(
    val username : String,
    val password : String
)
