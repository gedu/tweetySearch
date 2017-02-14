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
public class User extends RealmObject {

    @SerializedName("description")
    private String mDescription;
    @SerializedName("followers_count")
    private String mFollowersCount;
    @SerializedName("name")
    private String mName;
    @SerializedName("profile_image_url")
    private String mProfileImageUrl;
    @SerializedName("id_str")
    private String mIdStr;
    @SerializedName("id")
    private int mId;

    public User() {}

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public String getFollowersCount() {
        return mFollowersCount;
    }

    public void setFollowersCount(String followersCount) {
        mFollowersCount = followersCount;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getProfileImageUrl() {
        return mProfileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        mProfileImageUrl = profileImageUrl;
    }

    public String getIdStr() {
        return mIdStr;
    }

    public void setIdStr(String idStr) {
        mIdStr = idStr;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }
}
