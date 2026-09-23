package com.example.domain.model

// What the SCREENS use. No @Serializable, no ?, and NO password.
data class User(
    val id: String,
    val fullName: String,
    val email: String,
    val birthdate: String
)
