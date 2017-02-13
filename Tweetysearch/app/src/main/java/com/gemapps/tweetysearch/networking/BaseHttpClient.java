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

package com.gemapps.tweetysearch.networking;

import android.os.Handler;
import android.os.Looper;

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
public class BaseHttpClient {

    private static final String TAG = "BaseHttpClient";
    public interface HttpClientListener {
        void onSuccess(String response);
        void onFailure();
    }
    private static final int OK = 200;

    private Handler mHandler = new Handler(Looper.getMainLooper());
    private HttpClientListener mListener;

    void doPost(String url, Headers headers, RequestBody body, HttpClientListener listener){

        mListener = listener;
        final Request request = new Request.Builder()
                .headers(headers)
                .url(url)
                .post(body)
                .build();
        makeNew(request);
    }

    void doGet(String url, HttpClientListener listener){
        mListener = listener;

        final Request request = new Request.Builder().url(url).build();
        makeNew(request);
    }

    private void makeNew(final Request request){
        final OkHttpClient httpClient = BaseOkhttp.CLIENT;
        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                mListener.onFailure();
            }

            @Override
            public void onResponse(Response response) throws IOException {
                final String body = response.body().string();
                response.body().close();
                if(response.code() == OK) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mListener.onSuccess(body);
                        }
                    });
                }else{
                    mListener.onFailure();
                }
            }
        });
    }

    private static class BaseOkhttp {

        static final OkHttpClient CLIENT = new OkHttpClient();
    }
}
