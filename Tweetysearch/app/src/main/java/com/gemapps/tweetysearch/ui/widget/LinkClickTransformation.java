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
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.Spannable;
import android.text.method.TransformationMethod;
import android.view.View;
import android.widget.TextView;

import java.lang.ref.WeakReference;

import static com.gemapps.tweetysearch.util.HtmlUtil.parseTextToHandleLink;

/**
 * Created by edu on 2/17/17.
 * Wrapper to handle the link clicking
 */
@RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
public class LinkClickTransformation implements TransformationMethod {

    private WeakReference<Activity> mActivityWeak;

    public LinkClickTransformation(Activity activity) {
        mActivityWeak = new WeakReference<Activity>(activity);
    }

    @Override
    public CharSequence getTransformation(CharSequence source, View view) {
        if(view instanceof TextView) {
            TextView textView = (TextView) view;
            if(textView.getText().length() > 0)
                return parseTextToHandleLink(mActivityWeak.get(), (Spannable) textView.getText());
        }
        return source;
    }


    @Override
    public void onFocusChanged(View view, CharSequence sourceText, boolean focused,
                               int direction, Rect previouslyFocusedRect) {}
}
