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

package com.gemapps.tweetysearch.networking.searchquery;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by edu on 2/14/17.
 */

public class RecentlySearchedItem extends RealmObject {

    public static final String COLUMN_URL_PARAM = "mUrlParams";

    @PrimaryKey
    private String mUrlParams;
    private String mHumanParams;

    public String getUrlParams() {
        return mUrlParams;
    }

    public void setUrlParams(String urlParams) {
        mUrlParams = urlParams;
    }

    public String getHumanParams() {
        return mHumanParams;
    }

    public void setHumanParams(String humanParams) {
        mHumanParams = humanParams;
    }
}
