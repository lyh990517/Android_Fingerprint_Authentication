package com.example.bio_authenticator

sealed class AuthState {
    object Idle : AuthState()
    object Error : AuthState()
    object Fail : AuthState()
    object Success : AuthState()
}