package com.seoultech.livingtogether_android.util

import android.app.Application
import android.content.Context
import android.content.SharedPreferences

object SharedPreferenceManager {

    private const val INITIALIZING = "initializing"

    private lateinit var prefs: SharedPreferences

    fun init(application: Application) {
        if (!::prefs.isInitialized) {
            prefs = application.getSharedPreferences("living_together", Context.MODE_PRIVATE)
        }
    }

    private fun getString(key: String): String? {
        val str = prefs.getString(key, "null")

        return if (str.equals("null")) {
            null
        } else {
            return str
        }
    }

    private fun setString(key: String, value: String?) {
        prefs.edit().putString(key, value).apply()
    }

    private fun setBoolean(key: String, value: Boolean) {
        prefs.edit().putBoolean(key, value).apply()
    }

    private fun getBoolean(key: String): Boolean {
        return prefs.getBoolean(key, false)
    }

    private fun getInt(key: String): Int? {
        val int = prefs.getInt(key, -1)

        return if (int == -1) {
            null
        } else {
            int
        }
    }

    private fun setInt(key: String, value: Int?) {
        if (value == null) {
            prefs.edit().putInt(key, -1).apply()
        } else {
            prefs.edit().putInt(key, value).apply()
        }
    }

    fun getInitializing(): Boolean {
        return getBoolean(INITIALIZING)
    }

    fun setInitializing(isInitialized: Boolean) {
        setBoolean(INITIALIZING, isInitialized)
    }
}
