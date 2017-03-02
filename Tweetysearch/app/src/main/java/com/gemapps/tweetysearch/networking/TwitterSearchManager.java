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

package com.gemapps.tweetysearch.networking;

import android.os.Handler;
import android.util.Log;

import com.gemapps.tweetysearch.networking.httpclient.AuthenticationHttpClient;
import com.gemapps.tweetysearch.networking.httpclient.SearchTweetsHttpClient;
import com.gemapps.tweetysearch.networking.model.Bearer;
import com.gemapps.tweetysearch.networking.searchquery.RecentlySearchedItem;
import com.gemapps.tweetysearch.networking.searchquery.UrlParameter;
import com.gemapps.tweetysearch.ui.model.TweetCollection;
import com.gemapps.tweetysearch.util.EventUtil;
import com.gemapps.tweetysearch.util.Util;

import io.realm.Realm;

/**
 * Created by edu on 2/13/17.
 */

public class TwitterSearchManager {
    private static final String TAG = "TwitterSearchManager";

    private static final String TWITTER_BASE_URL = "https://api.twitter.com/1.1/search/tweets.json?";
    private static final String TWITTER_OAUTH_URL = "https://api.twitter.com/oauth2/token";

    private static final long INVALID_MAX_ID = -1;
    private static final long INVALID_SINCE_ID = -1;

    private static TwitterSearchManager mInstance;
    public static TwitterSearchManager getInstance(){
        if(mInstance == null) mInstance = new TwitterSearchManager();
        return mInstance;
    }

    private Realm mRealm;
    private Bearer mBearer;

    private UrlParameter mCurrentSearch;
    private long mTweetMaxId = INVALID_MAX_ID;
    private long mTweetSinceId = INVALID_SINCE_ID;

    private TwitterSearchManager(){
        mRealm = Realm.getDefaultInstance();
        mBearer = mRealm.where(Bearer.class).findFirst();
    }

    public void authenticate(){
        if(!bearerExist()) {
            mBearer = mRealm.where(Bearer.class).findFirstAsync();
            new AuthenticationHttpClient().authenticate(TWITTER_OAUTH_URL);
        }
    }

    private boolean bearerExist(){
        return mBearer != null;
    }

    public void reTryLastSearch(){
        search(mCurrentSearch.getSearchedItem());
    }

    public void search(RecentlySearchedItem searchedItem){
        final TweetCollection tweetCollection = getLocalResults(searchedItem.getUrlParams());
        Log.d(TAG, "HAS LOCAL RESULTS? "+((tweetCollection == null) ? "NO" : "YES"));
        mCurrentSearch = new UrlParameter.Builder()
                .setCompleteParam(searchedItem.getUrlParams()).build();
        if (tweetCollection == null) {
            searchWith(searchedItem.getUrlParams());
        } else {
            //todo:remove this, check if the activity is crated after
            Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    Util.setMaxAndSinceIds(tweetCollection);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Log.d(TAG, "run: SENDING RESULTS");
                            EventUtil.sendSearchAndNotSaveEvent(tweetCollection);
                        }
                    }, 1000);
                }
            });

        }
    }

    private TweetCollection getLocalResults(String resultId){
        return Realm.getDefaultInstance().where(TweetCollection.class)
                .equalTo(TweetCollection.COLUMN_TWEETS_ID, resultId).findFirst();
    }

    public void search(UrlParameter urlParameter){
        mCurrentSearch = urlParameter;
        searchWith(urlParameter.getParameters());
    }

    private void searchWith(String urlParams) {
        new SearchTweetsHttpClient().getTweets(TWITTER_BASE_URL + urlParams);
    }

    public void loadNewOnce(){
        Log.d(TAG, "loadNewOnce: SINCE: "+mTweetSinceId);
        if(mTweetSinceId != INVALID_SINCE_ID){
            new SearchTweetsHttpClient()
                    .getTweetsWithSinceId(TWITTER_BASE_URL + mCurrentSearch.getParameters(),
                            mTweetSinceId);
        }else{
            search(mCurrentSearch);
        }
    }

    public void loadMore(){
        Log.d(TAG, "loadMore: MAX: "+mTweetMaxId);
        if(mTweetMaxId != INVALID_MAX_ID){
            new SearchTweetsHttpClient()
                    .getTweetsWithMaxId(TWITTER_BASE_URL + mCurrentSearch.getParameters(),
                            mTweetMaxId);
        }else{
            search(mCurrentSearch);
        }
    }

    public RecentlySearchedItem getSavableParameter(){
        return mCurrentSearch == null ? null : mCurrentSearch.getSearchedItem();
    }

    public void setTweetMaxId(long maxId){
        mTweetMaxId = maxId;
    }

    public void setTweetSinceId(long sinceId){
        mTweetSinceId = sinceId;
    }

    public Realm getRealm(){
        return mRealm;
    }

    public String getBearerToken(){
        return mBearer.getToken();
    }
}
