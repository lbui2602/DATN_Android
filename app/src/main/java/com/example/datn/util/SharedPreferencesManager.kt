package com.example.datn.util

import android.content.SharedPreferences
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharedPreferencesManager @Inject constructor(private val prefs: SharedPreferences) {

    fun saveAuthToken(token: String) {
        prefs.edit().putString(AUTH_TOKEN, token).apply()
    }

    fun getAuthToken(): String? {
        return prefs.getString(AUTH_TOKEN, null)
    }

    fun clearAuthToken() {
        prefs.edit().remove(AUTH_TOKEN).apply()
    }

    fun saveFaceToken(token: String) {
        prefs.edit().putString(FACE_TOKEN, token).apply()
    }

    fun getFaceToken(): String? {
        return prefs.getString(FACE_TOKEN, null)
    }

    fun clearFaceToken() {
        prefs.edit().remove(FACE_TOKEN).apply()
    }

    fun saveUserId(userId: String) {
        prefs.edit().putString(USER_ID, userId).apply()
    }

    fun getUserId(): String? {
        return prefs.getString(USER_ID, null)
    }

    fun clearUserId() {
        prefs.edit().remove(USER_ID).apply()
    }

    fun saveUserRole(userRole: String) {
        prefs.edit().putString(USER_ROLE, userRole).apply()
    }

    fun getUserRole(): String? {
        return prefs.getString(USER_ROLE, null)
    }

    fun clearUserRole() {
        prefs.edit().remove(USER_ROLE).apply()
    }

    companion object {
        private const val AUTH_TOKEN = "AUTH_TOKEN"
        private const val FACE_TOKEN = "FACE_TOKEN"
        private const val USER_ID = "USER_ID"
        private const val USER_ROLE = "USER_ROLE"
    }
}
