package com.example.myapplication.data.network.dto

import com.example.myapplication.domain.Message

fun MessageDto.toDomain(): Message = Message(
    id = id ?: "",
    sender = sender ?: "Unknown",
    text = text ?: "",
    createdAt = createdAt ?: 0L
)

fun List<MessageDto>.toDomain(): List<Message> =
    map { it.toDomain() }
