package com.switchamajig.visibleletters;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;


public class MainActivity extends Activity implements TextWatcher,
        SharedPreferences.OnSharedPreferenceChangeListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EditText editText = (EditText) findViewById(R.id.text_entry_view);
        editText.addTextChangedListener(this);
        PreferenceManager.getDefaultSharedPreferences(this)
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        TextView displayView = (TextView) findViewById(R.id.text_display_view);
        SpannableStringBuilder stringBuilder = new SpannableStringBuilder();

        for (int i = 0; i < s.length(); ++i) {
            char[] currentChar = {s.charAt(i)};
            TextAppearanceSpan span = getTextAppearanceForChar(currentChar[0]);
            stringBuilder.append(new String(currentChar), span, Spanned.SPAN_INTERMEDIATE);
        }
        displayView.setText(stringBuilder);
    }

    private TextAppearanceSpan getTextAppearanceForChar(char forChar) {
        char[] charArray = {forChar};
        String forCharString = new String(charArray);
        String font = LetterStylePreference.getFontFor(this, forCharString);
        int size = LetterStylePreference.getSizeFor(this, forCharString);
        int color = LetterStylePreference.getColorFor(this, forCharString);
        return new TextAppearanceSpan(font,
                0 /* Style */,
                size,
                ColorStateList.valueOf(color),
                ColorStateList.valueOf(color));
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        EditText editText = (EditText) findViewById(R.id.text_entry_view);
        afterTextChanged(editText.getText());
    }
}
