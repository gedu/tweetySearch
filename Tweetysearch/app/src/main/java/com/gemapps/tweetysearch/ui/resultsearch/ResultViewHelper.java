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

import android.os.Handler;
import android.os.Looper;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewStub;
import android.widget.ProgressBar;

import com.gemapps.tweetysearch.R;
import com.gemapps.tweetysearch.networking.TwitterSearchManager;
import com.gemapps.tweetysearch.ui.widget.NoConnectionHelper;

import butterknife.BindInt;
import butterknife.BindView;
import butterknife.ButterKnife;

import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

/**
 * Created by edu on 3/1/17.
 */

public class ResultViewHelper {

    public interface ResultViewListener {
        void onLoadMore();
        void onTryAgain();
    }

    private static final int LOAD_WINDOW_COUNT = 3;

    @BindView(R.id.result_loading_bar)
    ProgressBar mLoadingBar;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mRefreshLayout;
    @BindView(R.id.result_recycler_view)
    RecyclerView mResultView;

    @BindView(R.id.no_connection_stub)
    ViewStub mNoConnectionStub;
    @BindView(R.id.empty_list_stub)
    ViewStub mEmptyListStub;
    @BindView(R.id.search_hint_stub)
    ViewStub mDualPanelHint;

    @BindInt(R.integer.result_grid_span_count)
    int mSpanCount;

    private NoConnectionHelper mNoConnectionHelper;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private boolean mIsLoadingMore = false;
    private NoConnectionHelper.TryAgainListener mTryAgainListener;
    private ResultViewListener mListener;

    public ResultViewHelper(View rootView){
        ButterKnife.bind(this, rootView);
        setupResultRecycler();
        setupTryAgainListener();
    }

    private void setupResultRecycler(){
        mResultView.setLayoutManager(new StaggeredGridLayoutManager(mSpanCount,
                LinearLayoutManager.VERTICAL));

        mResultView.addOnScrollListener(mScrollListener);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                TwitterSearchManager.getInstance().loadNewOnce();
            }
        });
    }

    private void setupTryAgainListener(){
        mTryAgainListener = new NoConnectionHelper.TryAgainListener() {
            @Override
            public void onTry() {
                if(mListener != null) {
                    mLoadingBar.setVisibility(VISIBLE);
                    mListener.onTryAgain();
                }
            }
        };
    }

    public void setAdapter(RecyclerView.Adapter adapter){
        mResultView.setAdapter(adapter);
    }

    public void setResultListener(ResultViewListener listener){
        mListener = listener;
    }

    public void hidedLoadMoreProgress(){
        mIsLoadingMore = false;
    }

    public void showProgressBar(){
        mDualPanelHint.setVisibility(GONE);
        mLoadingBar.setVisibility(VISIBLE);
    }

    public void showDualPanelHint(){
        mDualPanelHint.setVisibility(VISIBLE);
        mLoadingBar.setVisibility(INVISIBLE);
    }

    public void hideHintAndProgress(){
        mLoadingBar.setVisibility(INVISIBLE);
        mDualPanelHint.setVisibility(GONE);
    }

    public void hideErrorMessage() {
        if(mNoConnectionHelper != null)
            mNoConnectionHelper.hideView();
    }

    public void showErrorMessage() {
        mLoadingBar.setVisibility(GONE);
        if(mNoConnectionHelper == null)
            mNoConnectionHelper = new NoConnectionHelper(mNoConnectionStub.inflate(), mTryAgainListener);
        mNoConnectionHelper.showView();
    }

    public void showEmptyView() {
        mLoadingBar.setVisibility(GONE);
        hideErrorMessage();
        mEmptyListStub.setVisibility(VISIBLE);
        mRefreshLayout.setVisibility(GONE);
    }

    public void hideLoadNewOnceProgress(){
        mRefreshLayout.setRefreshing(false);
        mResultView.smoothScrollToPosition(0);
    }

    private final RecyclerView.OnScrollListener mScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            if(dy > 0){
                int tweetsAmount = mResultView.getLayoutManager().getItemCount();
                int[] lastVisibleItem = ((StaggeredGridLayoutManager)mResultView.getLayoutManager())
                        .findLastVisibleItemPositions(null);

                //how many of tweets should have below the current scroll position before loading more
                if(!mIsLoadingMore && tweetsAmount <= (lastVisibleItem[lastVisibleItem.length-1] + LOAD_WINDOW_COUNT)){
                    loadMoreTweets();
                }
            }
        }
    };

    private void loadMoreTweets(){
        mIsLoadingMore = true;
        mHandler.post(mLoadMoreRunnable);
    }

    private final Runnable mLoadMoreRunnable = new Runnable() {
        @Override
        public void run() {
            if(mListener != null) mListener.onLoadMore();
        }
    };
}
