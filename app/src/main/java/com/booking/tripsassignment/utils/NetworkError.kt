package com.booking.tripsassignment.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.lang.Exception

//Used this instead of IO exception or Runtime to provide more context on what went wrong. As we scale this might be imp
class NetworkError : Exception {
    constructor() : super()
    constructor(message: String?) : super(message)
    constructor(message: String?, cause: Throwable?) : super(message, cause)
    constructor(cause: Throwable?) : super(cause)

    @RequiresApi(Build.VERSION_CODES.N)
    constructor(message: String?, cause: Throwable?, enableSuppression: Boolean, writableStackTrace: Boolean) : super(
        message,
        cause,
        enableSuppression,
        writableStackTrace
    )
}