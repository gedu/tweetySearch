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

import android.util.Log;
import android.view.View;

import com.gemapps.tweetysearch.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by edu on 2/22/17.
 */

public class NoConnectionHelper {

    private static final String TAG = "NoConnectionItem";

    public interface TryAgainListener {
        void onTry();
    }
    @BindView(R.id.contentPanel)
    View mContainer;

    private TryAgainListener mListener;

    public NoConnectionHelper(View view, TryAgainListener listener) {
        ButterKnife.bind(this, view);

        mListener = listener;
    }

    public void showView(){
        mContainer.setVisibility(View.VISIBLE);
    }

    public void hideView(){
        mContainer.setVisibility(View.GONE);
    }

    @OnClick(R.id.try_again_button)
    public void onTryClicked(){
        Log.d(TAG, "onTryClicked() called");
        if(mListener != null)
            mListener.onTry();
    }
}
