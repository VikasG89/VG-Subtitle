package com.vg.subtitle.utils

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

object ThreadUtils {
    @JvmStatic
    fun ioDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @JvmStatic
    fun defaultDispatcher(): CoroutineDispatcher = Dispatchers.Default
}
