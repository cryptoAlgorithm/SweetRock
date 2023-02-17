package com.cryptoalgo.sweetRock

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

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
        val title: String = "",
        val description: String = ""
    )

    var catalog = mutableStateListOf<CatalogItem>()
        private set

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