package com.example.suduka.DataClasses

import kotlinx.serialization.Serializable

@Serializable
data class CreateRoomRequest(
    val rsize : Int
)
