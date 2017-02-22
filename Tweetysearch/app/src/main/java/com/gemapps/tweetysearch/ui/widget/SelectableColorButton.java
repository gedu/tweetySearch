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
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;

import com.gemapps.tweetysearch.R;

import butterknife.BindColor;
import butterknife.ButterKnife;

/**
 * Created by edu on 2/21/17.
 */

public abstract class SelectableColorButton extends Button {
    private static final String TAG = "ColoredButton";

    private boolean mIsSelected;

    @BindColor(R.color.disable_dark)
    int IDLE_COLOR;
    @BindColor(R.color.colorAccent)
    int SELECTED_COLOR;

    public SelectableColorButton(Context context) {
        super(context);
        init();
    }

    public SelectableColorButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SelectableColorButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SelectableColorButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init(){
        ButterKnife.bind(this);
        mIsSelected = false;
        setTextColor(IDLE_COLOR);
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mIsSelected = !mIsSelected;
                setSelected(mIsSelected);
                onClicked();
            }
        });
    }

    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);
        setTextColor(mIsSelected ? SELECTED_COLOR : IDLE_COLOR);
    }

    protected abstract void onClicked();
}
