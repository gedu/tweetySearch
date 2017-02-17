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

package com.gemapps.tweetysearch.util;

import android.app.Activity;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.URLSpan;

import com.gemapps.tweetysearch.ui.widget.UrlClickableSpan;

/**
 * Created by edu on 2/16/17.
 */

public class HtmlUtil {

    private static final String TAG = "HtmlUtil";

    public static Spannable parseTextToHandleLink(Activity activity, Spannable text) {
        URLSpan[] urlSpans = text.getSpans(0, text.length(), URLSpan.class);
        for (URLSpan urlSpan : urlSpans) {
            int start = text.getSpanStart(urlSpan);
            int end = start + urlSpan.getURL().length();
            text.removeSpan(urlSpan);
            text.setSpan(new UrlClickableSpan(activity, urlSpan.getURL()),
                    start, end, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        }
        return text;
    }
}
