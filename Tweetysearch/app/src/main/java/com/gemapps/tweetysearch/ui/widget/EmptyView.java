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

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import com.gemapps.tweetysearch.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by edu on 3/3/17.
 */

public class EmptyView extends LinearLayoutCompat {

    @BindView(R.id.empty_title_view)
    TextView mEmptyTitleView;
    @BindView(R.id.empty_msg_view)
    TextView mEmptyMsgView;
    @BindView(R.id.empty_image)
    ImageView mImageView;

    private String mTitle;
    private String mMessage;
    private Drawable mEmptyImage;

    public EmptyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0, 0);
    }

    public EmptyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr, 0);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        setOrientation(VERTICAL);
        LayoutInflater.from(context).inflate(R.layout.empty_view, this);
        ButterKnife.bind(this);
        final TypedArray typedArray = context.obtainStyledAttributes(attrs,
                R.styleable.EmptyView, defStyleAttr, defStyleRes);

        try{
            mTitle = typedArray.getString(R.styleable.EmptyView_emptyTitle);
            mMessage = typedArray.getString(R.styleable.EmptyView_emptyMessage);
            mEmptyImage = typedArray.getDrawable(R.styleable.EmptyView_emptyIcon);
        }finally {
            typedArray.recycle();
        }
        ;
        mEmptyTitleView.setText(isTitleEmpty() ? "Sorry tweety flew away": mTitle);
        mEmptyMsgView.setText(isMessageEmpty() ? "" : mMessage);
        if(mImageView != null) {
            mImageView.setImageDrawable(mEmptyImage);
            invalidate();
        }
    }

    private boolean isTitleEmpty(){
        return mTitle.length() == 0;
    }

    private boolean isMessageEmpty(){
        return mMessage.length() == 0;
    }
}
