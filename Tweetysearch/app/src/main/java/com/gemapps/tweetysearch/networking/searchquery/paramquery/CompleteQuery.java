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

import com.gemapps.tweetysearch.networking.searchquery.Parameterizable;

/**
 * Created by edu on 3/3/17.
 */

public class CompleteQuery implements Parameterizable {

    private String mUrlQuery;
    private String mHumanQuery;

    public CompleteQuery() {}

    public CompleteQuery(String urlQuery, String humanQuery) {
        mUrlQuery = urlQuery;
        mHumanQuery = humanQuery;
    }

    @Override
    public void setParameter(String value) {
        mUrlQuery = value;
    }

    @Override
    public void setHumanParameter(String value) {
        mHumanQuery = value;
    }

    @Override
    public String getParameterQuery() {
        return mUrlQuery;
    }

    @Override
    public String getHumanReadableQuery() {
        return mHumanQuery;
    }
}
