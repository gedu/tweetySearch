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

import com.gemapps.tweetysearch.networking.searchquery.UrlParameter;

/**
 * Created by edu on 2/13/17.
 */

public class TweeterSearchManager {

    private static final String TWEETER_BASE_URL = "https://api.twitter.com/1.1/search/tweets.json?";
    private static TweeterSearchManager mInstance;

    public static TweeterSearchManager getInstance(){
        if(mInstance == null) mInstance = new TweeterSearchManager();
        return mInstance;
    }

    private TweeterSearchManager(){}

    private boolean mFirstSearch = false;

    public void search(UrlParameter urlParameter){

        new SearchTweetsHttpClient().getTweets(TWEETER_BASE_URL + urlParameter.getParameters());
    }
}
