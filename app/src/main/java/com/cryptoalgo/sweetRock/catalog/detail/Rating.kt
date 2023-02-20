package com.cryptoalgo.sweetRock.catalog.detail

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.ServerTimestamp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class Rating(
    @DocumentId
    val id: String? = null,
    val userID: String = "",
    val rating: Float = 0f,
    val review: String = "",
    val foodID: String = "",
    @ServerTimestamp
    val timestamp: Date? = null
) {
    companion object {
        private val formatter = SimpleDateFormat("dd/MM/yy hh:mm a", Locale.US)
    }

    @Exclude
    fun formatTimestamp(): String? { return formatter.format(timestamp ?: return null) }
