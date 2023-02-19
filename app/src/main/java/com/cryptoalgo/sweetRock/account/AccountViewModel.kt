package com.cryptoalgo.sweetRock.account

import android.content.Context
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.cryptoalgo.sweetRock.R
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
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
        // Keep current user updated
        auth.addAuthStateListener { user = it.currentUser }
    }

    /**
     * Create a new user with the specified email and password
     */
    @Throws(Exception::class)
    suspend fun signUp(email: String, password: String) = suspendCoroutine<Unit> {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.i(TAG, "Successfully created new user account")
                    it.resume(Unit)
                } else {
                    Log.w(TAG, "Could not create user ")
                    it.resumeWithException(task.exception!!)
                }
            }
    }

    /**
     * Sign in with email and password
     */
    @Throws(Exception::class)
    suspend fun signIn(email: String, password: String) = suspendCoroutine<Unit> {
        if (user != null) throw AuthErrors("Already signed in")

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "Signed in successfully!")
                    it.resume(Unit)
                } else {
                    Log.w(TAG, "Sign in failure: ${task.exception}")
                    it.resumeWithException(task.exception!!)
                }
            }
    }

    /**
     * Starts the sign in flow with Google one tap sign in
     */
    @Throws
    suspend fun signIn(
        context: Context,
        launcher: ActivityResultLauncher<IntentSenderRequest>
    ) {
        val oneTapClient = Identity.getSignInClient(context)
        val signInRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setFilterByAuthorizedAccounts(false)
                    // Your server's client ID, not your Android client ID.
                    .setServerClientId(context.getString(R.string.server_client_id))
                    // Only show accounts previously used to sign in.
                    .build()
            )
            // Automatically sign in when exactly one credential is retrieved.
            // .setAutoSelectEnabled(true)
            .build()
        try {
            // Use await() from https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-play-services
            // Instead of listeners that aren't cleaned up automatically
            val result = oneTapClient.beginSignIn(signInRequest).await()

            // Now construct the IntentSenderRequest the launcher requires
            val intentSenderRequest = IntentSenderRequest.Builder(result.pendingIntent).build()
            launcher.launch(intentSenderRequest)
        } catch (e: Exception) {
            // No saved credentials found. Launch the One Tap sign-up flow, or
            // do nothing and continue presenting the signed-out UI.
            Log.w(TAG, "Cannot start sign in flow: ${e.message}")
            // Problem starting sign up flow
            throw e
        }
    }

    /**
     * Complete Google sign in flow with provided ID token
     */
    @Throws(Exception::class)
    suspend fun signIn(idToken: String) = suspendCoroutine<Unit> {
        Log.d(TAG,  "Signing in with ID token")
        auth.signInWithCredential(GoogleAuthProvider.getCredential(idToken, null))
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "Signed in successfully with Google credentials")
                    it.resume(Unit)
                } else {
                    Log.w(TAG, "Google credential sign in problem: ${task.exception}")
                    it.resumeWithException(task.exception!!)
                }
            }
    }

    fun signOut() {
        auth.signOut()
    }
}