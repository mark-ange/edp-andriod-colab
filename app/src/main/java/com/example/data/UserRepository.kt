package com.example.data

import com.example.core.AppResult
import com.example.data.network.NetworkModule
import com.example.data.network.UserApiService
import com.example.data.network.dto.NewUserDto
import com.example.data.network.dto.UserDto
import com.example.data.network.dto.toDomain
import com.example.domain.model.User
import kotlinx.serialization.SerializationException
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class UserRepository(
    private val api: UserApiService = NetworkModule.userApi
) {
    private suspend fun findUsers(email: String): List<UserDto> =
        try {
            api.findByEmail(email)
        } catch (e: HttpException) {
            if (e.code() == 404) emptyList() else throw e
        }

    private inline fun <T> safeCall(block: () -> AppResult<T>): AppResult<T> =
        try {
            block()
        } catch (e: UnknownHostException) {
            AppResult.Failure.NoInternet
        } catch (e: SocketTimeoutException) {
            AppResult.Failure.Timeout
        } catch (e: HttpException) {
            AppResult.Failure.Unknown("Server error ${e.code()}")
        } catch (e: SerializationException) {
            AppResult.Failure.Unknown("The server sent data we could not read.")
        } catch (e: IOException) {
            AppResult.Failure.NoInternet
        } catch (e: Exception) {
            AppResult.Failure.NoInternet
        }

    suspend fun login(email: String, password: String): AppResult<User> = safeCall {
        val matches = findUsers(email.trim())
        val found = matches.firstOrNull { 
            it.email.equals(email.trim(), ignoreCase = true) && it.password == password 
        }
        if (found == null) AppResult.Failure.WrongLogin else AppResult.Success(found.toDomain())
    }

    suspend fun register(
        fullName: String,
        email: String,
        password: String,
        birthdate: String
    ): AppResult<User> = safeCall {
        val taken = findUsers(email.trim()).any { it.email.equals(email.trim(), ignoreCase = true) }
        if (taken) {
            AppResult.Failure.EmailTaken
        } else {
            val saved = api.createUser(
                NewUserDto(
                    fullname = fullName.trim(),
                    email = email.trim(),
                    password = password,
                    birthdate = birthdate.trim()
                )
            )
            AppResult.Success(saved.toDomain())
        }
    }
}
