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
import android.util.AttributeSet;
import android.view.LayoutInflater;

import com.gemapps.tweetysearch.R;
import com.hootsuite.nachos.NachoTextView;
import com.hootsuite.nachos.chip.Chip;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.hootsuite.nachos.terminator.ChipTerminatorHandler.BEHAVIOR_CHIPIFY_ALL;

/**
 * Created by edu on 2/28/17.
 */

public class SearchChipView extends LinearLayoutCompat {

    private static final String TAG = "SearchChipView";

    @BindView(R.id.search_edit_text)
    NachoTextView mSearchText;

    public SearchChipView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SearchChipView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        LayoutInflater.from(context)
                .inflate(R.layout.chip_search_view, this, true);
        ButterKnife.bind(this);
        mSearchText.addChipTerminator(',', BEHAVIOR_CHIPIFY_ALL);
        mSearchText.setChipTextColorResource(R.color.white);
    }

    public String getText(){
        mSearchText.chipifyAllUnterminatedTokens();
        StringBuilder builder = new StringBuilder();
        for (Chip chip : mSearchText.getAllChips()) {
            builder.append(chip.getText());
            builder.append(" ");
        }
        return builder.toString().trim();
    }

    @OnClick(R.id.search_close_btn)
    public void onCloseClicked(){
        mSearchText.setText("");
    }
}
