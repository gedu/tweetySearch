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

package com.gemapps.tweetysearch.networking.model;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by edu on 2/14/17.
 */

public class NetworkResponseBridge<T> {

    @IntDef(flag=true, value={TWEETS_SEARCH})
    @Retention(RetentionPolicy.SOURCE)
    public @interface NetworkResponseType{}
    public static final int TWEETS_SEARCH = 0;
    public static final int TWEETS_LOAD_MORE = 1;
    public static final int TWEETS_LOAD_NEW = 2;

    private @NetworkResponseType int mType;
    private T mContent;

    public NetworkResponseBridge(int type, T content) {
        mType = type;
        mContent = content;
    }

    public int getType() {
        return mType;
    }

    public T getContent(){
        return mContent;
    }
}
