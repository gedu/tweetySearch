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

package com.gemapps.tweetysearch.ui.resultsearch;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gemapps.tweetysearch.R;
import com.gemapps.tweetysearch.networking.TwitterSearchManager;
import com.gemapps.tweetysearch.networking.model.NetworkResponseBridge;
import com.gemapps.tweetysearch.ui.butter.ButterFragment;
import com.gemapps.tweetysearch.ui.mainsearch.MainSearchActivity;
import com.gemapps.tweetysearch.ui.model.TweetCollection;
import com.gemapps.tweetysearch.ui.model.TweetItem;
import com.gemapps.tweetysearch.util.RealmUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import io.realm.RealmList;

/**
 * Use the {@link ResultSearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ResultSearchFragment extends ButterFragment
        implements ResultViewHelper.ResultViewListener {

    private static final String TAG = "ResultSearchFragment";

    private ResultRecyclerAdapter mAdapter;
    private ResultViewHelper mViewHelper;
    private boolean mIsDualPanel;

    public ResultSearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment ResultSearchFragment.
     */
    public static ResultSearchFragment newInstance() {
        return new ResultSearchFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mIsDualPanel = false;
        mAdapter = new ResultRecyclerAdapter(getActivity(), new RealmList<TweetItem>());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return createView(inflater, container, R.layout.fragment_result_search);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewHelper = new ResultViewHelper(view);
        mViewHelper.setAdapter(mAdapter);
        setupDualPanelHint();
    }

    private void setupDualPanelHint(){
        mIsDualPanel = isDualPanel();
        if(mIsDualPanel) mViewHelper.showDualPanelHint();
    }

    private boolean isDualPanel(){
        return getActivity() instanceof MainSearchActivity &&
                ((MainSearchActivity)getActivity()).isDualPanel();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        mViewHelper.setResultListener(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        mViewHelper.setResultListener(null);
        super.onStop();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNetworkResponseEvent(NetworkResponseBridge response){
        TweetCollection tweetCollection = (TweetCollection) response.getContent();
        switch(response.getType()){
            case NetworkResponseBridge.TWEETS_SEARCH: RealmUtil.saveSearchResult(tweetCollection);
            case NetworkResponseBridge.TWEETS_SEARCH_NOT_SAVE: addNewTweets(tweetCollection.getTweetItems());
                break;
            case NetworkResponseBridge.TWEETS_LOAD_MORE: handleLoadMoreTweets(tweetCollection.getTweetItems());
                break;
            case NetworkResponseBridge.TWEETS_LOAD_NEW: handleLoadNewTweets(tweetCollection.getTweetItems());
                break;
            case NetworkResponseBridge.TWEETS_LOAD_ERROR: mViewHelper.showErrorMessage();
                break;
            case NetworkResponseBridge.TWEETS_EMPTY_SEARCH: mViewHelper.showEmptyView();
        }
        mViewHelper.hideHintAndProgress();
    }

    private void addNewTweets(RealmList<TweetItem> tweets) {
        mViewHelper.hideErrorMessage();
        mAdapter.addTweetsAtEnd(tweets);
    }

    private void handleLoadMoreTweets(RealmList<TweetItem> tweets){
        mViewHelper.hidedLoadMoreProgress();
        mAdapter.removeProgressItem();
        addNewTweets(tweets);
    }

    private void handleLoadNewTweets(RealmList<TweetItem> tweets){
        mViewHelper.hideErrorMessage();
        mAdapter.addTweetsAtStart(tweets);
        mViewHelper.hideLoadNewOnceProgress();
    }

    public void showProgressBar(){
        mViewHelper.showProgressBar();
    }

    @Override
    public void onLoadMore() {
        mAdapter.addProgressItem();
        TwitterSearchManager.getInstance().loadMore();
    }

    @Override
    public void onTryAgain() {
        TwitterSearchManager.getInstance().reTryLastSearch();
    }
}
