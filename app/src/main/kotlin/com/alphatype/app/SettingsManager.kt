package com.alphatype.app

import android.content.Context
import android.content.SharedPreferences

object SettingsManager {
    private const val PREFS_NAME = "alpha_type_settings"
    private const val KEY_VOICE_ENABLED = "voice_enabled"
    private const val KEY_VIBRATION_ENABLED = "vibration_enabled"
    private const val KEY_THEME = "keyboard_theme"
    private const val KEY_DEFAULT_LAYOUT = "default_layout"

    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun isVoiceEnabled(context: Context): Boolean {
        return getPrefs(context).getBoolean(KEY_VOICE_ENABLED, false) // Default OFF for professional feel
    }

    fun setVoiceEnabled(context: Context, enabled: Boolean) {
        getPrefs(context).edit().putBoolean(KEY_VOICE_ENABLED, enabled).apply()
    }

    fun isVibrationEnabled(context: Context): Boolean {
        return getPrefs(context).getBoolean(KEY_VIBRATION_ENABLED, true) // Default ON for tactical feedback
    }

    fun setVibrationEnabled(context: Context, enabled: Boolean) {
        getPrefs(context).edit().putBoolean(KEY_VIBRATION_ENABLED, enabled).apply()
    }
    
    // Theme Settings
    fun getTheme(context: Context): String {
        return getPrefs(context).getString(KEY_THEME, "dark") ?: "dark"
    }

    fun setTheme(context: Context, theme: String) {
        getPrefs(context).edit().putString(KEY_THEME, theme).apply()
    }

    // Default Layout Settings
    fun getDefaultLayout(context: Context): String {
        return getPrefs(context).getString(KEY_DEFAULT_LAYOUT, "abc") ?: "abc"
    }

    fun setDefaultLayout(context: Context, layout: String) {
        getPrefs(context).edit().putString(KEY_DEFAULT_LAYOUT, layout).apply()
    }
}
