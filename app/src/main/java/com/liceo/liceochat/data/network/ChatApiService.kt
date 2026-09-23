package com.liceo.liceochat.data.network

import com.liceo.liceochat.data.network.dto.MessageDto
import com.liceo.liceochat.data.network.dto.NewMessageDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ChatApiService {
    // TODO 4: get every message, newest first
    @GET("messages")
    suspend fun getMessages(
        @Query("sortBy") sortBy: String = "createdAt",
        @Query("order") order: String = "desc"
    ): List<MessageDto>

    // TODO 5: send one new message
    @POST("messages")
    suspend fun sendMessage(
        @Body message: NewMessageDto
    ): MessageDto
}
