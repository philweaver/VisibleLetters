package com.switchamajig.visibleletters;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.EditText;

/**
 * Edit text that filters keystrokes, speaking the letters but not typing them unless a function key
 * is pressed after the letter.
 */
public class TwoKeystrokeTypingEditText extends EditText {
    private static final boolean DEBUG = false;

    KeyEvent lastKeyDownEvent;
    private TextToSpeech tts;
    private boolean doubleKeyHit = false;

    public TwoKeystrokeTypingEditText(Context context) {
        super(context);
    }

    public TwoKeystrokeTypingEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TwoKeystrokeTypingEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (!doubleKeyHit) {
            return super.onKeyDown(keyCode, event);
        }

        if (isFunctionKey(keyCode)) {
            sendLastEvent();
        } else {
            if (tts != null) {
                String characterDescription = Character.toString(event.getDisplayLabel());
                if (keyCode == KeyEvent.KEYCODE_SPACE) {
                    characterDescription = "space";
                }
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    characterDescription = "enter";
                }
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    characterDescription = "backspace";
                }

                tts.speak(characterDescription, TextToSpeech.QUEUE_FLUSH, null, "id");
            }
            lastKeyDownEvent = new KeyEvent(event);
        }
        return true;
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (!doubleKeyHit) {
            return super.onKeyUp(keyCode, event);
        }
        return true;
    }

    public void setTts(TextToSpeech tts) {
        this.tts = tts;
    }

    public void toggleDoubleKeyHit() {
        doubleKeyHit = !doubleKeyHit;
    }

    private void sendLastEvent() {
        if (lastKeyDownEvent == null) {
            return;
        }
        super.onKeyDown(lastKeyDownEvent.getKeyCode(), lastKeyDownEvent);
        KeyEvent keyUpEvent = KeyEvent.changeAction(lastKeyDownEvent, KeyEvent.ACTION_UP);
        super.onKeyUp(keyUpEvent.getKeyCode(), keyUpEvent);
        lastKeyDownEvent = null;
    }

    private boolean isFunctionKey(int keyCode) {
        if (DEBUG && (keyCode == KeyEvent.KEYCODE_SEMICOLON)) return true;
        return (keyCode == KeyEvent.KEYCODE_F1) || (keyCode == KeyEvent.KEYCODE_F2)
                || (keyCode == KeyEvent.KEYCODE_F3) || (keyCode == KeyEvent.KEYCODE_F4)
                || (keyCode == KeyEvent.KEYCODE_F5) || (keyCode == KeyEvent.KEYCODE_F6)
                || (keyCode == KeyEvent.KEYCODE_F7) || (keyCode == KeyEvent.KEYCODE_F8)
                || (keyCode == KeyEvent.KEYCODE_F9) || (keyCode == KeyEvent.KEYCODE_F10)
                || (keyCode == KeyEvent.KEYCODE_F11) || (keyCode == KeyEvent.KEYCODE_F12);
    }


}
