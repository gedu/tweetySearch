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

package com.gemapps.tweetysearch.networking.httpclient;

import com.gemapps.tweetysearch.networking.model.NetworkResponseBridge;
import com.gemapps.tweetysearch.ui.model.TweetCollection;
import com.gemapps.tweetysearch.util.EventUtil;
import com.gemapps.tweetysearch.util.GsonUtil;

import static com.gemapps.tweetysearch.networking.model.NetworkResponseBridge.TWEETS_EMPTY_SEARCH;

/**
 * Created by edu on 2/13/17.
 */

public class SearchTweetsHttpClient extends BaseHttpClient {

    private static final String TAG = "SearchTweetsHttpClient";
    private static final String MAX_ID_PARAM = "&max_id=%s";
    private static final String SINCE_ID_PARAM = "&since_id=%s";

    public void getTweets(String url){
        doGet(url, NetworkResponseBridge.TWEETS_SEARCH);
    }

    public void getTweetsWithMaxId(String url, long maxId) {
        doGet(url+String.format(MAX_ID_PARAM, maxId), NetworkResponseBridge.TWEETS_LOAD_MORE);
    }

    public void getTweetsWithSinceId(String url, long tweetSinceId) {
        doGet(url+String.format(SINCE_ID_PARAM, tweetSinceId), NetworkResponseBridge.TWEETS_LOAD_NEW);
    }

    @Override
    protected void onSuccess(String body, int tag) {
        TweetCollection collection = GsonUtil.TWEETS_GSON.fromJson(body, TweetCollection.class);
        int responseType = collection.getTweetItems().size() > 0 ? tag : TWEETS_EMPTY_SEARCH;
        EventUtil.sendNetworkEvent(responseType, collection);
    }

    @Override
    protected void onFail() {
       EventUtil.sendErrorEvent();
    }

}
