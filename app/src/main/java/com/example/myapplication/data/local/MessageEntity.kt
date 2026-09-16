package com.example.myapplication.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.myapplication.domain.Message

@Entity(tableName = "messages")
data class MessageEntity(
    @androidx.room.PrimaryKey 
    val id: String,
    val sender: String,
    val text: String,
    val createdAt: Long
)

fun MessageEntity.toDomain(): Message = Message(
    id = id,
    sender = sender,
    text = text,
    createdAt = createdAt
)

fun Message.toEntity(): MessageEntity = MessageEntity(
    id = id,
    sender = sender,
    text = text,
    createdAt = createdAt
)
