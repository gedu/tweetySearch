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
import android.os.Parcel;
import android.os.Parcelable;
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
    private static final String TAG = "ResultTypeButtonSelecta";

    private @ResultType.SEARCH_RESULT_TYPE int mType;
    private boolean mClicked;
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
        mClicked = false;
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
    public Parcelable onSaveInstanceState() {
        ResultTypeSavedState resultTypeSavedState = new ResultTypeSavedState(super.onSaveInstanceState());
        resultTypeSavedState.clicked = mClicked;
        return resultTypeSavedState;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        if(state instanceof ResultTypeSavedState){
            ResultTypeSavedState resultTypeSavedState = (ResultTypeSavedState) state;
            super.onRestoreInstanceState(resultTypeSavedState.getSuperState());
            mClicked = resultTypeSavedState.clicked;
            if(mClicked) setType();
        }else {
            super.onRestoreInstanceState(state);
        }
    }

    @Override
    protected void onClicked() {
        mClicked = true;
        setType();
    }

    private void setType(){
        if(mBuilder != null)
            mBuilder.setResultType(mType);
    }

    public void setResultTypeBuilder(UrlParameter.Builder builder){
        mBuilder = builder;
    }

    public static class ResultTypeSavedState extends BaseSavedState {

        boolean clicked;

        public ResultTypeSavedState(Parcel source) {
            super(source);
            clicked = source.readInt() == 1;
        }

        public ResultTypeSavedState(Parcelable superState) {
            super(superState);
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(clicked ? 1 : 0);
        }

        public static final Parcelable.Creator<ResultTypeSavedState> CREATOR
                = new Parcelable.Creator<ResultTypeSavedState>() {
            public ResultTypeSavedState createFromParcel(Parcel in) {
                return new ResultTypeSavedState(in);
            }

            public ResultTypeSavedState[] newArray(int size) {
                return new ResultTypeSavedState[size];
            }
        };
    }
}
