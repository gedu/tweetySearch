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

package com.gemapps.tweetysearch.ui.mainsearch.presenter;

import com.gemapps.tweetysearch.networking.searchquery.RecentlySearchedItem;
import com.gemapps.tweetysearch.ui.mainsearch.RecentlySearchedAdapter;
import com.gemapps.tweetysearch.util.RealmUtil;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

/**
 * Created by edu on 3/11/17.
 */

public class MainFragmentPresenter implements MainFragmentContract.OnInteractionListener {
    private static final String TAG = "MainFragmentPresenter";
    private final Realm mRealm;
    private MainFragmentContract.OnSearchListener mListener;
    private MainFragmentContract.View mMainView;
    private RecentlySearchedAdapter mSearchedAdapter;
    private RealmResults<RecentlySearchedItem> mSearchedItems;

    public MainFragmentPresenter(MainFragmentContract.View view ,
                                 RecentlySearchable searchableInjection,
                                 Realm realm,
                                 MainFragmentContract.OnSearchListener listener) {
        mMainView = view;
        mRealm = realm;
        mListener = listener;
        mSearchedItems = searchableInjection.getRecentlySearchedItems();
        mSearchedItems.addChangeListener(new RealmChangeListener<RealmResults<RecentlySearchedItem>>() {
            @Override
            public void onChange(RealmResults<RecentlySearchedItem> element) {
                updateViewFromSearch();
            }
        });
    }

    @Override
    public void updateViewFromSearch() {
        if(mSearchedAdapter.getItemCount() == 0) mMainView.showEmptyView();
        else mMainView.hideEmptyView();
    }

    public void addAdapter(RecentlySearchedAdapter adapter){
        mSearchedAdapter = adapter;
    }

    @Override
    public void onPerformActionSearch() {
        if(mMainView.isTextToSearchValid()) doSearch();
        else mMainView.showSearchErrorLabel();
    }

    @Override
    public RealmResults<RecentlySearchedItem> getSearchedItems() {
        return mSearchedItems;
    }

    @Override
    public void onSearchedItemClick(int position) {
        mListener.onSearchedItemClicked(mSearchedItems.get(position));
    }

    @Override
    public void onDeleteSearchedItem(int position) {
        RealmUtil.deleteSearch(mRealm, mSearchedItems.get(position));
    }

    private void doSearch(){
        mMainView.hideSearchErrorLabel();
        mListener.onSearch(mMainView.getSearchifiedText());
    }

    @Override
    public void wipeListeners() {
        mListener = null;
        mSearchedItems.removeChangeListeners();
        mRealm.close();
    }


}
