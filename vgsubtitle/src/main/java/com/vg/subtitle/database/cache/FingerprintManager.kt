package com.vg.subtitle.database.cache

import java.io.File
import java.security.MessageDigest

object FingerprintManager {
    fun calculateFingerprint(file: File): String {
        // Simple fingerprint: size + last modified + prefix hash
        val digest = MessageDigest.getInstance("MD5")
        val info = "${file.length()}_${file.lastModified()}"
        digest.update(info.toByteArray())
        
        // Hash first 1MB if possible
        if (file.exists() && file.length() > 0) {
            file.inputStream().use { input ->
                val buffer = ByteArray(1024 * 1024)
                val read = input.read(buffer)
                if (read > 0) {
                    digest.update(buffer, 0, read)
                }
            }
        }
        
        return digest.digest().joinToString("") { "%02x".format(it) }
    }
}
