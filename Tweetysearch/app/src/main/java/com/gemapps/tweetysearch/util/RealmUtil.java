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

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by edu on 2/24/17.
 */

public class RealmUtil {

    private static final String TAG = "RealmUtil";

    public static void saveSearchResult(final TweetCollection tweetsResult){
        Log.d(TAG, "SAVING TWEETS ");
        final RecentlySearchedItem queryParam = TwitterSearchManager.getInstance()
                .getSavableParameter();
        if(queryParam == null) return;
        tweetsResult.setId(queryParam.getUrlParams());
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Log.d(TAG, "execute: ");
                realm.copyToRealmOrUpdate(queryParam);
                realm.copyToRealmOrUpdate(tweetsResult);
            }
        });

    }

    public static void deleteSearch(Realm realm, final RecentlySearchedItem searchedItem){
        realm.executeTransaction(new Realm.Transaction() {
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
}
