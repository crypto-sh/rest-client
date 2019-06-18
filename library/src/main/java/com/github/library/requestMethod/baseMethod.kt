package com.github.library.requestMethod


import com.github.library.RestClient
import com.github.library.helper.LogHelper

import java.util.concurrent.TimeUnit

abstract class BaseMethod {

    internal var logHelper = LogHelper(RestClient::class.java)

    companion object {

        protected fun calcTime(startTime: Long?): String {
            val duration = timeMillisecond - startTime!!
            return TimeUnit.MILLISECONDS.toSeconds(duration).toString() + "." + (TimeUnit.MILLISECONDS.toMillis(duration) % 1000).toString().substring(0, 1)
        }

        private val timeMillisecond: Long
            get() = System.currentTimeMillis()
    }

}
