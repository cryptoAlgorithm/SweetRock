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

    /**
     * Register a change listener on the database document to
     * populate and keep catalog fresh.
     */
    fun registerListener() {
        // Unregister existing listeners first
        listener?.remove()
        listener = db.collection("catalog").addSnapshotListener { snap, err ->
            if (err != null) {
                listener?.remove()
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