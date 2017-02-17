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
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gemapps.tweetysearch.R;
import com.gemapps.tweetysearch.networking.TwitterSearchManager;
import com.gemapps.tweetysearch.networking.model.NetworkResponseBridge;
import com.gemapps.tweetysearch.ui.butter.ButterFragment;
import com.gemapps.tweetysearch.ui.model.TweetCollection;
import com.gemapps.tweetysearch.ui.model.TweetItem;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import io.realm.RealmList;

/**
 * Use the {@link ResultSearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ResultSearchFragment extends ButterFragment {

    private static final String TAG = "ResultSearchFragment";
    private static final int LOAD_WINDOW_COUNT = 3;

    @BindView(R.id.result_loading_bar)
    ContentLoadingProgressBar mLoadingBar;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mRefreshLayout;
    @BindView(R.id.result_recycler_view)
    RecyclerView mResultView;

    private ResultRecyclerAdapter mAdapter;
    private boolean mIsLoadingMore = false;
    private Handler mHandler = new Handler(Looper.getMainLooper());

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
        mLoadingBar.show();
        mResultView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mResultView.setAdapter(mAdapter);
        mResultView.addOnScrollListener(mScrollListener);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.d(TAG, "onRefresh");
                TwitterSearchManager.getInstance().loadNew();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNetworkResponseEvent(NetworkResponseBridge response){
        TweetCollection tweetCollection = (TweetCollection) response.getContent();
        switch(response.getType()){
            case NetworkResponseBridge.TWEETS_SEARCH: addNewTweets(tweetCollection.getTweetItems());
                break;
            case NetworkResponseBridge.TWEETS_LOAD_MORE: handleLoadMoreTweets(tweetCollection.getTweetItems());
                break;
            case NetworkResponseBridge.TWEETS_LOAD_NEW: handleLoadNewTweets(tweetCollection.getTweetItems());
                break;
        }
        mLoadingBar.hide();
    }

    private void addNewTweets(RealmList<TweetItem> tweets) {
        mAdapter.addTweetsAtEnd(tweets);
    }

    private void handleLoadMoreTweets(RealmList<TweetItem> tweets){
        mIsLoadingMore = false;
        mAdapter.removeProgressItem();
        addNewTweets(tweets);
    }

    private void handleLoadNewTweets(RealmList<TweetItem> tweets){
        mRefreshLayout.setRefreshing(false);
        mAdapter.addTweetsAtStart(tweets);
        mResultView.smoothScrollToPosition(0);
    }

    private final RecyclerView.OnScrollListener mScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            if(dy > 0){
                int tweetsAmount = mResultView.getLayoutManager().getItemCount();
                int lastVisibleItem = ((LinearLayoutManager)mResultView.getLayoutManager())
                        .findLastVisibleItemPosition();

                //how many of tweets should have below the current scroll position before loading more
                if(!mIsLoadingMore && tweetsAmount <= (lastVisibleItem + LOAD_WINDOW_COUNT)){
                    loadMoreTweets();
                }

            }
        }
    };

    private void loadMoreTweets(){
        mHandler.post(mLoadMoreRunnable);
    }

    private final Runnable mLoadMoreRunnable = new Runnable() {
        @Override
        public void run() {
            mIsLoadingMore = true;
            mAdapter.addProgressItem();
            TwitterSearchManager.getInstance().loadMore();
        }
    };

}
