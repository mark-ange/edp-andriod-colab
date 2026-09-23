package com.liceo.liceochat.data.repository

import com.liceo.liceochat.core.AppResult
import com.liceo.liceochat.data.network.ChatApiService
import com.liceo.liceochat.data.network.dto.NewMessageDto
import com.liceo.liceochat.data.network.dto.toDomain
import com.liceo.liceochat.domain.ChatRepository
import com.liceo.liceochat.domain.Message
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class ChatRepositoryImpl(
    private val api: ChatApiService
) : ChatRepository {

    // TODO 7: get the messages
    override suspend fun getMessages(): AppResult<List<Message>> = safeCall {
        api.getMessages().toDomain()
    }

    // TODO 8: send a message
    override suspend fun sendMessage(sender: String, text: String): AppResult<Unit> = safeCall {
        val dto = NewMessageDto(sender, text, System.currentTimeMillis())
        api.sendMessage(dto)
        Unit
    }

    private inline fun <T> safeCall(block: () -> T): AppResult<T> =
        try { AppResult.Success(block()) }
        catch (e: UnknownHostException) { AppResult.Failure.NoInternet }
        catch (e: SocketTimeoutException) { AppResult.Failure.Timeout }
        catch (e: IOException) { AppResult.Failure.NoInternet }
        catch (e: Exception) { AppResult.Failure.Unknown(e.message) }
}
