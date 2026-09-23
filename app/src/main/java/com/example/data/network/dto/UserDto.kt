package com.example.data.network.dto

import kotlinx.serialization.Serializable

// What the SERVER sends. We do not trust it, so every field may be missing.
@Serializable
data class UserDto(
    val id: String? = null,
    val fullname: String? = null,
    val email: String? = null,
    val password: String? = null,
    val birthdate: String? = null
)

// What WE send when creating an account. We made it, so nothing is missing.
@Serializable
data class NewUserDto(
    val fullname: String,
    val email: String,
    val password: String,
    val birthdate: String
)
