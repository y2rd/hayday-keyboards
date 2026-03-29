package com.example.haydaykeyboard.util

import android.content.Context

class SettingsStore(context: Context) {
    private val prefs = context.getSharedPreferences("hayday_keyboard_prefs", Context.MODE_PRIVATE)

    fun isArabic(): Boolean = prefs.getBoolean(KEY_ARABIC, false)
    fun setArabic(value: Boolean) = prefs.edit().putBoolean(KEY_ARABIC, value).apply()

    fun favorites(): Set<String> = prefs.getStringSet(KEY_FAVORITES, emptySet()) ?: emptySet()
    fun setFavorites(values: Set<String>) = prefs.edit().putStringSet(KEY_FAVORITES, values).apply()

    fun lastMachineId(): String? = prefs.getString(KEY_LAST_MACHINE_ID, null)
    fun setLastMachineId(value: String?) = prefs.edit().putString(KEY_LAST_MACHINE_ID, value).apply()

    companion object {
        private const val KEY_ARABIC = "is_arabic"
        private const val KEY_FAVORITES = "favorites"
        private const val KEY_LAST_MACHINE_ID = "last_machine_id"
    }
}
