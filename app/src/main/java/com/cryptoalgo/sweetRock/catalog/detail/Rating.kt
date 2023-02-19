package com.cryptoalgo.sweetRock.catalog.detail

data class Rating(
    val userID: String = "",
    val rating: Float = 0f,
    val review: String = "",
    val foodID: String = ""
)
