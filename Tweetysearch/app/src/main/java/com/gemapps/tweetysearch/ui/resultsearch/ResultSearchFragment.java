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

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gemapps.tweetysearch.R;
import com.gemapps.tweetysearch.networking.TwitterSearchManager;
import com.gemapps.tweetysearch.networking.model.NetworkResponseBridge;
import com.gemapps.tweetysearch.networking.searchquery.RecentlySearchedItem;
import com.gemapps.tweetysearch.ui.butter.ButterFragment;
import com.gemapps.tweetysearch.ui.mainsearch.MainSearchActivity;
import com.gemapps.tweetysearch.ui.model.TweetCollection;
import com.gemapps.tweetysearch.ui.model.TweetItem;
import com.gemapps.tweetysearch.ui.state.ViewStateManager;
import com.gemapps.tweetysearch.util.RealmUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindBool;
import io.realm.RealmList;

/**
 * Use the {@link ResultSearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ResultSearchFragment extends ButterFragment
        implements ResultViewHelper.ResultViewListener,
        ResultRecyclerAdapter.ResultAdapterListener {

    private static final String TAG = "ResultSearchFragment";

    public interface ResultSearchListener {
        void onImageClicked(View v, TweetItem tweet);
    }

    private ResultRecyclerAdapter mAdapter;
    private ResultViewHelper mViewHelper;
    private ResultSearchListener mListener;
    private boolean mIsDualPanel;

    @BindBool(R.bool.is_sw600_land)
    boolean isLargeLandScreen;

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
        mAdapter = new ResultRecyclerAdapter(getActivity(), new RealmList<TweetItem>(), this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ResultSearchListener) {
            mListener = (ResultSearchListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
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
        if(savedInstanceState != null && showDualPanel()){
            getActivity().finish();
            TwitterSearchManager.getInstance().reTryLastSearch();
            return;
        }

        if(ViewStateManager.getInstance().hasToRebuild(savedInstanceState)){
           rebuildState();
        }
    }

    private boolean showDualPanel(){
        return isLargeLandScreen && ViewStateManager.getInstance().hasContentToShow();
    }

    private void setupDualPanelHint(){
        mIsDualPanel = isDualPanel();
        if(mIsDualPanel) mViewHelper.showDualPanelHint();
    }

    private boolean isDualPanel(){
        return getActivity() instanceof MainSearchActivity &&
                ((MainSearchActivity)getActivity()).isDualPanel();
    }

    private void rebuildState(){
        mViewHelper.hideHintAndProgress();
        RecentlySearchedItem searchedItem = TwitterSearchManager.getInstance().getSavableParameter();
        if(searchedItem != null){
            mAdapter.setTweets(RealmUtil.findSearchResultsFrom(searchedItem.getUrlParams()));
            mViewHelper.scrollTo(ViewStateManager.getInstance().getVisiblePositionInTweetList());
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        mViewHelper.setResultListener(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        int visiblePosition = mViewHelper.getFirstVisiblePosition();
        ViewStateManager.getInstance().setVisiblePositionInTweetList(visiblePosition);
        super.onSaveInstanceState(outState);
        RealmUtil.saveTweetsResult(mAdapter.getItems());
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        mViewHelper.setResultListener(null);
        super.onStop();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNetworkResponseEvent(NetworkResponseBridge response){
        Log.d(TAG, "onNetworkResponseEvent TWEETS YAY");
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
        if(mIsDualPanel)
            mViewHelper.scrollTo(ViewStateManager.getInstance().getVisiblePositionInTweetList());
    }

    private void addNewTweets(RealmList<TweetItem> tweets) {
        mAdapter.clearTweets();

        addTweets(tweets);
    }

    private void addTweets(RealmList<TweetItem> tweets){
        mViewHelper.hideErrorMessage();
        mAdapter.addTweetsAtEnd(tweets);
        ViewStateManager.getInstance().setHasContentToShow();
    }

    private void handleLoadMoreTweets(RealmList<TweetItem> tweets){
        mViewHelper.hidedLoadMoreProgress();
        mAdapter.removeProgressItem();
        addTweets(tweets);
    }

    private void handleLoadNewTweets(RealmList<TweetItem> tweets){
        mViewHelper.hideErrorMessage();
        mAdapter.addTweetsAtStart(tweets);
        mViewHelper.hideLoadNewOnceProgress();
        ViewStateManager.getInstance().setHasContentToShow();
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
    public void onDetach() {
        mListener = null;

        super.onDetach();
    }

    @Override
    public void onTryAgain() {
        TwitterSearchManager.getInstance().reTryLastSearch();
    }

    @Override
    public void onImageClicked(View v, int posClicked) {
        if(mListener != null)
            mListener.onImageClicked(v, mAdapter.getItemBy(posClicked));
    }
}
