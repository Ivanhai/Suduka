package com.example.suduka.DataClasses

import kotlinx.serialization.Serializable

@Serializable
data class UserEntity(
    val token : String? = null
)
