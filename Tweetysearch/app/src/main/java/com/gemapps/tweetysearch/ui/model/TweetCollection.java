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

package com.gemapps.tweetysearch.ui.model;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by edu on 2/13/17.
 */

public class TweetCollection extends RealmObject {

    public static final String COLUMN_TWEETS_ID = "mId";

    @SerializedName("statuses")
    private RealmList<TweetItem> mTweetItems = new RealmList<>();
    @PrimaryKey
    private String mId;

    public TweetCollection() {}

    public void addTweet(TweetItem tweetItem){
        mTweetItems.add(tweetItem);
    }

    public RealmList<TweetItem> getTweetItems() {
        return mTweetItems;
    }

    public void setTweetItems(RealmList<TweetItem> tweetItems) {
        mTweetItems = tweetItems;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }
}
