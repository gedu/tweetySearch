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
import android.os.Build;
import android.support.annotation.StringRes;
import android.view.View;

/**
 * Created by edu on 3/3/17.
 */

public class SearchViewWrapper {

    private static SearchTextAction mSearchTextView;
    static {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
            mSearchTextView = new JellybeanSearchTextView();
        else
            mSearchTextView = new GingerbreadSearchTextView();
    }

    public SearchViewWrapper(Context context, View rootView){
        mSearchTextView.init(context, rootView);
    }

    public void addSearchActionListener(SearchTextAction.SearchTextActionListener listener){
        mSearchTextView.addSearchActionListener(listener);
    }

    public void addSearchWatcherListener(SearchChipView searchChipView){
        mSearchTextView.addSearchWatcherListener(searchChipView);
    }

    public String getText(){
        return mSearchTextView.getText();
    }

    public boolean isContentValid(){
        return mSearchTextView.isContentValid();
    }

    public void setText(String text){
        mSearchTextView.setText(text);
    }

    public @StringRes int getErrorText(){
        return mSearchTextView.getErrorText();
    }
}
