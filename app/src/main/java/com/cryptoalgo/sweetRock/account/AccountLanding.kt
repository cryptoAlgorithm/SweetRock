package com.cryptoalgo.sweetRock.account

import android.app.Activity
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cryptoalgo.sweetRock.MainViewModel
import com.cryptoalgo.sweetRock.R
import com.google.android.gms.auth.api.identity.Identity
import kotlinx.coroutines.launch

private const val TAG = "Account"

@Composable
fun AccountLanding(
    model: AccountViewModel = viewModel()
) {
    Box {
        Box {
            Image(
                painter = painterResource(id = R.drawable.banner), contentDescription = null,
                Modifier
                    .height(200.dp)
                    .fillMaxWidth(),
                contentScale = ContentScale.Crop
            )
            Spacer(
                Modifier
                    .matchParentSize()
                    .background(
                        Brush.verticalGradient(
                            0.75f to Color.Transparent,
                            1f to MaterialTheme.colorScheme.background
                        )
                    )
            )
        }
        LazyColumn(Modifier.fillMaxSize(), contentPadding = PaddingValues(16.dp)) {
            item { Spacer(Modifier.height(140.dp)) }
            item {
                when {
                    model.user != null -> SignedInLanding()
                    else -> SignInLaunchpad()
                }
            }
        }
    }
}

@Composable
private fun SignedInLanding(
    model: AccountViewModel = viewModel()
) {
    val user = model.user ?: return // Return early if the current user is null

    ElevatedCard(
        Modifier.fillMaxWidth(),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 0.dp),
        shape = MaterialTheme.shapes.large
    ) {
        Column(Modifier.padding(16.dp, 12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text("Your Account", fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.headlineMedium)
            user.displayName?.let {
                Text(it)
            }
            Text(user.email ?: "No Email")
            Button(onClick = { model.signOut() },
                Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)) {
                Text("Sign Out")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SignInLaunchpad(
    model: AccountViewModel = viewModel(),
    mainModel: MainViewModel = viewModel(),
) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    var loadingMethod by remember { mutableStateOf<Int?>(null) }
    var error by remember { mutableStateOf<String?>(null) }

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        if (result.resultCode != Activity.RESULT_OK) {
            loadingMethod = null
            // The user cancelled the login, was it due to an Exception?
            if (result.data?.action == ActivityResultContracts.StartIntentSenderForResult.ACTION_INTENT_SENDER_REQUEST) {
                /*val exception: Exception? = result.data?.getSerializableExtra(
                    ActivityResultContracts.StartIntentSenderForResult.EXTRA_SEND_INTENT_EXCEPTION)*/
                Log.e(TAG, "Couldn't start One Tap UI")
            } else Log.w(TAG, "User cancelled flow")
            return@rememberLauncherForActivityResult
        }
        val oneTapClient = Identity.getSignInClient(context)
        val credential = oneTapClient.getSignInCredentialFromIntent(result.data)
        val idToken = credential.googleIdToken
        if (idToken != null) {
            // Got an ID token from Google. Use it to authenticate
            // with your backend.
            coroutineScope.launch {
                try {
                    model.signIn(idToken)
                } catch (e: Exception) {
                    mainModel.snackbarHostState.showSnackbar("Failed to sign you in with Google")
                } finally {
                    loadingMethod = null
                }
            }
        } else {
            Log.w("LOG", "Null Token")
            loadingMethod = null
        }
    }

    var email by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(""))
    }
    var password by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(""))
    }

    ElevatedCard(
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 0.dp),
        shape = MaterialTheme.shapes.large
    ) {
        Column(Modifier.padding(8.dp, 8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                "Welcome Back",
                Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineMedium
            )
            Text(
                text = "Place orders, add reviews and more!",
                Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp), textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodySmall
            )

            // Email sign-in
            OutlinedTextField(
                value = email, onValueChange = { email = it },
                Modifier.fillMaxWidth(),
                label = { Text("Email") }
            )
            OutlinedTextField(
                value = password, onValueChange = { password = it },
                Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation(),
                label = { Text("Password") },
                supportingText = { if (error != null) Text(error!!) },
                isError = error != null
            )

            Button(
                onClick = { coroutineScope.launch {
                    loadingMethod = 0
                    try {
                        model.signIn(email.text, password.text)
                    } catch (e: Exception) {
                        error = e.message
                        Log.d("AccountLanding", "Sign in failed")
                    } finally {
                        loadingMethod = null
                    }
                }},
                Modifier.fillMaxWidth(),
                enabled = email.text.trim().isNotBlank() && password.text.isNotEmpty() && loadingMethod != 0
            ) {
                if (loadingMethod == 0) {
                    CircularProgressIndicator(
                        Modifier
                            .padding(end = 8.dp)
                            .size(16.dp),
                        strokeWidth = 2.dp,
                        strokeCap = StrokeCap.Round
                    )
                }
                Text("Continue with Email")
            }

            Row(
                Modifier.padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Divider(Modifier.weight(1f))
                Text("You can also...", style = MaterialTheme.typography.titleSmall)
                Divider(Modifier.weight(1f))
            }

            FilledTonalButton(
                onClick = { coroutineScope.launch {
                    loadingMethod = 1
                    try {
                        model.signIn(context, launcher)
                    } catch (e: Exception) {
                        // Problem initiating sign in flow
                        loadingMethod = null
                        mainModel.snackbarHostState.showSnackbar("Could not initiate Google sign-in, please ensure Google Play services is present and updated")
                    }
                }},
                Modifier.fillMaxWidth()
            ) {
                if (loadingMethod == 1) {
                    CircularProgressIndicator(
                        Modifier
                            .padding(end = 8.dp)
                            .size(16.dp),
                        strokeWidth = 2.dp,
                        strokeCap = StrokeCap.Round
                    )
                }
                Text("Continue with Google")
            }
        }
    }
}