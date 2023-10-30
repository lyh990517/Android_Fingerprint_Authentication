package com.example.bio_authenticator

class AuthenticatorFactory {
    companion object{
        fun create() : Authenticator {
            return AuthenticatorImpl()
        }
    }
}