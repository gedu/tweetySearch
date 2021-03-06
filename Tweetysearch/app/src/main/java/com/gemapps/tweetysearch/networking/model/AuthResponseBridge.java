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
 * Created by edu on 3/6/17.
 */

public class AuthResponseBridge {

    @IntDef(flag=true, value={SUCCESS, FAILED, LOGIN_REQUIRED, LOGIN_IN_PROGRESS})
    @Retention(RetentionPolicy.SOURCE)
    public @interface AuthType{}
    public static final int SUCCESS = 0;
    public static final int FAILED = 1;
    public static final int LOGIN_REQUIRED = 2;
    public static final int LOGIN_IN_PROGRESS = 3;

    private @AuthResponseBridge.AuthType int mType;

    public AuthResponseBridge(int type) {
        mType = type;
    }

    public int getType(){return mType;}
}
