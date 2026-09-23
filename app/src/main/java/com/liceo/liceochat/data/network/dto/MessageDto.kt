package com.liceo.liceochat.data.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class MessageDto(
    // TODO 1a: add id
    val id: String? = null,
    // TODO 1b: add sender
    val sender: String? = null,
    // TODO 1c: add text
    val text: String? = null,
    // TODO 1d: add createdAt
    val createdAt: Long? = null
)

@Serializable
data class NewMessageDto(
    val sender: String,
    val text: String,
    val createdAt: Long
)
