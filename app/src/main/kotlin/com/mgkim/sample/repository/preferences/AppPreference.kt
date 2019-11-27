package com.mgkim.sample.repository.preferences

import android.content.Context

class AppPreference(context: Context) : BasePreference(context) {
    override fun preferenceName(): String = PREFERENCE_NAME_APP

    companion object {
        const val PREFERENCE_NAME_APP = "preference_name_app"

        const val PREFERENCE_KEY_B_DEBUG_ENABLE = "preference_key_b_debug_enable"

        const val PREFERENCE_KEY_S_TOKEN = "preference_key_s_token"
    }
}