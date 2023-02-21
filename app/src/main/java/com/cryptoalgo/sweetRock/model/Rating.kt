package com.cryptoalgo.sweetRock.model

import android.util.Log
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.ServerTimestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

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
        private const val TAG = "Rating"
    }

    @Exclude
    fun formatTimestamp(): String? { return formatter.format(timestamp ?: return null) }

    /**
     * Delete this rating
     *
     * This will throw if the post is not owned by the current user
     */
    @Throws
    suspend fun delete() = suspendCoroutine {
        if (userID != Firebase.auth.currentUser?.uid) {
            it.resumeWithException(Exception("Rating was not created by the current user!"))
            return@suspendCoroutine
        }
        Firebase.firestore.collection("ratings").document(id!!).delete()
            .addOnSuccessListener { _ ->
                Log.d(TAG, "Successfully deleted rating $id")
                it.resume(Unit)
            }
            .addOnFailureListener { error ->
                Log.w(TAG, "Failed to delete rating: ${error.localizedMessage}")
                it.resumeWithException(error)
            }
    }
}