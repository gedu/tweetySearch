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

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import com.gemapps.tweetysearch.R;
import com.gemapps.tweetysearch.networking.searchquery.UrlParameter;
import com.gemapps.tweetysearch.networking.searchquery.paramquery.ResultType;

import static com.gemapps.tweetysearch.networking.searchquery.paramquery.ResultType.POPULAR;
import static com.gemapps.tweetysearch.networking.searchquery.paramquery.ResultType.RECENT;

/**
 * Created by edu on 2/21/17.
 */

public class ResultTypeButtonSelectable extends SelectableColorButton {

    private @ResultType.SEARCH_RESULT_TYPE int mType;
    private UrlParameter.Builder mBuilder;

    public ResultTypeButtonSelectable(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, R.style.BorderlessButton, 0);
    }

    public ResultTypeButtonSelectable(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr, 0);
    }

    public ResultTypeButtonSelectable(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes){

        final TypedArray typedArray = context.obtainStyledAttributes(attrs,
                R.styleable.ResultTypeButtonSelectable, defStyleAttr, defStyleRes);

        try{
            int type = typedArray.getInt(R.styleable.ResultTypeButtonSelectable_search_type, 0);
            mType = type == RECENT ? RECENT : POPULAR;
        }finally {
            typedArray.recycle();
        }
    }

    @Override
    protected void onClicked() {

        if(mBuilder != null)
            mBuilder.setResultType(mType);
    }

    public void setResultTypeBuilder(UrlParameter.Builder builder){
        mBuilder = builder;
    }
}
