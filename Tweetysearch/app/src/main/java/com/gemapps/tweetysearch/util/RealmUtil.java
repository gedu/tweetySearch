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

package com.gemapps.tweetysearch.util;

import android.util.Log;

import com.gemapps.tweetysearch.networking.TwitterSearchManager;
import com.gemapps.tweetysearch.networking.searchquery.RecentlySearchedItem;
import com.gemapps.tweetysearch.ui.model.TweetCollection;
import com.gemapps.tweetysearch.ui.model.TweetItem;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * Created by edu on 2/24/17.
 */

public class RealmUtil {

    private static final String TAG = "RealmUtil";
    private static final Realm mRealm = Realm.getDefaultInstance();

    public static void saveSearchResult(final TweetCollection tweetsResult){
        final RecentlySearchedItem queryParam = TwitterSearchManager.getInstance()
                .getSavableParameter();
        if(queryParam == null) return;
        tweetsResult.setId(queryParam.getUrlParams());
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                mRealm.copyToRealmOrUpdate(queryParam);
                mRealm.copyToRealmOrUpdate(tweetsResult);
            }
        });
    }

    public static void saveTweetsResult(final RealmList<TweetItem> tweetItems){
        if(tweetItems.size() > 0) {
            TweetCollection tweetCollection = new TweetCollection();
            tweetCollection.setTweetItems(tweetItems);
            saveSearchResult(tweetCollection);
        }
    }

    public static RecentlySearchedItem saveRecentlySearch(String urlParams, String humanParams){
        final RecentlySearchedItem searchedItem = new RecentlySearchedItem();
        searchedItem.setUrlParams(urlParams);
        searchedItem.setHumanParams(humanParams);
        mRealm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.insertOrUpdate(searchedItem);
            }
        });
        return searchedItem;
    }

    public static void deleteSearch(Realm realm, final RecentlySearchedItem searchedItem){
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RecentlySearchedItem savedSearch = realm.where(RecentlySearchedItem.class)
                        .equalTo(RecentlySearchedItem.COLUMN_URL_PARAM, searchedItem.getUrlParams())
                        .findFirst();
                Log.d(TAG, "deleting: "+searchedItem.getUrlParams());
                RealmResults<TweetCollection> tweetCollections = realm.where(TweetCollection.class)
                        .equalTo(TweetCollection.COLUMN_TWEETS_ID, searchedItem.getUrlParams())
                        .findAll();
                savedSearch.deleteFromRealm();
                for (TweetCollection tweet :
                        tweetCollections) {
                    Log.d(TAG, "t deleting: "+tweet.getId());
                    tweet.deleteFromRealm();
                }
            }
        });
    }

    public static TweetCollection findSearchResultsFrom(String id){
        return mRealm.where(TweetCollection.class)
                .equalTo(TweetCollection.COLUMN_TWEETS_ID, id)
                .findFirst();
    }

    public static RealmResults<RecentlySearchedItem> findRecentlySearchedAsync(){
        return mRealm.where(RecentlySearchedItem.class).findAllAsync();
    }
}
