package com.cryptoalgo.sweetRock.account

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountLanding() {
    var email by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(""))
    }
    var password by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(""))
    }

    Box {
        LazyColumn(Modifier.fillMaxSize(), contentPadding = PaddingValues(16.dp)) {
            item {
                ElevatedCard(
                    shape = MaterialTheme.shapes.extraLarge,
                    elevation = CardDefaults.elevatedCardElevation(defaultElevation = 3.dp)
                ) {
                    Column(Modifier.padding(12.dp, 8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text = "Welcome Back",
                            Modifier.fillMaxWidth().padding(top = 8.dp), textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.headlineMedium
                        )
                        Text(
                            text = "Save and sync your cart, place orders, add reviews and more!",
                            Modifier.fillMaxWidth(), textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodySmall
                        )
                        OutlinedTextField(
                            value = email, onValueChange = { email = it },
                            Modifier.fillMaxWidth(),
                            label = { Text("Email") }
                        )
                        OutlinedTextField(
                            value = password, onValueChange = { password = it },
                            Modifier.fillMaxWidth(),
                            visualTransformation = PasswordVisualTransformation(),
                            label = { Text("Password") }
                        )

                        Button(onClick = {  }, Modifier.fillMaxWidth().padding(top = 4.dp)) {
                            Text("Log in")
                        }
                    }
                }
            }
        }
    }
}

