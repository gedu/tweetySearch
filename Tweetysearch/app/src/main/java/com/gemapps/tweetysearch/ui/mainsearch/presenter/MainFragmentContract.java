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
import com.gemapps.tweetysearch.networking.searchquery.UrlParameter;
import com.gemapps.tweetysearch.ui.mainsearch.RecentlySearchedAdapter;

import io.realm.RealmResults;

/**
 * Created by edu on 3/11/17.
 * Contract between the view and the presenter
 */
public interface MainFragmentContract {

    interface View {
        void showEmptyView();
        void hideEmptyView();

        boolean isTextToSearchValid();

        void showSearchErrorLabel();
        void hideSearchErrorLabel();

        UrlParameter getSearchifiedText();
    }


    interface OnSearchListener {
        void onSearchedItemClicked(RecentlySearchedItem searchedItem);
        void onSearch(UrlParameter urlParameter);
    }

    interface OnInteractionListener {
        void updateViewFromSearch();
        void addAdapter(RecentlySearchedAdapter adapter);
        void onPerformActionSearch();

        RealmResults<RecentlySearchedItem> getSearchedItems();

        void onSearchedItemClick(int position);
        void onDeleteSearchedItem(int position);

        void wipeListeners();
    }
}
