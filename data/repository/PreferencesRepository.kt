package org.zapomni.venturers.data.repository

import android.content.SharedPreferences
import com.google.gson.Gson
import org.zapomni.venturers.domain.model.TripFeedbackCollector
import org.zapomni.venturers.domain.model.User
import org.zapomni.venturers.domain.model.navigation.AuthNavigation
import org.zapomni.venturers.domain.model.navigation.TripFeedbackNavigation
import org.zapomni.venturers.domain.model.navigation.TripNavigation

class PreferencesRepository constructor(private val preferences: SharedPreferences,
                                        private val gson: Gson) {

    companion object {
        const val KEY_REFRESH_TOKEN = "KEY_REFRESH_TOKEN"
        const val KEY_ACCESS_TOKEN = "KEY_ACCESS_TOKEN"
        const val KEY_AUTH_STEP = "KEY_AUTH_STEP"
        const val KEY_USER = "KEY_USER"
        const val KEY_USER_ID_SAW_PHONE = "KEY_USER_ID_SAW_PHONE"
        const val KEY_SHOW_TIPS = "KEY_SHOW_TIPS"
        const val KEY_TRIP_NAVIGATION = "KEY_TRIP_NAVIGATION"
        const val KEY_TRIP_FEEDBACK_COLLECTOR = "KEY_TRIP_FEEDBACK_COLLECTOR"
        const val KEY_TRIP_FEEDBACK_NAVIGATOR = "KEY_TRIP_FEEDBACK_NAVIGATOR"
    }

    var refreshToken: String?
        get() = preferences.getString(KEY_REFRESH_TOKEN, null)
        set(value) {
            preferences.edit()
                    .putString(KEY_REFRESH_TOKEN, value)
                    .apply()
        }

    var accessToken: String?
        get() = preferences.getString(KEY_ACCESS_TOKEN, null)
        set(value) {
            preferences.edit()
                    .putString(KEY_ACCESS_TOKEN, value)
                    .apply()
        }

    var authNavigation: AuthNavigation?
        get() {
            val step = preferences.getString(KEY_AUTH_STEP, null)
            return when (step) {
                AuthNavigation.PHONE.toString() -> AuthNavigation.PHONE
                AuthNavigation.PROFILE.toString() -> AuthNavigation.PROFILE
                else -> null
            }
        }
        set(value) {
            preferences.edit()
                    .putString(KEY_AUTH_STEP, value.toString())
                    .apply()
        }

    var user: User?
        get() = gson.fromJson(preferences.getString(KEY_USER, null), User::class.java)
        set(value) {
            preferences.edit()
                    .putString(KEY_USER, gson.toJson(value))
                    .apply()
        }

    var userIdSawPhone: String?
        get() = preferences.getString(KEY_USER_ID_SAW_PHONE, null)
        set(value) {
            preferences.edit()
                    .putString(KEY_USER_ID_SAW_PHONE, value)
                    .apply()
        }

    var showTips: Boolean
        get() = preferences.getBoolean(KEY_SHOW_TIPS, true)
        set(value) {
            preferences.edit()
                    .putBoolean(KEY_SHOW_TIPS, value)
                    .apply()
        }

    var tripNavigation: TripNavigation
        get() {
            val nav = preferences.getString(KEY_TRIP_NAVIGATION, null)
            return when (nav) {
                TripNavigation.NO_TRIP.toString() -> TripNavigation.NO_TRIP
                TripNavigation.FIRST.toString() -> TripNavigation.FIRST
                TripNavigation.SECOND.toString() -> TripNavigation.SECOND
                TripNavigation.THIRD.toString() -> TripNavigation.THIRD
                else -> TripNavigation.NO_TRIP
            }
        }
        set(value) {
            preferences.edit()
                    .putString(KEY_TRIP_NAVIGATION, value.toString())
                    .apply()
        }

    var tripFeedbackCollector: TripFeedbackCollector
        get() = gson.fromJson(preferences.getString(KEY_TRIP_FEEDBACK_COLLECTOR, "{}"), TripFeedbackCollector::class.java)
        set(value) {
            preferences.edit()
                    .putString(KEY_TRIP_FEEDBACK_COLLECTOR, gson.toJson(value))
                    .apply()
        }

    var tripFeedbackNavigation: TripFeedbackNavigation
        get() {
            val nav = preferences.getString(KEY_TRIP_FEEDBACK_NAVIGATOR, null)
            return when (nav) {
                TripFeedbackNavigation.EVENT.toString() -> TripFeedbackNavigation.EVENT
                TripFeedbackNavigation.CURATOR.toString() -> TripFeedbackNavigation.CURATOR
                TripFeedbackNavigation.REVIEW.toString() -> TripFeedbackNavigation.REVIEW
                TripFeedbackNavigation.LUCK.toString() -> TripFeedbackNavigation.LUCK
                TripFeedbackNavigation.BONUS.toString() -> TripFeedbackNavigation.BONUS
                TripFeedbackNavigation.LUCK.toString() -> TripFeedbackNavigation.LUCK
                else -> TripFeedbackNavigation.EVENT
            }
        }
        set(value) {
            preferences.edit()
                    .putString(KEY_TRIP_FEEDBACK_NAVIGATOR, value.toString())
                    .apply()
        }

    fun clearUserData() {
        preferences.edit()
                .remove(KEY_ACCESS_TOKEN)
                .remove(KEY_REFRESH_TOKEN)
                .remove(KEY_AUTH_STEP)
                .remove(KEY_USER)
                .remove(KEY_TRIP_NAVIGATION)
                .remove(KEY_TRIP_FEEDBACK_COLLECTOR)
                .remove(KEY_TRIP_FEEDBACK_NAVIGATOR)
                .apply()
    }

    fun clearFeedback() {
        preferences.edit()
                .remove(KEY_TRIP_FEEDBACK_NAVIGATOR)
                .remove(KEY_TRIP_FEEDBACK_COLLECTOR)
                .remove(KEY_TRIP_NAVIGATION)
                .apply()
    }
}