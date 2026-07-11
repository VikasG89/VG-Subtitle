package com.vg.subtitle.utils

import android.content.Context
import java.io.File

object FileUtils {
    @JvmStatic
    fun ensureParent(path: String): File {
        val file = File(path)
        file.parentFile?.mkdirs()
        return file
    }

    @JvmStatic
    fun tempFile(context: Context, prefix: String, suffix: String): File {
        val dir = File(context.cacheDir, "vgsubtitle").apply { mkdirs() }
        return File.createTempFile(prefix, suffix, dir)
    }

    @JvmStatic
    fun requireReadable(path: String): File {
        val file = File(path)
        require(file.isFile && file.canRead()) { "File is not readable: $path" }
        return file
    }
}
