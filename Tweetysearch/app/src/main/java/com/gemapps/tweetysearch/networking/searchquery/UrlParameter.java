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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by edu on 2/12/17.
 */

public final class UrlParameter {

    private static final String TAG = "UrlParameter";
    private static final String URL_AND ="&";
    private String mParams;

    private UrlParameter(Builder builder) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Parameterizable param : builder.queries) {

            stringBuilder.append(param.getParameterQuery());
            stringBuilder.append(URL_AND);
        }
        stringBuilder.deleteCharAt(stringBuilder.length() -1);
        mParams = stringBuilder.toString();
    }

    public String getParameters(){
        return mParams;
    }

    public static class Builder {

        List<Parameterizable> queries = new ArrayList<>();

        public void addParameter(Parameterizable parameter){
            removeParameter(parameter);
            queries.add(parameter);
        }

        public void removeParameter(Parameterizable parameter){
            if(queries.contains(parameter)) queries.remove(parameter);
        }

        public List<Parameterizable> getQueries(){
            return queries;
        }

        public UrlParameter build(){
            return new UrlParameter(this);
        }
    }
}
