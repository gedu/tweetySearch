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

import android.support.annotation.IntDef;

import com.gemapps.tweetysearch.networking.searchquery.Parameterizable;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by edu on 2/20/17.
 */

public class ResultType implements Parameterizable {

    private static final String TAG = "ResultType";
    @IntDef(flag=true, value={RECENT, POPULAR, MIXED})
    @Retention(RetentionPolicy.SOURCE)
    public @interface SEARCH_RESULT_TYPE{}
    public static final int RECENT = 1;
    public static final int POPULAR = 1 << 1;
    public static final int MIXED = RECENT | POPULAR;

    private static final String RESULT_TYPE_KEY = "result_type=%s";

    private @SEARCH_RESULT_TYPE int mSearchType = 0;

    public void updateType(@SEARCH_RESULT_TYPE int type){
        mSearchType ^= type;
    }

    @Override
    public void setParameter(String value) {

        //// TODO: 2/20/17 show message invalid method
    }

    @Override
    public String getParameterQuery() {
        switch (mSearchType){
            case RECENT: return formatType("recent");
            case POPULAR: return formatType("popular");
            case MIXED: return formatType("mixed");
            default: return "";
        }
    }

    private String formatType(String type){
        return String.format(RESULT_TYPE_KEY, type);
    }
}
