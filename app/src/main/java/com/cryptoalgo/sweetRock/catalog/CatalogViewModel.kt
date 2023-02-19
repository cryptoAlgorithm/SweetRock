package com.cryptoalgo.sweetRock.catalog

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.cryptoalgo.sweetRock.catalog.detail.Rating
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/**
 * Manages the business logic of the catalog
 */
class CatalogViewModel: ViewModel() {
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

    val catalog = mutableStateListOf<CatalogItem>()

    fun getCatalogItem(id: String): CatalogItem? = catalog.find { it.id == id }

    private val db = Firebase.firestore
    private var listener: ListenerRegistration? = null

    init { registerListener() }

    /**
     * Register a change listener on the database document to
     * populate and keep catalog fresh.
     */
    private fun registerListener() {
        // Unregister existing listeners first
        listener?.remove()
        listener = db.collection("catalog").addSnapshotListener { snap, err ->
            if (err != null) {
                // No need to detach listener on error
                listener = null
                return@addSnapshotListener
            }

            // Clear existing catalog
            catalog.clear()
            snap?.documents?.forEach { doc ->
                catalog.add(doc.toObject() ?: return@forEach)
            }
        }
    }
}