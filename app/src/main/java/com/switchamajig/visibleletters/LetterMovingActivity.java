package com.switchamajig.visibleletters;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.TextAppearanceSpan;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsoluteLayout;
import android.widget.TextView;


public class LetterMovingActivity extends Activity implements View.OnTouchListener {
    public static final String EXTRA_TEXT_TO_MOVE = "com.switchamajig.visibleletters_TEXT_TO_MOVE";

    private int viewBeingDraggedId;
    private float lastTouchX, lastTouchY;
    private boolean currentlyDragging = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_letter_moving);
        AbsoluteLayout absLayout = (AbsoluteLayout) findViewById(R.id.letter_movement_area);
        Intent intent = getIntent();
        String lettersToMove = intent.getStringExtra(EXTRA_TEXT_TO_MOVE);
        int startCoordinate = 0;
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int screenWidth = size.x;
        for (Character character : lettersToMove.toCharArray()) {
            TextView characterView = new TextView(this);
            characterView.setOnTouchListener(this);
            SpannableStringBuilder stringBuilder = new SpannableStringBuilder();
            TextAppearanceSpan span = MainActivity.getTextAppearanceForChar(this, character);
            stringBuilder.append(character.toString(), span, Spanned.SPAN_INTERMEDIATE);
            characterView.setText(stringBuilder);

            AbsoluteLayout.LayoutParams layoutParams = new AbsoluteLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    startCoordinate,
                    0);
            absLayout.addView(characterView, layoutParams);
            startCoordinate += screenWidth / (lettersToMove.length() + 1);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_letter_moving, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int pointerIndex = event.getActionIndex();
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                /* Drag the first View in this event */
                currentlyDragging = true;
                viewBeingDraggedId = event.getPointerId(pointerIndex);
                lastTouchX = event.getRawX();
                lastTouchY = event.getRawY();
                return true;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                currentlyDragging = false;
                return true;
            case MotionEvent.ACTION_POINTER_UP:
                // Stop dragging when first view is released
                if (viewBeingDraggedId == event.getPointerId(pointerIndex)) {
                    currentlyDragging = false;
                }
                return true;
            case MotionEvent.ACTION_MOVE:
                if (currentlyDragging
                        && (viewBeingDraggedId == event.getPointerId(pointerIndex))) {
                    float deltaX = event.getRawX() - lastTouchX;
                    int intDeltaX = (int) deltaX;
                    float deltaY = event.getRawY() - lastTouchY;
                    int intDeltaY = (int) deltaY;
                    lastTouchX = lastTouchX + intDeltaX;
                    lastTouchY = lastTouchY + intDeltaY;
                    AbsoluteLayout.LayoutParams layoutParams =
                            (AbsoluteLayout.LayoutParams) v.getLayoutParams();
                    layoutParams.x += intDeltaX;
                    layoutParams.y += intDeltaY;
                    v.setLayoutParams(layoutParams);
                }
                return true;
        }
        return false;
    }
}
