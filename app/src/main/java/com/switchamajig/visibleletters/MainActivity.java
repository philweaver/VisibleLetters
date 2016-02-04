package com.switchamajig.visibleletters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.TextAppearanceSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


public class MainActivity extends Activity implements TextWatcher,
        SharedPreferences.OnSharedPreferenceChangeListener {

    private int indexOfChange;
    private int changeCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final EditText editText = (EditText) findViewById(R.id.text_display_view);
        editText.addTextChangedListener(this);
        Intent intent = getIntent();
        if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            File file = new File(intent.getData().getPath());
            try {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                StringBuilder stringBuilder = new StringBuilder();
                String receiveString;
                while ((receiveString = reader.readLine()) != null) {
                    stringBuilder.append(receiveString);
                    stringBuilder.append("\n");
                }
                editText.setText(stringBuilder, null);
            } catch (FileNotFoundException e) {
            } catch (IOException e) {
            }
        }
        PreferenceManager.getDefaultSharedPreferences(this)
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onDestroy() {
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
        super.onDestroy();
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

        if (id == R.id.move_letters) {
            Intent launchMoveLettersIntent =
                    new Intent(MainActivity.this, LetterMovingActivity.class);
            EditText editText = (EditText) findViewById(R.id.text_display_view);
            launchMoveLettersIntent.putExtra(
                    LetterMovingActivity.EXTRA_TEXT_TO_MOVE, editText.getText().toString());
            startActivity(launchMoveLettersIntent);
            return true;
        }

        if (id == R.id.speak_when_typing) {
            startActivity(new Intent(MainActivity.this, TalkingActivity.class));
            return true;
        }

        if (id == R.id.main_menu) {
            startActivity(new Intent(MainActivity.this, MainActivity.class));
            return true;
        }



        return super.onOptionsItemSelected(item);
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        indexOfChange = start;
        changeCount = count;
    }

    @Override
    public void afterTextChanged(Editable s) {
        while (changeCount-- > 0) {
            char[] currentChar = {s.charAt(indexOfChange + changeCount)};
            TextAppearanceSpan span = getTextAppearanceForChar(this, currentChar[0]);
            s.setSpan(span, indexOfChange + changeCount, indexOfChange + changeCount + 1, Spanned.SPAN_INTERMEDIATE);
        }
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
        EditText editText = (EditText) findViewById(R.id.text_display_view);
        Editable text = editText.getText();
        onTextChanged(text, 0, text.length(), text.length());
        afterTextChanged(text);
        editText.setText(text, null);
    }
}
