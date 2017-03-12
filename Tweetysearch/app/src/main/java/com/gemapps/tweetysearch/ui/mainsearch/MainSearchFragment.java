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

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gemapps.tweetysearch.R;
import com.gemapps.tweetysearch.networking.searchquery.RecentlySearchedItem;
import com.gemapps.tweetysearch.networking.searchquery.UrlParameter;
import com.gemapps.tweetysearch.networking.searchquery.paramquery.Query;
import com.gemapps.tweetysearch.ui.butter.ButterFragment;
import com.gemapps.tweetysearch.ui.mainsearch.presenter.MainFragmentContract;
import com.gemapps.tweetysearch.ui.mainsearch.presenter.MainFragmentPresenter;
import com.gemapps.tweetysearch.ui.mainsearch.presenter.SearchedItemInjector;
import com.gemapps.tweetysearch.ui.widget.search.SearchTextAction;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MainFragmentContract.OnSearchListener} interface
 * to handle interaction events.
 * Use the {@link MainSearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainSearchFragment extends ButterFragment
        implements RecentlySearchedAdapter.RecentlySearchedListener,
        SearchTextAction.SearchTextActionListener,
        MainFragmentContract.View {

    private static final String TAG = "MainSearchFragment";

    private MainFragmentContract.OnInteractionListener mInteractionListener;

    private MainSearchViewHelper mViewHelper;
    private UrlParameter.Builder mParameterBuilder;
    private RecentlySearchedAdapter mSearchedAdapter;

    private Query mQuery;

    public MainSearchFragment() {
        // Required empty public constructor
    }

    public static MainSearchFragment newInstance() {
        return new MainSearchFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MainFragmentContract.OnSearchListener) {
            mInteractionListener = new MainFragmentPresenter(this,
                    SearchedItemInjector.provideRecentlyItems(),
                    Realm.getDefaultInstance(),
                    (MainFragmentContract.OnSearchListener) context);
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement MainFragmentContract.OnSearchListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupQueryBuilder();
        setupAdapterForSearched();
    }

    private void setupAdapterForSearched(){
        RealmResults<RecentlySearchedItem> searchedItems = mInteractionListener.getSearchedItems();
        mSearchedAdapter = new RecentlySearchedAdapter(getContext(), searchedItems, this);
        mInteractionListener.addAdapter(mSearchedAdapter);
    }

    @Override
    public void showEmptyView(){
        mViewHelper.showEmptyView();
    }

    @Override
    public void hideEmptyView(){
        mViewHelper.hideEmptyView();
    }

    private void setupQueryBuilder(){
        mQuery = new Query("");
        mParameterBuilder = new UrlParameter.Builder(mQuery);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = createView(inflater, container, R.layout.fragment_main_search);
        mViewHelper = new MainSearchViewHelper(rootView);
        setupViewHelper();
        mInteractionListener.updateViewFromSearch();
        return rootView;
    }

    private void setupViewHelper(){
        mViewHelper.setRecentlySearchAdapter(mSearchedAdapter);
        mViewHelper.setResultTypeBuilderToButtons(mParameterBuilder);
        mViewHelper.addSearchActionListener(this);
    }

    @Override
    public void onSearchAction() {
        mInteractionListener.onPerformActionSearch();
    }

    @Override
    public boolean isTextToSearchValid() {
        return mViewHelper.isSearchTextValid();
    }

    @Override
    public void showSearchErrorLabel() {
        mViewHelper.showErrorSearchLabel();
    }

    @Override
    public void hideSearchErrorLabel() {
        mViewHelper.hideErrorSearchLabel();
    }

    @Override
    public UrlParameter getSearchifiedText(){
        mQuery.setParameter(mViewHelper.getTextToSearch());
        return mParameterBuilder.build();
    }

    @Override
    public void onClicked(int position) {
        mInteractionListener.onSearchedItemClick(position);
    }

    @Override
    public void onDeleted(final int position) {
        mInteractionListener.onDeleteSearchedItem(position);
    }

    @Override
    public void onDetach() {
        mInteractionListener.wipeListeners();
        super.onDetach();
    }
}
