package com.cryptoalgo.sweetRock.model

import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/**
 * Represents an item in the catalog
 */
data class CatalogItem(
    @DocumentId
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val price: Float = 0f,
    val coverImage: String? = null
) {
    private val db = Firebase.firestore

    suspend fun rate(rating: Float, review: String) = suspendCoroutine {
        val user = Firebase.auth.currentUser
        if (user == null) {
            it.resumeWithException(Throwable("User is not signed in!"))
            return@suspendCoroutine
        }
        db.collection("ratings")
            .add(
                Rating(
                    userID = user.uid,
                    foodID = id,
                    rating = rating,
                    review = review
                )
            )
            .addOnSuccessListener { _ ->
                it.resume(Unit)
            }
            .addOnFailureListener { ex ->
                it.resumeWithException(ex)
            }
    }
}