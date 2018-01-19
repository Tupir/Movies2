package com.example.android.moviesremake.settings

import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.preference.CheckBoxPreference
import android.support.v7.preference.ListPreference
import android.support.v7.preference.Preference
import android.support.v7.preference.PreferenceFragmentCompat
import com.example.android.moviesremake.R

/**
 * The SettingsFragment serves as the display for all of the user's settings.
 *
 * You need to modify build gradle by compile 'com.android.support:preference-v7:25.0.1'
 * create fragment from activity_settings
 * create new xml file, where all settings parts will be created
 * add correct preferene into styles: <item name="preferenceTheme">@style/PreferenceThemeOverlay</item>
 *
 *
 */
class SettingsFragment : PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener {    // for setting change

    // method will accept params and sets them
    private fun setPreferenceSummary(preference: Preference, value: Any?) {
        val stringValue = value!!.toString()
        val key = preference.key

        if (preference is ListPreference) {
            /* For list preferences, look up the correct display value in */
            /* the preference's 'entries' list (since they have separate labels/values). */
            val prefIndex = preference.findIndexOfValue(stringValue)
            if (prefIndex >= 0) {
                preference.setSummary(preference.entries[prefIndex])
            }
        } else {
            // For other preferences, set the summary to the value's simple string representation.
            preference.summary = stringValue
        }
    }


    // adds xml file to the settings
    override fun onCreatePreferences(bundle: Bundle?, s: String?) {
        /* Add 'general' preferences, defined in the XML file */
        addPreferencesFromResource(R.xml.pref_general)

        val sharedPreferences = preferenceScreen.sharedPreferences
        val prefScreen = preferenceScreen
        val count = prefScreen.preferenceCount
        for (i in 0 until count) {
            val p = prefScreen.getPreference(i)
            if (p !is CheckBoxPreference) {
                val value = sharedPreferences.getString(p.key, "")
                setPreferenceSummary(p, value)
            }
        }
    }

    //  Unregister SettingsFragment (this) as a SharedPreferenceChangedListener in onStop
    override fun onStop() {
        super.onStop()
        /* Unregister the preference change listener */
        preferenceScreen.sharedPreferences
                .unregisterOnSharedPreferenceChangeListener(this)
    }


    //  Register SettingsFragment (this) as a SharedPreferenceChangedListener in onStart
    override fun onStart() {
        super.onStart()
        /* Register the preference change listener */
        preferenceScreen.sharedPreferences
                .registerOnSharedPreferenceChangeListener(this)
    }


    // Override onSharedPreferenceChanged to update non CheckBoxPreferences when they are changed
    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        val preference = findPreference(key)
        if (null != preference) {
            if (preference !is CheckBoxPreference) {
                setPreferenceSummary(preference, sharedPreferences.getString(key, ""))
            }
        }
    }
}