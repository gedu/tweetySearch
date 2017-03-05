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

import android.os.Bundle;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

/**
 * Created by edu on 2/13/17.
 */

public class TweetItem extends RealmObject {

    public static final String TWEET_CREATION_DATE_KEY = "tweety.TWEET_CREATION_DATE_KEY";
    public static final String TWEET_USER_NAME_KEY = "tweety.TWEET_USER_NAME_KEY";
    public static final String TWEET_MEDIA_URL_KEY = "tweety.TWEET_MEDIA_URL_KEY";

    @SerializedName("id_str")
    private String mIdStr;
    @SerializedName("id")
    private long mId;
    @SerializedName("text")
    private String mText;
    @SerializedName("created_at")
    private String mCreatedAt;
    @SerializedName("user")
    private User mUser;
    @SerializedName("entities")
    private Entity mEntity;

    public TweetItem() {}

    public String getIdStr() {
        return mIdStr;
    }

    public void setIdStr(String idStr) {
        mIdStr = idStr;
    }

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        mId = id;
    }

    public String getText() {
        return mText;
    }

    public void setText(String text) {
        mText = text;
    }

    public String getCreatedAt() {
        return mCreatedAt;
    }

    public void setCreatedAt(String createdAt) {
        mCreatedAt = createdAt;
    }

    public User getUser() {
        return mUser;
    }

    public void setUser(User user) {
        mUser = user;
    }

    public Entity getEntity() {
        return mEntity;
    }

    public void setEntity(Entity entity) {
        mEntity = entity;
    }

    public Bundle bundleMainContent(){
        Bundle bundle = new Bundle();

        bundle.putString(TWEET_CREATION_DATE_KEY, mCreatedAt);
        bundle.putString(TWEET_USER_NAME_KEY, mUser.getName());
        if(mEntity.hasMedia())
            bundle.putString(TWEET_MEDIA_URL_KEY, mEntity.getMediaEntity().get(0).getMediaUrl());

        return bundle;
    }

    @Override
    public String toString() {
        return "ID: " + mIdStr + " TEXT: " + mText + " USER NAME: "+mUser.getName() +
                "DESCRIPTION: " + mUser.getDescription() +
                " MEDIA: " + (mEntity.hasMedia() ? mEntity.getMediaEntity().get(0).getMediaUrl() : "NONE");
    }
}
