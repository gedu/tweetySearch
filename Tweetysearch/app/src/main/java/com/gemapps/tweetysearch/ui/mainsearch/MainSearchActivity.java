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
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.FrameLayout;

import com.gemapps.tweetysearch.R;
import com.gemapps.tweetysearch.networking.TwitterSearchManager;
import com.gemapps.tweetysearch.networking.connectionreceiver.NetworkStateReceiver;
import com.gemapps.tweetysearch.networking.model.AuthResponseBridge;
import com.gemapps.tweetysearch.networking.searchquery.RecentlySearchedItem;
import com.gemapps.tweetysearch.networking.searchquery.UrlParameter;
import com.gemapps.tweetysearch.ui.butter.ButterActivity;
import com.gemapps.tweetysearch.ui.detailsearch.DetailSearchActivity;
import com.gemapps.tweetysearch.ui.mainsearch.presenter.MainFragmentContract;
import com.gemapps.tweetysearch.ui.model.TweetItem;
import com.gemapps.tweetysearch.ui.resultsearch.ResultSearchActivity;
import com.gemapps.tweetysearch.ui.resultsearch.ResultSearchFragment;
import com.gemapps.tweetysearch.ui.state.ViewStateManager;
import com.gemapps.tweetysearch.util.Util;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindBool;
import butterknife.BindView;

public class MainSearchActivity extends ButterActivity
        implements MainFragmentContract.OnSearchListener,
        ResultSearchFragment.ResultSearchListener {

    private static final String TAG = "MainSearchActivity";
    private static final String SEARCH_FRAGMENT_TAG = "tweety.SEARCH_FRAGMENT_TAG";
    private static final String RESULT_FRAGMENT_TAG = "tweety.RESULT_FRAGMENT_TAG";

    private static final IntentFilter mNetworkStateFilter =
            new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
    private static final BroadcastReceiver mNetworkStateReceiver = new NetworkStateReceiver();

    @BindView(R.id.activity_main_search)
    CoordinatorLayout mCoordinatorLayout;

    @Nullable
    @BindView(R.id.result_fragment_container)
    FrameLayout mResultContainer;

    @BindBool(R.bool.is_sw600_land)
    boolean isLargeLandScreen;
    @BindBool(R.bool.is_phone)
    boolean mIsPhone;

    private Snackbar mSnackbar;

    private ResultSearchFragment mResultFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_search);
        setViewStates();
        ViewStateManager.getInstance().setDualPanelState(isDualPanel());
        checkViewStateContent();
        addSearchContent(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mNetworkStateReceiver, mNetworkStateFilter);
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAuthResponseEvent(AuthResponseBridge auth){
        switch(auth.getType()){
            case AuthResponseBridge.SUCCESS: dismissAuthSnackbar();
                break;
            case AuthResponseBridge.FAILED: showAuthSnackbar(R.string.login_failed_error_msg,
                    R.string.try_again);
                break;
            case AuthResponseBridge.LOGIN_REQUIRED: showAuthSnackbar(R.string.login_required_error_msg,
                    R.string.login);
                break;
            case AuthResponseBridge.LOGIN_IN_PROGRESS: showAuthSnackbar(R.string.authentication_in_progress,
                    null);
        }
    }

    private void dismissAuthSnackbar(){
        if(mSnackbar != null) mSnackbar.dismiss();
    }

    private void showAuthSnackbar(@StringRes int msg, @Nullable @StringRes Integer actionLabel) {
        dismissAuthSnackbar();
        mSnackbar = Snackbar.make(mCoordinatorLayout, msg, BaseTransientBottomBar.LENGTH_INDEFINITE);
        if(actionLabel != null) {
            mSnackbar.setAction(actionLabel, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TwitterSearchManager.getInstance().authenticate();
                }
            });
        }
        mSnackbar.show();
    }

    @Override
    protected void onPause() {
        unregisterReceiver(mNetworkStateReceiver);
        super.onPause();
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
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
        if(!TwitterSearchManager.getInstance().isAuthenticated())return;

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

    @Override
    public void onImageClicked(View v, TweetItem tweet) {
        if (Util.isLollipop()) {
            startActivity(DetailSearchActivity.getInstance(v, tweet.bundleMainContent(), DetailSearchActivity.class),
                    ActivityOptions.makeSceneTransitionAnimation(this, v,
                            getResources().getString(R.string.tweet_pic_trans_name)).toBundle());
        }else{
            startActivity(DetailSearchActivity.getInstance(v, DetailSearchActivity.class));
        }
    }
}
