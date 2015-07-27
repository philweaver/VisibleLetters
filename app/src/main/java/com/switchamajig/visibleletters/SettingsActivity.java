package com.switchamajig.visibleletters;

import android.app.Activity;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;

/**
 * Created by pweaver on 7/24/15.
 */
public class SettingsActivity extends PreferenceActivity {
    private static String stringWithAllChars =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890-+=";

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        PreferenceManager preferenceManager = getPreferenceManager();

        PreferenceScreen mainScreen = preferenceManager.createPreferenceScreen(this);

        /* Set up default separately */
        PreferenceScreen defaultScreen = preferenceManager.createPreferenceScreen(this);
        String prefTitleDefault = getString(R.string.pref_title_default);
        defaultScreen.setTitle(prefTitleDefault);
        defaultScreen.setSummary(getString(R.string.pref_summary_default));

        ListPreference defaultFont = new ListPreference(this);
        defaultFont.setTitle(getString(R.string.font_pref_title));
        defaultFont.setKey(String.format(getString(R.string.font_key_format), prefTitleDefault));
        defaultFont.setEntries(R.array.fonts);
        defaultFont.setEntryValues(R.array.fonts);
        defaultScreen.addPreference(defaultFont);

        ListPreference defaultSize = new ListPreference(this);
        defaultSize.setTitle(getString(R.string.size_pref_title));
        defaultSize.setKey(String.format(getString(R.string.size_key_format), prefTitleDefault));
        defaultSize.setEntries(R.array.sizes);
        defaultSize.setEntryValues(R.array.sizes);
        defaultScreen.addPreference(defaultSize);

        ListPreference defaultColor = new ListPreference(this);
        defaultColor.setTitle(getString(R.string.color_pref_title));
        defaultColor.setKey(String.format(getString(R.string.color_key_format), prefTitleDefault));
        defaultColor.setEntries(R.array.color_entries);
        defaultColor.setEntryValues(R.array.color_values);
        defaultScreen.addPreference(defaultColor);

        mainScreen.addPreference(defaultScreen);

        /* Set up for all characters */
        for (char thisChar : stringWithAllChars.toCharArray()) {
            PreferenceScreen screen = preferenceManager.createPreferenceScreen(this);
            char[] charInArray = {thisChar};
            String charString = new String(charInArray);
            screen.setTitle(charString);

            ListPreference font = new ListPreference(this);
            font.setTitle(getString(R.string.font_pref_title));
            font.setKey(String.format(getString(R.string.font_key_format), charString));
            font.setEntries(R.array.fonts_with_default);
            font.setEntryValues(R.array.fonts_with_default);
            screen.addPreference(font);

            ListPreference size = new ListPreference(this);
            size.setTitle(getString(R.string.size_pref_title));
            size.setKey(String.format(getString(R.string.size_key_format), charString));
            size.setEntries(R.array.sizes_with_default);
            size.setEntryValues(R.array.sizes_with_default);
            screen.addPreference(size);

            ListPreference color = new ListPreference(this);
            color.setTitle(getString(R.string.color_pref_title));
            color.setKey(String.format(getString(R.string.color_key_format), charString));
            color.setEntries(R.array.color_entries_with_default);
            color.setEntryValues(R.array.color_values_with_default);
            screen.addPreference(color);

            mainScreen.addPreference(screen);
        }

        setPreferenceScreen(mainScreen);
    }
}
