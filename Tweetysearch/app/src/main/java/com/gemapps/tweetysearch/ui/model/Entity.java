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

/**
 * Created by edu on 2/20/17.
 */

public class Entity extends RealmObject {

    @SerializedName("media")
    public RealmList<MediaEntity> mMediaEntity;

    public Entity() {}

    public boolean hasMedia(){
        return mMediaEntity != null && mMediaEntity.size() > 0;
    }
    public RealmList<MediaEntity> getMediaEntity() {
        return mMediaEntity;
    }


    public void setMediaEntity(RealmList<MediaEntity> mediaEntity) {
        mMediaEntity = mediaEntity;
    }
}
