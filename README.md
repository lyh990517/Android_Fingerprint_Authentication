# Fingerprint Authentication Library

This project provides a fingerprint authentication library for use in Android applications.

## Overview

This library is designed to easily integrate biometric authentication functionality into your Android apps. It offers a simple API to implement the fingerprint authentication process.

## How to Use

### Initialization

```kotlin
val authenticator: Authenticator = AuthenticatorFactory.create()
authenticator.initialize(this) // 'this' refers to an instance of AppCompatActivity.
```

### Check Authentication Capability

```kotlin
if (authenticator.canAuthenticate()) {
    // In case authentication is possible
} else {
    // In case authentication is not possible
}
```

### Execute Authentication

```kotlin
authenticator.authenticate()
```

### Observe Authentication State

```kotlin
authenticator.getLoginState().collect { state ->
    when (state) {
        is AuthState.Idle -> // Initial state
        is AuthState.Error -> // Authentication error
        is AuthState.Fail -> // Authentication failure
        is AuthState.Success -> // Authentication success
    }
}
```

### Customization and Contribution
You are free to modify and extend this library to suit your needs. If you have any changes or improvements, please send a Pull Request. Also, if you find this project helpful, please give it a Star to show your support!

### Usage Example
Here is a simple example of using this library:

#### In XML
```kotlin
class MyActivity : AppCompatActivity() {

    private lateinit var authenticator: Authenticator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my)

        authenticator = AuthenticatorFactory.create()
        authenticator.initialize(this)

        if (authenticator.canAuthenticate()) {
            authenticator.authenticate()
        }

        lifecycleScope.launchWhenStarted {
            authenticator.getLoginState().collect { state ->
                // Update UI based on state
            }
        }
    }
}
```
#### In Jetpack Compose
```kotlin
@Composable
fun Example(context: AppCompatActivity){
    val authenticator: Authenticator = remember { AuthenticatorFactory.create() }
    val isLogin = authenticator.getLoginState().collectAsState()
    val canAuthenticate = remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        authenticator.initialize(context)
        canAuthenticate.value = authenticator.canAuthenticate()
    }
    when (isLogin.value) {
        AuthState.Success -> Text(text = "Login Success!!")
        AuthState.Fail -> Text(text = "Fail!")
        AuthState.Error -> Text(text = "Error!")
        AuthState.Idle -> {}
    }
    Button(onClick = { authenticator.authenticate() }) {
        Text(text = "login with fingerprint")
    }
}
```
