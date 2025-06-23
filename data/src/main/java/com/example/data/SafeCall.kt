package com.example.data

import com.example.core.domain.DataError
import retrofit2.Response
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import com.example.core.domain.Result
import kotlinx.coroutines.ensureActive
import kotlin.coroutines.coroutineContext

suspend inline fun <reified T> safeCall(
    execute: () -> Response<T>
): Result<T, DataError.Remote> {
    val response = try {
        execute()
    } catch (e: SocketTimeoutException) {
        return Result.Error(DataError.Remote.REQUEST_TIMEOUT)
    } catch (e: UnknownHostException) {
        return Result.Error(DataError.Remote.NO_INTERNET)
    } catch (e: Exception) {
        coroutineContext.ensureActive()
        return Result.Error(DataError.Remote.UNKNOWN)
    }

    return responseToResult(response)
}

suspend inline fun <reified T> responseToResult(response: Response<T>): Result<T, DataError.Remote> {
    return if (response.isSuccessful) {
        val body = response.body()
        if (body != null) {
            Result.Success(body)
        } else {
            Result.Error(DataError.Remote.UNKNOWN)
        }
    } else {
        when (response.code()) {
            408 -> Result.Error(DataError.Remote.REQUEST_TIMEOUT)
            429 -> Result.Error(DataError.Remote.TOO_MANY_REQUESTS)
            in 500..599 -> Result.Error(DataError.Remote.SERVER)
            else -> Result.Error(DataError.Remote.UNKNOWN)
        }
    }
}