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

import android.util.Log;

import com.gemapps.tweetysearch.ui.model.TweetCollection;
import com.gemapps.tweetysearch.util.GsonUtil;

/**
 * Created by edu on 2/13/17.
 */

public class SearchTweetsHttpClient extends BaseHttpClient {

    private static final String TAG = "SearchTweetsHttpClient";

    public void getTweets(String url){
        Log.d(TAG, "getTweets: URL: "+url);
        doGet(url, new HttpClientListener() {
            @Override
            public void onSuccess(String response) {

                TweetCollection collection = GsonUtil.TWEETS_GSON.fromJson(response, TweetCollection.class);
                Log.d(TAG, "onSuccess: "+collection.getTweetItems().size());
            }

            @Override
            public void onFailure() {
                Log.d(TAG, "onFailure: ");
            }
        });
    }
}