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

package com.gemapps.tweetysearch.ui.state;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

/**
 * Created by edu on 3/2/17.
 */

public class ViewStateManager {

    private static ViewStateManager sInstance;
    private ActionState mState;
    private int mVisiblePositionInTweetList = RecyclerView.NO_POSITION;


    public static ViewStateManager getInstance(){
        if(sInstance == null) sInstance = new ViewStateManager();
        return sInstance;
    }

    public void initState(Context context){
        if(mState == null){
            mState = StateFactory.getDeviceState(context);
        }
    }

    public void setDualPanelState(boolean isDualPanel){
        mState.setDualPanelState(isDualPanel);
    }

    public void setHasContentToShow(){
        mState.setHasContentToShow(true);
    }

    public boolean wasDualPanel(){
        return mState.wasDualPanel();
    }

    public boolean hasToRebuild(Bundle savedInstanceState){
        if(mState.getType() == ActionState.TABLET){
            return wasDualPanel() && hasContentToShow();
        }else{
            return savedInstanceState != null;
        }
    }

    public boolean hasToSwapContext(){
        if(mState.getType() == ActionState.TABLET){
            return hasContentToShow();
        }else{
            return false;
        }
    }

    public boolean isDualPanel(){
        return mState.isDualPanel();
    }

    public boolean hasContentToShow(){
        return mState.hasContentToShow();
    }

    public int getVisiblePositionInTweetList() {
        return mVisiblePositionInTweetList;
    }

    public void setVisiblePositionInTweetList(int visiblePositionInTweetList) {
        mVisiblePositionInTweetList = visiblePositionInTweetList;
    }
}
