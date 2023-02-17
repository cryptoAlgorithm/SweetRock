package com.cryptoalgo.sweetRock

import com.google.firebase.firestore.DocumentId
import java.util.UUID

class CatalogProvider {
    data class CatalogItem(
        @DocumentId
        val id: UUID,
        val title: String,
        val description: String
    )
}