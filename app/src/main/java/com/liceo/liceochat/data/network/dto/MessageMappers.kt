package com.liceo.liceochat.data.network.dto

import com.liceo.liceochat.domain.Message

fun MessageDto.toDomain(): Message = Message(
    id = id ?: "",
    // TODO 3a: sender
    sender = sender ?: "Unknown",
    // TODO 3b: text
    text = text ?: "",
    // TODO 3c: createdAt
    createdAt = createdAt ?: 0L
)

fun List<MessageDto>.toDomain(): List<Message> = map { it.toDomain() }
