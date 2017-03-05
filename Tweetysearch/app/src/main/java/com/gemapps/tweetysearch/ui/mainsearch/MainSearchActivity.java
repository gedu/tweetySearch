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

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.FrameLayout;

import com.gemapps.tweetysearch.R;
import com.gemapps.tweetysearch.networking.TwitterSearchManager;
import com.gemapps.tweetysearch.networking.searchquery.RecentlySearchedItem;
import com.gemapps.tweetysearch.networking.searchquery.UrlParameter;
import com.gemapps.tweetysearch.ui.butter.ButterActivity;
import com.gemapps.tweetysearch.ui.resultsearch.ResultSearchActivity;
import com.gemapps.tweetysearch.ui.resultsearch.ResultSearchFragment;
import com.gemapps.tweetysearch.ui.state.ViewStateManager;
import com.gemapps.tweetysearch.util.Util;

import butterknife.BindBool;
import butterknife.BindView;

public class MainSearchActivity extends ButterActivity
        implements MainSearchFragment.OnSearchListener {

    private static final String TAG = "MainSearchActivity";
    private static final String SEARCH_FRAGMENT_TAG = "tweety.SEARCH_FRAGMENT_TAG";
    private static final String RESULT_FRAGMENT_TAG = "tweety.RESULT_FRAGMENT_TAG";

    @Nullable
    @BindView(R.id.result_fragment_container)
    FrameLayout mResultContainer;

    @BindBool(R.bool.is_sw600_land)
    boolean isLargeLandScreen;
    @BindBool(R.bool.is_phone)
    boolean mIsPhone;

    private ResultSearchFragment mResultFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");
        setContentView(R.layout.activity_main_search);
        setViewStates();
        ViewStateManager.getInstance().setDualPanelState(isDualPanel());
        checkViewStateContent();
        addSearchContent(savedInstanceState);
    }

    public boolean isDualPanel() {
        return mResultContainer != null;
    }

    private void setViewStates() {
        ViewStateManager.getInstance().initState(this);
    }

    private void checkViewStateContent() {
        if (!isLargeLandScreen && ViewStateManager.getInstance().hasToSwapContext()) {
            startResultActivity();
        }
    }

    private void addSearchContent(Bundle savedInstanceState) {
        if (savedInstanceState == null) setFirstState();
        else setResultPanel();
    }

    private void setFirstState() {
        setSearchPanel();
        setResultPanel();
    }

    private void setSearchPanel() {
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.main_fragment_container,
                        MainSearchFragment.newInstance(),
                        SEARCH_FRAGMENT_TAG)
                .commit();
    }

    private void setResultPanel() {
        if (!isDualPanel()) {
            Fragment fragment = getResultFragment();
            if(fragment != null) getSupportFragmentManager().beginTransaction().remove(fragment).commit();
            return;
        }

        mResultFragment = ResultSearchFragment.newInstance();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.result_fragment_container,
                        mResultFragment,
                        RESULT_FRAGMENT_TAG)
                .commit();

    }

    private boolean isResultInLayout() {
        Fragment fragment = getResultFragment();
        return fragment != null && fragment.isInLayout();
    }

    private ResultSearchFragment getResultFragment() {
        return (ResultSearchFragment) getSupportFragmentManager()
                .findFragmentByTag(RESULT_FRAGMENT_TAG);
    }

    @Override
    public void onSearchedItemClicked(RecentlySearchedItem searchedItem) {
        startResultActivity();
        TwitterSearchManager.getInstance().search(searchedItem);
    }

    @Override
    public void onSearch(UrlParameter urlParameter) {
        TwitterSearchManager.getInstance().search(urlParameter);
        startResultActivity();
    }

    private void startResultActivity() {
        if (!isDualPanel()) {
            if (Util.isLollipop()) {
                startActivity(new Intent(this, ResultSearchActivity.class),
                        ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
            }else{
                Intent intent = new Intent(this, ResultSearchActivity.class);
                startActivity(intent);
            }
        } else {
            mResultFragment.showProgressBar();
        }
    }
}
