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

package com.gemapps.tweetysearch.ui.widget.search;

import android.content.Context;
import android.support.annotation.StringRes;
import android.view.View;

import com.gemapps.tweetysearch.R;

/**
 * Created by edu on 3/3/17.
 */

public interface SearchTextAction {

    interface SearchTextActionListener {
        void onSearchAction();
    }

    static final int MAX_AMOUNT_OF_WORDS = 10;
    static final int MIN_AMOUNT_OF_WORDS = 0;
    static final int EMPTY_ERROR_TEXT = R.string.search_text_empty_error;
    static final int EXCEEDED_ERROR_TEXT = R.string.search_text_exceeded_error;
    static final int COMPLEXITY_ERROR_TEXT = R.string.search_text_complexity_error;

    void init(Context context, View rootView);
    void addSearchActionListener(SearchTextActionListener listener);
    String getText();
    boolean isContentValid();
    void setText(String text);
    @StringRes int getErrorText();
}