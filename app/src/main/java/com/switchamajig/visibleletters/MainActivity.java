package com.switchamajig.visibleletters;

import android.app.Activity;
import android.content.Context;
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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class MainActivity extends Activity implements TextWatcher,
        SharedPreferences.OnSharedPreferenceChangeListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final EditText editText = (EditText) findViewById(R.id.text_entry_view);
        editText.addTextChangedListener(this);
        PreferenceManager.getDefaultSharedPreferences(this)
                .registerOnSharedPreferenceChangeListener(this);
        Button moveLettersButton = (Button) findViewById(R.id.move_text_button);
        moveLettersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent launchMoveLettersIntent =
                        new Intent(MainActivity.this, LetterMovingActivity.class);
                launchMoveLettersIntent.putExtra(
                        LetterMovingActivity.EXTRA_TEXT_TO_MOVE, editText.getText().toString());
                startActivity(launchMoveLettersIntent);
            }
        });
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
            TextAppearanceSpan span = getTextAppearanceForChar(this, currentChar[0]);
            stringBuilder.append(new String(currentChar), span, Spanned.SPAN_INTERMEDIATE);
        }
        displayView.setText(stringBuilder);
    }

    /* TODO(pweaver) Move to utils class */
    public static TextAppearanceSpan getTextAppearanceForChar(Context context, char forChar) {
        char[] charArray = {forChar};
        String forCharString = new String(charArray);
        String font = LetterStylePreference.getFontFor(context, forCharString);
        int size = LetterStylePreference.getSizeFor(context, forCharString);
        int color = LetterStylePreference.getColorFor(context, forCharString);
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
