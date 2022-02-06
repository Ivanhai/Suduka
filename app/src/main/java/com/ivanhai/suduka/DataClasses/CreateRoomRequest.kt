package com.ivanhai.suduka.DataClasses

import kotlinx.serialization.Serializable

@Serializable
data class CreateRoomRequest(
    val rsize : Int
)
