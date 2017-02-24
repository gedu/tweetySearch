/*
 *    Copyright 2017 Edu Graciano
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.gemapps.tweetysearch.ui.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.TextView;

import com.gemapps.tweetysearch.R;

/**
 * Created by edu on 2/23/17.
 */

public class MultiChipsEditText extends EditText {

    public MultiChipsEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MultiChipsEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MultiChipsEditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context){

        addTextChangedListener(mTextWatcher);
    }

    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(count > 0){
                if(s.charAt(start) == ',') setChips();
            }
        }

        @Override
        public void afterTextChanged(Editable s) {}
    };

    private void setChips() {

        if(getText().toString().contains(",")) {
            SpannableStringBuilder builder = new SpannableStringBuilder(getText());
            String[] chips = getText().toString().trim().split(",");
            int x = 0;
            LayoutInflater inflater = LayoutInflater.from(getContext());
            for (String word : chips) {

                TextView chipView = (TextView) inflater.inflate(R.layout.chip_text, null);
                chipView.setText(word);
                int spec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
                chipView.measure(spec, spec);
                chipView.layout(0, 0, chipView.getMeasuredWidth(), chipView.getMeasuredHeight());
                Bitmap chipBitmap = Bitmap.createBitmap(chipView.getMeasuredWidth()+100,
                        chipView.getMeasuredHeight()*2, Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(chipBitmap);
                canvas.translate(-chipView.getScrollX(), -chipView.getScrollY());
                chipView.draw(canvas);
                chipView.setDrawingCacheEnabled(true);
                Bitmap cachedBitmap = chipView.getDrawingCache();
                Bitmap viewBitmap = cachedBitmap.copy(Bitmap.Config.ARGB_8888, true);
                chipView.destroyDrawingCache();
                BitmapDrawable chipDrawable = new BitmapDrawable(viewBitmap);
                chipDrawable.setBounds(0, 0, chipDrawable.getIntrinsicWidth(), chipDrawable.getIntrinsicHeight());
                builder.setSpan(new ImageSpan(chipDrawable),
                        x, x + word.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                x = x + word.length() + 1;
            }

            setText(builder);
            setSelection(getText().length());
        }
    }
}
