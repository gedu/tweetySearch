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
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.StringRes;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.gemapps.tweetysearch.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by edu on 2/28/17.
 */

public class SearchChipView extends LinearLayoutCompat {

    private static final String TAG = "SearchChipView";

    @BindView(R.id.error_search_text)
    TextView mErrorSearchText;
    SearchViewWrapper mViewWrapper;
    private boolean mIsShowingError;

    public SearchChipView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SearchChipView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        mIsShowingError = false;
        View rootView = LayoutInflater.from(context)
                .inflate(R.layout.chip_search_view, this, true);
        ButterKnife.bind(this);
        mViewWrapper = new SearchViewWrapper(context, rootView);
    }

    @Override
    public Parcelable onSaveInstanceState() {
        SearchChipSavedState selectableSavedState = new SearchChipSavedState(super.onSaveInstanceState());
        selectableSavedState.isShowingError = mIsShowingError;
        return selectableSavedState;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        if(state instanceof SearchChipSavedState){
            SearchChipSavedState chipSavedState = (SearchChipSavedState) state;
            super.onRestoreInstanceState(chipSavedState.getSuperState());
            mIsShowingError = chipSavedState.isShowingError;
            if(mIsShowingError) showErrorLabel();
        }else {
            super.onRestoreInstanceState(state);
        }
    }

    public boolean isContentValid(){
        return mViewWrapper.isContentValid();
    }

    public String getText(){
        return mViewWrapper.getText();
    }

    public void showErrorLabel(){
        mIsShowingError = true;
        mErrorSearchText.setText(mViewWrapper.getErrorText());
        mErrorSearchText.setVisibility(VISIBLE);
    }

    public void hideErrorLabel(){
        mIsShowingError = false;
        mErrorSearchText.setVisibility(GONE);
    }

    public @StringRes int getErrorText(){
        return mViewWrapper.getErrorText();
    }

    @OnClick(R.id.search_close_btn)
    public void onCloseClicked(){
        mViewWrapper.setText("");
    }

    public static class SearchChipSavedState extends BaseSavedState {

        boolean isShowingError;

        public SearchChipSavedState(Parcelable superState) {
            super(superState);
        }

        public SearchChipSavedState(Parcel source) {
            super(source);
            isShowingError = (source.readInt() == 0);
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(isShowingError ? 1 : 0);
        }

        public static final Creator<SearchChipSavedState> CREATOR = new Creator<SearchChipSavedState>() {
            public SearchChipSavedState createFromParcel(Parcel in) {
                return new SearchChipSavedState(in);
            }

            public SearchChipSavedState[] newArray(int size) {
                return new SearchChipSavedState[size];
            }
        };
    }
}
