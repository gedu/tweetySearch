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
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.SearchView;
import android.util.AttributeSet;
import android.view.LayoutInflater;

import com.gemapps.tweetysearch.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by edu on 2/23/17.
 */

public class PillSearchView extends LinearLayoutCompat {

    @BindView(R.id.word_search_view)
    SearchView mWordSearchView;

    public PillSearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PillSearchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.pill_search_view, this);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.query_search_button)
    public void onClick() {
    }
}
