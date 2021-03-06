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

package com.gemapps.tweetysearch.ui.widget;

import android.app.Activity;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.View;

import com.gemapps.tweetysearch.util.ChromeTabActivityUtil;

import java.lang.ref.WeakReference;

/**
 * Created by edu on 2/17/17.
 * Wrapper class to handle myself the link click events, so I can launch Chrome custom tabs
 */
public class UrlClickableSpan extends URLSpan {

    private static final String TAG = "UrlClickableSpan";
    private WeakReference<Activity> mCurrentActivityWeak;
    private String mUrl;

    public UrlClickableSpan(Activity activity, String url) {
        super(url);
        mUrl = url;
        mCurrentActivityWeak = new WeakReference<Activity>(activity);
    }

    public UrlClickableSpan(Parcel in) {
        super(in);
        mUrl = in.readString();
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mUrl);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void onClick(View widget) {
        Log.d(TAG, "onClick: URL: " + mUrl);
        if(mCurrentActivityWeak.get() != null)
            ChromeTabActivityUtil.openCustomTab(mCurrentActivityWeak.get(), mUrl);
    }

    public static final Parcelable.Creator<UrlClickableSpan> CREATOR =
            new Parcelable.Creator<UrlClickableSpan>() {
        @Override
        public UrlClickableSpan createFromParcel(Parcel source) {
            return new UrlClickableSpan(source);
        }

        @Override
        public UrlClickableSpan[] newArray(int size) {
            return new UrlClickableSpan[size];
        }
    };
}