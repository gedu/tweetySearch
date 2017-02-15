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

import io.realm.RealmObject;

/**
 * Created by edu on 2/13/17.
 */

public class TweetItem extends RealmObject {

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
}
