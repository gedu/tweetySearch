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

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gemapps.tweetysearch.R;
import com.gemapps.tweetysearch.util.Util;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by edu on 2/28/17.
 */

public class SearchChipView extends LinearLayoutCompat
        implements SearchTextAction.SearchTextWatchListener {

    private static final String TAG = "SearchChipView";

    @BindView(R.id.error_search_text)
    TextView mErrorSearchText;
    @BindView(R.id.search_icon_image)
    ImageView mSearchIcon;
    @BindView(R.id.search_close_btn)
    ImageView mSearchClearButton;

    SearchViewWrapper mViewWrapper;
    private boolean mIsShowingError;
    private boolean mIsShowingGlass;

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
        mIsShowingGlass = true;
        View rootView = LayoutInflater.from(context)
                .inflate(R.layout.chip_search_view, this, true);
        ButterKnife.bind(this);

        mViewWrapper = new SearchViewWrapper(context, rootView);
        setupLollipopView();
    }

    private void setupLollipopView(){
        if(Util.isLollipop()) {
            mViewWrapper.addSearchWatcherListener(this);
            mSearchClearButton.setVisibility(GONE);
        } else mSearchClearButton.setVisibility(VISIBLE);
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

    public void addSearchActionListener(SearchTextAction.SearchTextActionListener listener){
        mViewWrapper.addSearchActionListener(listener);
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
        clearSearchText();
    }

    @OnClick(R.id.search_icon_image)
    public void onIconClicked(){
        clearSearchText();
    }

    private void clearSearchText(){
        hideErrorLabel();
        mViewWrapper.setText("");
    }

    private Drawable getSearchIconDrawable(boolean isGlass){
        int mDrawable = isGlass ? R.drawable.ic_search_black_24px : R.drawable.ic_close_black;
        if(!Util.isLollipop()){
            return getResources().getDrawable(mDrawable);
        }else{
            return ContextCompat.getDrawable(getContext(), mDrawable);
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onTextWatched(int count) {
        AnimatedVectorDrawable drawable;
        if(count > 0 && mIsShowingGlass){
            mIsShowingGlass = false;
            drawable = (AnimatedVectorDrawable) ContextCompat.getDrawable(getContext(), R.drawable.avd_search);
            mSearchIcon.setImageDrawable(drawable);
            drawable.start();
            mSearchIcon.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mSearchIcon.setImageDrawable(getSearchIconDrawable(mIsShowingGlass));
                }
            }, 1000L);
        }else if(count == 0 && !mIsShowingGlass){
            mIsShowingGlass = true;
            drawable = (AnimatedVectorDrawable) ContextCompat.getDrawable(getContext(), R.drawable.avd_close);
            mSearchIcon.setImageDrawable(drawable);
            drawable.start();
            //todo fix: on some android/devices the animation doesn't finished, set the drawable at the end
            mSearchIcon.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mSearchIcon.setImageDrawable(getSearchIconDrawable(mIsShowingGlass));
                }
            }, 1000L);
        }
        invalidate();
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
