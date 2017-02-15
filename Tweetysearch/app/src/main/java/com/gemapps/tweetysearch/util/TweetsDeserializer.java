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

import com.gemapps.tweetysearch.networking.TwitterSearchManager;
import com.gemapps.tweetysearch.ui.model.TweetCollection;
import com.gemapps.tweetysearch.ui.model.TweetItem;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

/**
 * Created by edu on 2/13/17.
 */

public class TweetsDeserializer implements JsonDeserializer<TweetCollection> {
    private static final String TAG = "TwitterArrayDeserialize";
    private static final String STATUS_KEY = "statuses";
    private static final int REMOVE_REDUNDANT_ID = 1;

    @Override
    public TweetCollection deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        return parse(json);
    }

    private TweetCollection parse(JsonElement json){
        JsonArray tweetsData = getTweetsData(json);
        return populateTweetsWith(tweetsData);
    }

    private JsonArray getTweetsData(JsonElement json){
        return json.getAsJsonObject().get(STATUS_KEY).getAsJsonArray();
    }

    private TweetCollection populateTweetsWith(JsonArray statuses){
        TweetCollection tweetsCollection = new TweetCollection();
        long lowestId = Long.MAX_VALUE;
        long higherId = Long.MIN_VALUE;
        //// TODO: 2/15/17 CHECK IF ALWAYS THE LAST ITEM IS THE LOWEST AND THE FIRST IS THE HIGHEST
        for (JsonElement element : statuses) {
            TweetItem tweetItem = new Gson().fromJson(element, TweetItem.class);

            long id = tweetItem.getId();
            if(id < lowestId){
                lowestId = id;
            }

            if(id > higherId){
                higherId = id;
            }

            tweetsCollection.addTweet(tweetItem);
        }
        TwitterSearchManager.getInstance().setTweetMaxId(lowestId - REMOVE_REDUNDANT_ID);
        TwitterSearchManager.getInstance().setTweetSinceId(higherId);
        return tweetsCollection;
    }
}
