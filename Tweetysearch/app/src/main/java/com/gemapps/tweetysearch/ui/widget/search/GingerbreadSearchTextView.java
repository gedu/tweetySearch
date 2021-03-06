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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.gemapps.tweetysearch.R;

/**
 * Created by edu on 3/3/17.
 */

public class GingerbreadSearchTextView implements SearchTextAction {

    private EditText mEditText;
    private InputMethodManager mInputManager;

    @Override
    public void init(Context context, View rootView) {
        mInputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        mEditText = (EditText) rootView.findViewById(R.id.search_edit_text);
        mEditText.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
    }

    @Override
    public void addSearchActionListener(final SearchTextActionListener listener) {
        mEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if(actionId == EditorInfo.IME_ACTION_SEARCH){
                    listener.onSearchAction();
                    mInputManager.hideSoftInputFromWindow(mEditText.getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                    return true;
                }

                return false;
            }
        });
    }

    @Override
    public void addSearchWatcherListener(final SearchTextWatchListener listener) {
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                listener.onTextWatched(count+start);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public String getText() {
        return mEditText.getText().toString();
    }

    @Override
    public boolean isContentValid() {
        int size = getAmountOfWords();
        return size > MIN_AMOUNT_OF_WORDS && size <= MAX_AMOUNT_OF_WORDS;
    }

    private int getAmountOfWords(){
        if(getText().length() == 0) return 0;
        String[] words = getText().split(" ");
        return words.length;
    }

    @Override
    public void setText(String text) {
        mEditText.setText(text);
    }

    @Override
    public int getErrorText() {
        int size = getAmountOfWords();
        if(size <= MIN_AMOUNT_OF_WORDS) return EMPTY_ERROR_TEXT;
        else if (size > MAX_AMOUNT_OF_WORDS) return EXCEEDED_ERROR_TEXT;
        else return COMPLEXITY_ERROR_TEXT;
    }
}
