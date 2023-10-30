package com.example.fingerprintmodule

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bio_authenticator.AuthState
import com.example.bio_authenticator.Authenticator
import com.example.bio_authenticator.AuthenticatorFactory
import com.example.fingerprintmodule.ui.theme.FingerprintModuleTheme

class MainActivity : AppCompatActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FingerprintModuleTheme {
                Scaffold(Modifier.fillMaxSize()) {
                    AuthScreen(context = this)
                }
            }
        }
    }
}


@Composable
fun AuthScreen(context: AppCompatActivity) {
    val authenticator: Authenticator = remember { AuthenticatorFactory.create() }
    val canAuthenticate = remember { mutableStateOf(false) }
    val isLogin = authenticator.getLoginState().collectAsState()
    LaunchedEffect(Unit) {
        authenticator.initialize(context)
        canAuthenticate.value = authenticator.canAuthenticate()
    }
    LoginContent(authenticator = authenticator, canAuthenticate = canAuthenticate) {
        Crossfade(targetState = isLogin.value, label = "") { state ->
            when (state) {
                AuthState.Success -> Text(text = "Login Success!!", fontSize = 40.sp, color = Color.Blue)
                AuthState.Fail -> Text(text = "Fail!", fontSize = 40.sp, color = Color.Red)
                AuthState.Error -> Text(text = "Error!", fontSize = 40.sp, color = Color.Red)
                AuthState.Idle -> {}
            }
        }
    }
}

@Composable
fun LoginContent(
    authenticator: Authenticator,
    canAuthenticate: State<Boolean>,
    stateMessage: @Composable () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            stateMessage()
            Spacer(modifier = Modifier.height(200.dp))
            Button(onClick = { }) {
                Text(text = "login with password")
            }
            AnimatedVisibility(visible = canAuthenticate.value) {
                Button(onClick = { authenticator.authenticate() }) {
                    Text(text = "login with fingerprint")
                }
            }
        }
    }
}