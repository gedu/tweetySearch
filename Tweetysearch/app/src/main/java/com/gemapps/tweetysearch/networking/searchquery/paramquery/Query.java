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

package com.gemapps.tweetysearch.networking.searchquery.paramquery;

import android.support.annotation.NonNull;

import com.gemapps.tweetysearch.networking.searchquery.Parameterizable;

import java.net.URLEncoder;

/**
 * Created by edu on 2/13/17.
 */

public class Query implements Parameterizable {

    private static final String QUERY_KEY = "q=%s";
    private String mQueryValue = "";

    public Query(@NonNull String queryValue) {
        setParameter(queryValue);
    }

    @Override
    public void setParameter(@NonNull String value) {
        mQueryValue = URLEncoder.encode(value);
    }

    @Override
    public String getParameterQuery() {
        //// TODO: 2/13/17 Create a custom Exception
        if(mQueryValue.isEmpty()) throw new UnsupportedOperationException("UrlParameter value is empty");
        return String.format(QUERY_KEY, mQueryValue);
    }

//    @Override
//    public String getHumanReadableQuery() {
//        return null;
//    }
}
