package com.ivanhai.suduka.DataClasses

import kotlinx.serialization.Serializable

@Serializable
data class GamesEntity(
    val list : HashMap<String, Int>
)
