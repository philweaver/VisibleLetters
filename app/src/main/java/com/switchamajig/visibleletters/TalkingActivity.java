package com.switchamajig.visibleletters;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

/**
 * Activity that speaks letters and words as they are typed
 */
public class TalkingActivity extends MainActivity implements TextToSpeech.OnInitListener {
    private static String LOG_TAG = "TalkingActivity";
    private Set<String> dictionary = new HashSet<>();
    private boolean ttsInitialized;
    private TextToSpeech tts;
    private int lastTextLength = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tts = new TextToSpeech(this, this);
        if (tts == null) {
            Log.d("TalkingActivity", "tts is null");
        }
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(
                    new InputStreamReader(getAssets().open("google-10000-english-usa.txt")));
            String receiveString;
            while ((receiveString = reader.readLine()) != null) {
                dictionary.add(receiveString.trim().toLowerCase());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d(LOG_TAG, "dictionary contains " + dictionary.size() + " words.");
        ((EditText) findViewById(R.id.text_display_view)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int cursorPosition =
                        ((EditText) findViewById(R.id.text_display_view)).getSelectionStart();
                if (ttsInitialized && (tts != null) && (cursorPosition > 0)
                        && (s.length() > lastTextLength)) {
                    char newChar = s.charAt(cursorPosition - 1);
                    if ((newChar == ' ') || (newChar == '\n')) {
                        String currentWord = getCurrentWord().toLowerCase();
                        if (dictionary.contains(currentWord)) {
                            tts.speak(currentWord, TextToSpeech.QUEUE_FLUSH, null, "id");
                            Log.d(LOG_TAG, "Trying to say word " + currentWord);
                        } else {
                            Log.d(LOG_TAG, currentWord + " not in dictionary");
                        }
                    } else {
                        String keyString = String.format("%c", newChar);
                        tts.speak(keyString, TextToSpeech.QUEUE_FLUSH, null, "id");
                        Log.d(LOG_TAG, "Trying to say letter " + keyString);
                    }
                }
                lastTextLength = s.length();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    @Override
    protected void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }

    @Override
    public void onInit(int status) {
        ttsInitialized = true;
        Log.d("TalkingActivity", "TTS initialized with status " + status);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_talking, menu);
        return true;    }

    String getCurrentWord() {
        EditText editText = (EditText) findViewById(R.id.text_display_view);
        int cursorPosition = editText.getSelectionStart();
        if (cursorPosition <=0 ) {
            return null;
        }
        CharSequence currentText = editText.getText().subSequence(0, cursorPosition);
        String[] words = TextUtils.split(currentText.toString(), "\\s");
        if (words.length < 2) {
            return null;
        }
        String currentWord = words[words.length - 2];
        return currentWord.trim();
    }
}
