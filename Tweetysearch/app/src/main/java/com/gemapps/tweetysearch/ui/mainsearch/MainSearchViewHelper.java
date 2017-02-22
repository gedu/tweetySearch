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

package com.gemapps.tweetysearch.ui.mainsearch;

import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;

import com.gemapps.tweetysearch.R;
import com.gemapps.tweetysearch.networking.searchquery.UrlParameter;
import com.gemapps.tweetysearch.ui.widget.ResultTypeButtonSelectable;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by edu on 2/13/17.
 */

public class MainSearchViewHelper {

    private static final String TAG = "MainSearchViewHelper";
    public interface SearchClickListener {
        void onSearchClicked();
    }

    @BindView(R.id.recent_search_button)
    ResultTypeButtonSelectable mRecentSearchButton;
    @BindView(R.id.popular_search_button)
    ResultTypeButtonSelectable mPopularSearchButton;
    @BindView(R.id.word_search_view)
    SearchView mSearchView;

    private SearchClickListener mSearchClickListener;

    public MainSearchViewHelper(View root){
        ButterKnife.bind(this, root);
        mSearchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick");
                if(mSearchClickListener != null) mSearchClickListener.onSearchClicked();
            }
        });
    }

    public void addSearchClickListener(SearchClickListener listener){
        mSearchClickListener = listener;
    }

    public void setResultTypeBuilderToButtons(UrlParameter.Builder builder){
        mRecentSearchButton.setResultTypeBuilder(builder);
        mPopularSearchButton.setResultTypeBuilder(builder);
    }

    public String getTextToSearch(){
        String text = mSearchView.getQuery().toString();
        mSearchView.setQuery("", false);
        return text;
    }
}
