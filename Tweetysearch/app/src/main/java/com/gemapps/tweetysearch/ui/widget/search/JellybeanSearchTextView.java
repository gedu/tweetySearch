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
import android.content.SharedPreferences;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.gemapps.tweetysearch.R;
import com.gemapps.tweetysearch.util.Util;
import com.hootsuite.nachos.NachoTextView;
import com.hootsuite.nachos.chip.Chip;

import static android.view.View.GONE;
import static com.hootsuite.nachos.terminator.ChipTerminatorHandler.BEHAVIOR_CHIPIFY_ALL;

/**
 * Created by edu on 3/3/17.
 */

public class JellybeanSearchTextView implements SearchTextAction {

    private static final String TAG = "JellybeanSearchTextView";
    private static final String TUTORIAL_COUNT_PREF = "tweety.TUTORIAL_COUNT_PREF";
    private static final int TUTORIAL_MAX_COUNT = 3;
    private NachoTextView mSearchText;
    private View mTutorialSearch;
    private InputMethodManager mInputManager;

    @Override
    public void init(Context context, View rootView) {
        mInputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        mSearchText = (NachoTextView) rootView.findViewById(R.id.search_edit_text);
        mTutorialSearch = rootView.findViewById(R.id.search_tutorial_text);
        setupSearchText();
        setupTutorialText(context);
    }

    private void setupSearchText(){
        mSearchText.addChipTerminator(',', BEHAVIOR_CHIPIFY_ALL);
        mSearchText.setChipTextColorResource(R.color.white);
        mSearchText.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
    }

    private void setupTutorialText(Context context){
        SharedPreferences sharedPreferences = Util.getPrivatePreferences(context);
        int count = sharedPreferences.getInt(TUTORIAL_COUNT_PREF, 0);
        if(count > TUTORIAL_MAX_COUNT){
            mTutorialSearch.setVisibility(GONE);
        }else{
            count++;
            sharedPreferences.edit().putInt(TUTORIAL_COUNT_PREF, count).apply();
        }
    }

    @Override
    public void addSearchActionListener(final SearchTextActionListener listener) {
        mSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH){
                    listener.onSearchAction();
                    mInputManager.hideSoftInputFromWindow(mSearchText.getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
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
                listener.onTextWatched(count+start);
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
