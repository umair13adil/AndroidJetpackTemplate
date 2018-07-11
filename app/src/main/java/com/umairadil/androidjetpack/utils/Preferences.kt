package com.umairadil.androidjetpack.utils

import android.content.Context
import android.content.SharedPreferences
import io.reactivex.annotations.NonNull
import java.util.*

/**
 * SharedPreferences Utilities

 */
class Preferences {

    companion object {

        @NonNull
        private val ourInstance = Preferences()

        @NonNull
        fun getInstance(): Preferences {
            return ourInstance
        }
    }

    /**
     * The name of SharedPreferences
     */
    private val PREFERENCE_NAME = "sp_jetpack"

    /**
     * Get the instance of SharedPreferences

     * @param context
     * *
     * @return
     */
    fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
    }

    /**
     * Save Single String preference

     * @param context
     * *
     * @param key
     * *
     * @param value
     * *
     * @return
     */
    fun save(context: Context, key: String, value: String): Boolean {
        val sharedPreferences = getSharedPreferences(context)
        val editor = sharedPreferences.edit()
        editor.putString(key, value)
        return editor.commit()
    }

    /**
     * Get string value through key

     * @param context
     * *
     * @param key
     * *
     * @return
     */
    fun getString(context: Context, key: String): String {
        val sharedPreferences = getSharedPreferences(context)
        return sharedPreferences.getString(key, "")
    }

    /**
     * Save map String preference

     * @param context
     * *
     * @param valuesMap
     * *
     * @return
     */
    fun save(context: Context, valuesMap: HashMap<String, String>): Boolean {
        val sharedPreferences = getSharedPreferences(context)
        val editor = sharedPreferences.edit()
        var value = ""
        for (key in valuesMap.keys) {
            value = valuesMap[key].toString()
            editor.putString(key, value)
        }
        return editor.commit()
    }

    /**
     * Save single boolean preference

     * @param context
     * *
     * @param key
     * *
     * @param value
     * *
     * @return
     */
    fun save(context: Context, key: String, value: Boolean): Boolean {
        val sharedPreferences = getSharedPreferences(context)
        val editor = sharedPreferences.edit()
        editor.putBoolean(key, value)
        return editor.commit()
    }

    /**
     * Get boolean value through key

     * @param context
     * *
     * @param key
     * *
     * @param defFlag
     * *
     * @return
     */
    fun getBoolean(context: Context, key: String, defaultValue: Boolean): Boolean {
        val sharedPreferences = getSharedPreferences(context)
        return sharedPreferences.getBoolean(key, defaultValue)
    }

    /**
     * Clean the SharedPreferences

     * @param context
     */
    fun clear(context: Context) {
        val sharedPreferences = getSharedPreferences(context)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.commit()
    }

    /**
     * remove the value by key

     * @param context
     * *
     * @param key
     */
    fun remove(context: Context, key: String) {
        val sharedPreferences = getSharedPreferences(context)
        val editor = sharedPreferences.edit()
        editor.remove(key)
        editor.commit()
    }

    /**
     * Save int value

     * @param context
     * *
     * @param key
     * *
     * @param value
     * *
     * @return
     */
    fun save(context: Context, key: String, value: Int): Boolean {
        val sharedPreferences = getSharedPreferences(context)
        val editor = sharedPreferences.edit()
        editor.putInt(key, value)
        return editor.commit()
    }

    /**
     * Get int value

     * @param context
     * *
     * @param key
     * *
     * @param defaultValue
     * *
     * @return
     */
    fun getInt(context: Context, key: String, defaultValue: Int): Int {
        val sharedPreferences = getSharedPreferences(context)
        return sharedPreferences.getInt(key, defaultValue)
    }

    /**
     * Save float value

     * @param context
     * *
     * @param key
     * *
     * @param value
     * *
     * @return
     */
    fun save(context: Context, key: String, value: Float): Boolean {
        val sharedPreferences = getSharedPreferences(context)
        val editor = sharedPreferences.edit()
        editor.putFloat(key, value)
        return editor.commit()
    }

    /**
     * Get float value

     * @param context
     * *
     * @param key
     * *
     * @param defaultValue
     * *
     * @return
     */
    fun getFloat(context: Context, key: String, defaultValue: Float): Float {
        val sharedPreferences = getSharedPreferences(context)
        return sharedPreferences.getFloat(key, defaultValue)
    }

    fun save(context: Context, key: String, value: Set<String>?): Boolean {
        if (value == null) return false
        val sharedPreferences = getSharedPreferences(context)
        val editor = sharedPreferences.edit()
        editor.putStringSet(key, value)
        return editor.commit()
    }

    fun getStringSet(context: Context, key: String, defaultValue: Set<String>): Set<String> {
        val sharedPreferences = getSharedPreferences(context)
        return sharedPreferences.getStringSet(key, defaultValue)
    }

}
