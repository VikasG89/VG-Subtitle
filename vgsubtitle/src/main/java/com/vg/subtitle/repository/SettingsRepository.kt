package com.vg.subtitle.repository

import android.content.Context
import com.vg.subtitle.database.VGSubtitleDatabase
import com.vg.subtitle.database.entity.SettingsEntity

class SettingsRepository(context: Context) {
    private val settingsDao = VGSubtitleDatabase.get(context).settingsDao()

    suspend fun getString(key: String, defaultValue: String): String {
        return settingsDao.get(key)?.value ?: defaultValue
    }

    suspend fun putString(key: String, value: String) {
        settingsDao.insert(SettingsEntity(key, value, "String"))
    }

    suspend fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return settingsDao.get(key)?.value?.toBoolean() ?: defaultValue
    }

    suspend fun putBoolean(key: String, value: Boolean) {
        settingsDao.insert(SettingsEntity(key, value.toString(), "Boolean"))
    }
}
