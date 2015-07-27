package com.switchamajig.visibleletters;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.DialogPreference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by pweaver on 7/24/15.
 */
public class LetterStylePreference {
    public static String getFontFor(Context context, String letter) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        String defaultTitle = context.getString(R.string.pref_title_default);
        String defaultFont = sharedPrefs.getString(
                getFontKeyForLetter(context, defaultTitle),
                context.getString(R.string.default_font));
        String fontString =
                sharedPrefs.getString(getFontKeyForLetter(context, letter), defaultFont);
        return (fontString.equals(defaultTitle)) ? defaultFont : fontString;
    }

    private static String getFontKeyForLetter(Context context, String letter) {
        return String.format(context.getString(R.string.font_key_format), letter);
    }

    public static int getSizeFor(Context context, String letter) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        String defaultSize = sharedPrefs.getString(
                getSizeKeyForLetter(context, context.getString(R.string.pref_title_default)),
                context.getString(R.string.default_size));
        String sizeString =
                sharedPrefs.getString(getSizeKeyForLetter(context, letter), defaultSize);
        try {
            return Integer.decode(sizeString);
        } catch (NumberFormatException e) {
            /* Handles weird corruption of prefs and case when pref is set to default */
            return Integer.decode(defaultSize);
        }
    }

    private static String getSizeKeyForLetter(Context context, String letter) {
        return String.format(context.getString(R.string.size_key_format), letter);
    }

    public static int getColorFor(Context context, String letter) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        String defaultColor = sharedPrefs.getString(
                getColorKeyForLetter(context, context.getString(R.string.pref_title_default)),
                context.getString(R.string.default_color));
        String colorString =
                sharedPrefs.getString(getColorKeyForLetter(context, letter), defaultColor);
        try {
            /* Handles weird corruption of prefs and case when pref is set to default */
            return Long.decode(colorString).intValue();
        } catch (NumberFormatException e) {
            return Long.decode(defaultColor).intValue();
        }
    }

    private static String getColorKeyForLetter(Context context, String letter) {
        return String.format(context.getString(R.string.color_key_format), letter);
    }
}
