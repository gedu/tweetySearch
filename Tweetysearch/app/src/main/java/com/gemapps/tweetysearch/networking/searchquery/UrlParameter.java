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

package com.gemapps.tweetysearch.networking.searchquery;

import com.gemapps.tweetysearch.networking.searchquery.paramquery.CompleteQuery;
import com.gemapps.tweetysearch.networking.searchquery.paramquery.Query;
import com.gemapps.tweetysearch.networking.searchquery.paramquery.ResultType;
import com.gemapps.tweetysearch.util.RealmUtil;

/**
 * Created by edu on 2/12/17.
 */

public final class UrlParameter {

    private static final String TAG = "UrlParameter";
    private static final String URL_AND ="&";
    private static final String HUMAN_AND =", ";
    private String mParams;
    private String mHumanParams;
    private RecentlySearchedItem mSearchedItem;

    private UrlParameter(Builder builder) {
        concatenateParams(builder);
        save();
    }

    private void concatenateParams(Builder builder){
        setUrlParams(builder);
        setHumanReadableParams(builder);
    }

    private void setUrlParams(Builder builder){
        StringBuilder urlStringBuilder = new StringBuilder();

        if(builder.mCompleteParam != null) {
            urlStringBuilder.append(builder.mCompleteParam.getParameterQuery());
        }else {
            urlStringBuilder.append(builder.mQuery.getParameterQuery());

            if (builder.mResultType != null) {
                urlStringBuilder.append(URL_AND);
                urlStringBuilder.append(builder.mResultType.getParameterQuery());
            }
        }
        mParams = urlStringBuilder.toString();
    }

    private void setHumanReadableParams(Builder builder){
        StringBuilder humanReadableBuilder = new StringBuilder();

        if(builder.mCompleteParam != null) {
            humanReadableBuilder.append(builder.mCompleteParam.getHumanReadableQuery());
        }else {
            humanReadableBuilder.append(builder.mQuery.getHumanReadableQuery());

            if (builder.mResultType != null) {
                humanReadableBuilder.append(HUMAN_AND);
                humanReadableBuilder.append(builder.mResultType.getHumanReadableQuery());
            }
        }
        mHumanParams = humanReadableBuilder.toString();
    }

    private void save(){
        mSearchedItem = RealmUtil.saveRecentlySearch(mParams, mHumanParams);
    }

    public RecentlySearchedItem getSearchedItem() {
        return mSearchedItem;
    }

    public String getParameters(){
        return mParams;
    }

    public static class Builder {

        private CompleteQuery mCompleteParam = null;
        private ResultType mResultType = null;
        private Query mQuery = null;

        public Builder(Query query){
            mQuery = query;
        }

        public Builder setResultType(Integer type){
            if(mResultType == null) mResultType = new ResultType();
            mResultType.updateType(type);
            return this;
        }

        public Builder setCompleteParam(CompleteQuery completeQuery){
            mCompleteParam = completeQuery;
            return this;
        }

        public UrlParameter build(){
            return new UrlParameter(this);
        }
    }
}
