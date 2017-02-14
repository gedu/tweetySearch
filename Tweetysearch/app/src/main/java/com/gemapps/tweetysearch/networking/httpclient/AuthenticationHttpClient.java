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

package com.gemapps.tweetysearch.networking.httpclient;

import android.util.Log;

import com.gemapps.tweetysearch.BuildConfig;
import com.gemapps.tweetysearch.networking.TwitterSearchManager;
import com.gemapps.tweetysearch.networking.model.Bearer;
import com.gemapps.tweetysearch.util.GsonUtil;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;

import io.realm.Realm;

/**
 * Created by edu on 2/13/17.
 */

public class AuthenticationHttpClient extends BaseHttpClient {

    private static final String TAG = "AuthenticationHttpClien";

    private static final String AUTHORIZATION_VALUE = "Basic %s";
    private static final String CONTENT_TYPE_VALUE = "application/x-www-form-urlencoded;charset=UTF-8";
    private static final String BODY = "grant_type=client_credentials";

    public void authenticate(String url){
        doPost(url, buildHeader(), buildRequestBody(), new HttpClientListener() {
            @Override
            public void onSuccess(final String response) {
                TwitterSearchManager.getInstance()
                        .getRealm().executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        Bearer bearer = GsonUtil.BEARER_GSON.fromJson(response, Bearer.class);
                        realm.insertOrUpdate(bearer);
                    }
                });
            }

            @Override
            public void onFailure() {
                Log.d(TAG, "onFailure() called");
            }
        });
    }

    private Headers buildHeader(){
        Headers.Builder builder = new Headers.Builder();
        builder.add(AUTHORIZATION_KEY, String.format(AUTHORIZATION_VALUE, BuildConfig.TWITTER_APP_KEY));
        return builder.build();
    }

    private RequestBody buildRequestBody(){
        return RequestBody.create(MediaType.parse(CONTENT_TYPE_VALUE), BODY);
    }
}
