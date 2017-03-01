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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gemapps.tweetysearch.R;
import com.gemapps.tweetysearch.networking.searchquery.RecentlySearchedItem;
import com.gemapps.tweetysearch.networking.searchquery.UrlParameter;
import com.gemapps.tweetysearch.networking.searchquery.paramquery.Query;
import com.gemapps.tweetysearch.ui.butter.ButterFragment;
import com.gemapps.tweetysearch.util.RealmUtil;

import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MainSearchFragment.OnSearchListener} interface
 * to handle interaction events.
 * Use the {@link MainSearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainSearchFragment extends ButterFragment
        implements RecentlySearchedAdapter.RecentlySearchedListener {

    private static final String TAG = "MainSearchFragment";

    public interface OnSearchListener {
        void onSearchedItemClicked(RecentlySearchedItem searchedItem);
        void onSearch(UrlParameter urlParameter);
    }

    private OnSearchListener mListener;
    private MainSearchViewHelper mViewHelper;
    private UrlParameter.Builder mParameterBuilder;
    private RecentlySearchedAdapter mSearchedAdapter;
    private Realm mRealm;
    private RealmResults<RecentlySearchedItem> mSearchedItems;
    private Query mQuery;

    public MainSearchFragment() {
        // Required empty public constructor
    }

    public static MainSearchFragment newInstance() {
        return new MainSearchFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupQueryBuilder();
        setupAdapterForSearched();
    }

    private void setupAdapterForSearched(){
        mRealm = Realm.getDefaultInstance();
        mSearchedItems = RealmUtil.findRecentlySearchedAsync();
        mSearchedAdapter = new RecentlySearchedAdapter(getContext(), mSearchedItems, this);
    }

    private void setupQueryBuilder(){
        mParameterBuilder = new UrlParameter.Builder();
        mQuery = new Query("");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnSearchListener) {
            mListener = (OnSearchListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = createView(inflater, container, R.layout.fragment_main_search);
        mViewHelper = new MainSearchViewHelper(rootView);
        mViewHelper.setRecentlySearchAdapter(mSearchedAdapter);
        setupViewHelper();
        return rootView;
    }

    private void setupViewHelper(){
        Log.d(TAG, "setupViewHelper: SETTING BUILDER");
        mViewHelper.setResultTypeBuilderToButtons(mParameterBuilder);
    }

    @OnClick(R.id.query_search_button)
    public void onSearchClicked(){
        onSearchPressed();
    }

    public void onSearchPressed() {
        if (mListener != null) {
            mQuery.setParameter(mViewHelper.getTextToSearch());
            mParameterBuilder.addParameter(mQuery);
            mListener.onSearch(mParameterBuilder.build());
        }
    }

    @Override
    public void onClicked(int position) {
        Log.d(TAG, "onClicked: "+mSearchedItems.get(position).getUrlParams());
        mListener.onSearchedItemClicked(mSearchedItems.get(position));
    }

    @Override
    public void onDeleted(final int position) {
        Log.d(TAG, "onDeleted: "+mSearchedItems.get(position).getUrlParams());
        RealmUtil.deleteSearch(mRealm, mSearchedItems.get(position));
    }

    @Override
    public void onDetach() {
        mListener = null;
        mRealm.close();
        super.onDetach();
    }
}
