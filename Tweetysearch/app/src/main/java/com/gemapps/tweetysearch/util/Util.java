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

import java.util.Collections;
import java.util.Comparator;

/**
 * Created by edu on 2/20/17.
 */

public class Util {

    private static final int REMOVE_REDUNDANT_ID = 1;

    public static boolean isInteger(String value){
        return value.matches("^-?\\d+$");
    }

    public static void setMaxAndSinceIds(TweetCollection tweets){
        Collections.sort(tweets.getTweetItems(), new Util.SortDescTweets());
        long lowestId = tweets.getTweetItems().get(0).getId();
        long higherId = tweets.getTweetItems().get(tweets.getTweetItems().size()-1).getId();
        TwitterSearchManager.getInstance().setTweetMaxId(lowestId - REMOVE_REDUNDANT_ID);
        TwitterSearchManager.getInstance().setTweetSinceId(higherId);
    }

    public static class SortDescTweets implements Comparator<TweetItem> {

        @Override
        public int compare(TweetItem a, TweetItem b) {
            return a.getId() > b.getId() ? 1 : a.getId() < b.getId() ? -1 : 0;
        }
    }
}
