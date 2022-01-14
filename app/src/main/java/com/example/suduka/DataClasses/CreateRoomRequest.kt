package com.example.suduka.DataClasses

import kotlinx.serialization.Serializable

@Serializable
data class CreateRoomRequest(
    val token : String,
    val rsize : Int
)
