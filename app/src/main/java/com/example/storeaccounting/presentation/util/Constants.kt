package com.example.storeaccounting.presentation.util

import androidx.datastore.preferences.core.stringPreferencesKey

object Constants {
     const val FROM = "from"
     const val UNTIL = "until"
     const val DAY = "day"
     const val MONTH = "month"
     const val YEAR = "year"

     const val TEN_DAYS_GRAPH = "10_days_graph"
     const val THIRTY_DAYS_GRAPH = "30_days_graph"

     const val CREDIT_CARD_ID = "credit_card_id"

     const val USER_PREFERENCES = "user_preferences"
     val THEME_KEY = stringPreferencesKey("theme_key")

}