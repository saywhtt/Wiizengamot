package edu.born.flicility

import android.content.Context
import android.preference.PreferenceManager

const val PREFERENCE_LAST_RESULT_ID = "lastResultId"

fun getLastResultId(context: Context) = PreferenceManager.getDefaultSharedPreferences(context)
        .getString(PREFERENCE_LAST_RESULT_ID, null)

fun setLastResultId(context: Context, lastResultId: String) = PreferenceManager.getDefaultSharedPreferences(context)
        .edit()
        .putString(PREFERENCE_LAST_RESULT_ID, lastResultId)
        .apply()