package com.example.myapplication.data.repository

import com.example.myapplication.core.AppResult
import com.example.myapplication.data.local.MessageDao
import com.example.myapplication.data.local.toDomain
import com.example.myapplication.data.local.toEntity
import com.example.myapplication.data.network.ChatApiService
import com.example.myapplication.data.network.dto.NewMessageDto
import com.example.myapplication.data.network.dto.toDomain
import com.example.myapplication.domain.ChatRepository
import com.example.myapplication.domain.Message
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class ChatRepositoryImpl(
    private val api: ChatApiService,
    private val dao: MessageDao
) : ChatRepository {

    override suspend fun getMessages(): AppResult<List<Message>> {
        val result = safeCall {
            api.getMessages().toDomain()
        }
        
        return when (result) {
            is AppResult.Success -> {
                dao.insertAll(result.data.map { it.toEntity() })
                result
            }
            is AppResult.Failure -> {
                val saved = dao.getAll().map { it.toDomain() }
                if (saved.isNotEmpty()) {
                    AppResult.Success(saved)
                } else {
                    result
                }
            }
        }
    }

    override suspend fun sendMessage(sender: String, text: String): AppResult<Unit> = safeCall {
        val dto = NewMessageDto(
            sender = sender,
            text = text,
            createdAt = System.currentTimeMillis()
        )
        api.sendMessage(dto)
        Unit
    }

    override suspend fun deleteMessage(id: String): AppResult<Unit> = safeCall {
        api.deleteMessage(id)
        Unit
    }

    private inline fun <T> safeCall(block: () -> T): AppResult<T> = try {
        AppResult.Success(block())
    } catch (e: retrofit2.HttpException) {
        val errorBody = e.response()?.errorBody()?.string()
        AppResult.Failure.Unknown(errorBody ?: e.message())
    } catch (e: UnknownHostException) {
        AppResult.Failure.NoInternet
    } catch (e: SocketTimeoutException) {
        AppResult.Failure.Timeout
    } catch (e: IOException) {
        AppResult.Failure.NoInternet
    } catch (e: Exception) {
        AppResult.Failure.Unknown(e.message)
    }
}
