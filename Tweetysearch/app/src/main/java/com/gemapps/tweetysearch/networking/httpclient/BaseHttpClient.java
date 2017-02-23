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

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.gemapps.tweetysearch.networking.TwitterSearchManager;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * Created by edu on 2/12/17.
 *
 * Base class to use the Tweeter API
 */
public abstract class BaseHttpClient {

    private static final String TAG = "BaseHttpClient";
    public interface HttpClientListener {
        void onSuccess(String response);
        void onFailure();
    }
    static final String AUTHORIZATION_KEY = "Authorization";
    private static final String BEARER_VALUE = "Bearer %s";
    private static final int OK = 200;

    private Handler mHandler = new Handler(Looper.getMainLooper());

    void doPost(String url, Headers headers, RequestBody body){

        final Request request = new Request.Builder()
                .headers(headers)
                .url(url)
                .tag(OK)
                .post(body)
                .build();
        makeNew(request);
    }

    void doGet(String url, int tag){
        final Request request = new Request.Builder()
                .url(url)
                .tag(tag)
                .addHeader(AUTHORIZATION_KEY, formatBearer())
                .build();
        makeNew(request);
    }

    private void makeNew(final Request request){
        final OkHttpClient httpClient = BaseOkhttp.CLIENT;
        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                onFail();
            }

            @Override
            public void onResponse(Response response) throws IOException {
                final String body = response.body().string();
                final int tag = (int) response.request().tag();
                response.body().close();
                if(response.code() == OK) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            onSuccess(body, tag);
                        }
                    });
                }else{
                    Log.e(TAG, "onResponse: CODE: "+response.code());
                    onFail();
                }
            }
        });
    }

    private String formatBearer(){
        return String.format(BEARER_VALUE, TwitterSearchManager.getInstance().getBearerToken());
    }

    protected abstract void onSuccess(String body, int tag);
    protected abstract void onFail();

    private static class BaseOkhttp {

        static final OkHttpClient CLIENT = new OkHttpClient();
    }
}
