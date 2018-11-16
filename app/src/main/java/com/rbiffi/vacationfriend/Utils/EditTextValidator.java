package com.rbiffi.vacationfriend.Utils;

import android.view.View;
import android.widget.TextView;

public abstract class EditTextValidator implements View.OnFocusChangeListener {

    private final TextView textView;

    public EditTextValidator(TextView textView) {
        this.textView = textView;
    }

    protected abstract void validate(TextView textView, String text);


    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (!hasFocus) {
            TextView textView = (TextView) v;
            String text = textView.getText().toString();
            validate(textView, text);
        }
    }
}
