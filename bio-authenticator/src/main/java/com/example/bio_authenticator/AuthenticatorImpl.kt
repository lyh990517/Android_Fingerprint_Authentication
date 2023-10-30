package com.example.bio_authenticator

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.concurrent.Executor

internal class AuthenticatorImpl : Authenticator {
    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo
    private lateinit var biometricManager: BiometricManager
    private val loginState = MutableStateFlow<AuthState>(AuthState.Idle)

    override fun initialize(context: AppCompatActivity) {
        executor = ContextCompat.getMainExecutor(context)
        biometricManager = BiometricManager.from(context)
        biometricPrompt = BiometricPrompt(context, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(
                    errorCode: Int,
                    errString: CharSequence
                ) {
                    super.onAuthenticationError(errorCode, errString)
                    loginState.value = AuthState.Error
                }

                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult
                ) {
                    super.onAuthenticationSucceeded(result)
                    loginState.value = AuthState.Success
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    loginState.value = AuthState.Fail
                }
            })

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("내 앱을 위한 생체 인증 로그인")
            .setSubtitle("생체 인증을 사용하여 로그인하세요")
            .setNegativeButtonText("계정 비밀번호 사용")
            .build()

    }

    override fun canAuthenticate(): Boolean {
        val result = biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.BIOMETRIC_WEAK)
        val isSuccess = result == BiometricManager.BIOMETRIC_SUCCESS

        when (result) {
            BiometricManager.BIOMETRIC_SUCCESS -> Log.e("BiometricManager", "BIOMETRIC_SUCCESS")
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> Log.e("BiometricManager", "BIOMETRIC_ERROR_HW_UNAVAILABLE")
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> Log.e("BiometricManager", "BIOMETRIC_ERROR_NO_HARDWARE")
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> Log.e("BiometricManager", "BIOMETRIC_ERROR_NONE_ENROLLED")
            BiometricManager.BIOMETRIC_ERROR_SECURITY_UPDATE_REQUIRED -> Log.e("BiometricManager", "BIOMETRIC_ERROR_SECURITY_UPDATE_REQUIRED")
            BiometricManager.BIOMETRIC_ERROR_UNSUPPORTED -> Log.e("BiometricManager", "BIOMETRIC_ERROR_UNSUPPORTED")
            BiometricManager.BIOMETRIC_STATUS_UNKNOWN -> Log.e("BiometricManager", "BIOMETRIC_STATUS_UNKNOWN")
            else -> Log.e("BiometricManager", "BIOMETRIC_STATUS_UNKNOWN")
        }

        return isSuccess
    }


    override fun authenticate() {
        biometricPrompt.authenticate(promptInfo)
    }

    override fun getLoginState(): StateFlow<AuthState> = loginState.asStateFlow()
}