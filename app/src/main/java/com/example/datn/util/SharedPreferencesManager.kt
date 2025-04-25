package com.example.datn.util

import android.content.SharedPreferences
import com.example.datn.models.working_day.IdDepartment
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

    fun saveDepartment(idDepartment: String) {
        prefs.edit().putString(ID_DEPARTMENT, idDepartment).apply()
    }

    fun getDepartment(): String? {
        return prefs.getString(ID_DEPARTMENT, null)
    }

    fun clearDepartment() {
        prefs.edit().remove(ID_DEPARTMENT).apply()
    }

    fun saveImage(image: String) {
        prefs.edit().putString(IMAGE, image).apply()
    }

    fun getImage(): String? {
        return prefs.getString(IMAGE, null)
    }

    fun clearImage() {
        prefs.edit().remove(IMAGE).apply()
    }

    companion object {
        private const val AUTH_TOKEN = "AUTH_TOKEN"
        private const val IMAGE = "IMAGE"
        private const val USER_ID = "USER_ID"
        private const val USER_ROLE = "USER_ROLE"
        private const val ID_DEPARTMENT = "ID_DEPARTMENT"
    }
}
