package com.cryptoalgo.sweetRock.account

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.lang.Error
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class AccountViewModel: ViewModel() {
    private class AuthErrors(error: String): Error(error)

    private val auth = Firebase.auth

    companion object {
        private const val TAG = "AccountViewModel"
    }

    /**
     * The currently signed-in user, or null if none is present
     */
    var user by mutableStateOf(auth.currentUser)
        private set

    init {
        auth.addAuthStateListener {
            user = it.currentUser
        }
    }

    suspend fun signIn(email: String, password: String) = suspendCoroutine<Unit> {
        if (user != null) throw AuthErrors("Already signed in")

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "Signed in successfully!")
                    it.resume(Unit)
                } else {
                    Log.d(TAG, "Sign in failure: ${task.exception}")
                    it.resumeWithException(task.exception!!)
                }
            }
    }

    fun signOut() {
        auth.signOut()
    }
}