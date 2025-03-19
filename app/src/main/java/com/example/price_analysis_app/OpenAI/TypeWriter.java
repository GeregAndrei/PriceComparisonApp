package com.example.price_analysis_app.OpenAI;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;

public class TypeWriter extends androidx.appcompat.widget.AppCompatTextView {
    private CharSequence mText = "";
    private int mIndex = 0;
    private long mDelay = 50; // delay in ms between characters

    private Handler mHandler = new Handler();

    private Runnable characterAdder = new Runnable() {
        @Override
        public void run() {
            setText(mText.subSequence(0, mIndex++));
            if (mIndex <= mText.length()) {
                mHandler.postDelayed(characterAdder, mDelay);
            }
        }
    };

    public TypeWriter(Context context) {
        super(context);
    }

    public TypeWriter(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void animateText(CharSequence text) {
        mText = text;
        mIndex = 0;
        setText("");
        mHandler.removeCallbacks(characterAdder);
        mHandler.postDelayed(characterAdder, mDelay);
    }

    public void setCharacterDelay(long millis) {
        mDelay = millis;
    }
}
