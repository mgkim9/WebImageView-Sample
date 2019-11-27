package com.mgkim.sample.repository.preferences

import android.content.Context
import android.content.SharedPreferences
import com.mgkim.sample.repository.preferences.PreferencesHelper.get
import com.mgkim.sample.repository.preferences.PreferencesHelper.set

/**
 * BasePreference
 * @author : mgkim
 * @version : 1.0.0
 * @since : 2019-11-27 오후 7:09
 **/
abstract class BasePreference(context: Context) {

    abstract fun preferenceName(): String

    val preference: SharedPreferences

    init {
        preference = PreferencesHelper.custom(context, preferenceName())
    }

    fun getBoolean(key: String): Boolean? = preference[key]

    fun getBoolean(key: String, defaultValue: Boolean): Boolean = preference[key, defaultValue]!!

    fun setBoolean(key: String, value: Boolean?) {
        preference[key] = value
    }

    fun getInt(key: String): Int? = preference[key]

    fun getInt(key: String, defaultValue: Int): Int = preference[key, defaultValue]!!

    fun setInt(key: String, value: Int?) {
        preference[key] = value
    }

    fun getFloat(key: String): Float? = preference[key]

    fun getFloat(key: String, defaultValue: Float): Float = preference[key, defaultValue]!!

    fun setFloat(key: String, value: Float?) {
        preference[key] = value
    }

    fun getLong(key: String): Long? = preference[key]

    fun getLong(key: String, defaultValue: Long): Long = preference[key, defaultValue]!!

    fun setLong(key: String, value: Long?) {
        preference[key] = value
    }

    fun getString(key: String): String? = preference[key]

    fun getString(key: String, defaultValue: String): String = preference[key, defaultValue]!!

    fun setString(key: String, value: String?) {
        preference[key] = value
    }
}