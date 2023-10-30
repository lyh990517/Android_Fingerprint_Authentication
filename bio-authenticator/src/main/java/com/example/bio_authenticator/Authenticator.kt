package com.example.bio_authenticator

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.flow.StateFlow

interface Authenticator {
    fun initialize(context: AppCompatActivity)
    fun canAuthenticate() : Boolean
    fun authenticate()
    fun getLoginState() : StateFlow<AuthState>
}