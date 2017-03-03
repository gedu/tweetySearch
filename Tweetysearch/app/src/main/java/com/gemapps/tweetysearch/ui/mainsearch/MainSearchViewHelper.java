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

import android.support.annotation.StringRes;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.gemapps.tweetysearch.R;
import com.gemapps.tweetysearch.networking.searchquery.UrlParameter;
import com.gemapps.tweetysearch.ui.widget.ResultTypeButtonSelectable;
import com.gemapps.tweetysearch.ui.widget.search.SearchChipView;

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
    private static final int LIST_ORIENTATION = LinearLayoutManager.VERTICAL;
    @BindView(R.id.recent_search_button)
    ResultTypeButtonSelectable mRecentSearchButton;
    @BindView(R.id.popular_search_button)
    ResultTypeButtonSelectable mPopularSearchButton;
    @BindView(R.id.word_search_view)
    SearchChipView mSearchView;
    @BindView(R.id.recently_searched_recycler)
    RecyclerView mRecyclerView;

    public MainSearchViewHelper(View root){
        ButterKnife.bind(this, root);
        setupSearchedRecycler();
    }

    private void setupSearchedRecycler(){
        mRecyclerView.setLayoutManager(getLinearLayout());
        mRecyclerView.addItemDecoration(getDivider());
    }

    private LinearLayoutManager getLinearLayout(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mRecyclerView.getContext(),
                LIST_ORIENTATION, false);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        return linearLayoutManager;
    }

    private DividerItemDecoration getDivider(){
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                LIST_ORIENTATION);
        dividerItemDecoration.setDrawable(mRecyclerView.getContext()
                .getResources().getDrawable(R.drawable.divider));
        return dividerItemDecoration;
    }

    public boolean isSearchTextValid(){
        return mSearchView.isContentValid();
    }

    public @StringRes int getSearchErrorText(){
        return mSearchView.getErrorText();
    }

    public void showErrorSearchLabel(){
        mSearchView.showErrorLabel();
    }

    public void hideErrorSearchLabel(){
        mSearchView.hideErrorLabel();
    }

    public void setRecentlySearchAdapter(RecentlySearchedAdapter adapter){
        mRecyclerView.setAdapter(adapter);
    }

    public void setResultTypeBuilderToButtons(UrlParameter.Builder builder){
        mRecentSearchButton.setResultTypeBuilder(builder);
        mPopularSearchButton.setResultTypeBuilder(builder);
    }

    public String getTextToSearch(){
        return mSearchView.getText();
    }
}
