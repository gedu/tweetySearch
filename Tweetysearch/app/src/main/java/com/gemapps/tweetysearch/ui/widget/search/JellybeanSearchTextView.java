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
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.gemapps.tweetysearch.R;
import com.hootsuite.nachos.NachoTextView;
import com.hootsuite.nachos.chip.Chip;

import static com.hootsuite.nachos.terminator.ChipTerminatorHandler.BEHAVIOR_CHIPIFY_ALL;

/**
 * Created by edu on 3/3/17.
 */

public class JellybeanSearchTextView implements SearchTextAction {

    private static final String TAG = "JellybeanSearchTextView";
    private NachoTextView mSearchText;

    @Override
    public void init(Context context, View rootView) {
        mSearchText = (NachoTextView) rootView.findViewById(R.id.search_edit_text);
        mSearchText.addChipTerminator(',', BEHAVIOR_CHIPIFY_ALL);
        mSearchText.setChipTextColorResource(R.color.white);
        mSearchText.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
    }

    @Override
    public void addSearchActionListener(final SearchTextActionListener listener) {
        mSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH){
                    listener.onSearchAction();
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void addSearchWatcherListener(final SearchTextWatchListener listener) {
        mSearchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d(TAG, "onTextChanged: COUNT: "+count);
                listener.onTextWatched(count);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public String getText() {
        mSearchText.chipifyAllUnterminatedTokens();
        StringBuilder builder = new StringBuilder();
        for (Chip chip : mSearchText.getAllChips()) {
            builder.append(chip.getText());
            builder.append(" ");
        }
        return builder.toString().trim();
    }

    @Override
    public boolean isContentValid() {
        int size = getAmountOfChips();
        return size > MIN_AMOUNT_OF_WORDS && size <= MAX_AMOUNT_OF_WORDS;
    }

    private int getAmountOfChips(){
        mSearchText.chipifyAllUnterminatedTokens();
        return mSearchText.getAllChips().size();
    }

    @Override
    public void setText(String text) {
        mSearchText.setText(text);
    }

    @Override
    public int getErrorText() {
        int size = getAmountOfChips();
        if(size <= MIN_AMOUNT_OF_WORDS) return EMPTY_ERROR_TEXT;
        else if (size > MAX_AMOUNT_OF_WORDS) return EXCEEDED_ERROR_TEXT;
        else return COMPLEXITY_ERROR_TEXT;
    }
}
