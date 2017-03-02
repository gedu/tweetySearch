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

/**
 * Created by edu on 3/2/17.
 */

public class TabletState implements ActionState {

    private boolean mRebuildList = false;
    private boolean mWasDualPanel = false;
    private boolean mIsDualPanel = false;
    private boolean mHasContent = false;

    public void setRebuildState(boolean shouldRebuild){
        mRebuildList = shouldRebuild;
    }

    public boolean getRebuildState(){
        return mRebuildList;
    }

    @Override
    public int getType() {
        return TABLET;
    }

    @Override
    public boolean wasDualPanel() {
        return mWasDualPanel;
    }

    public boolean isDualPanel() {
        return mIsDualPanel;
    }

    @Override
    public boolean hasContentToShow() {
        return mHasContent;
    }

    public void setDualPanelState(boolean dualPanel) {
        mWasDualPanel = mIsDualPanel;
        mIsDualPanel = dualPanel;
    }

    @Override
    public void setHasContentToShow(boolean hasContent) {
        mHasContent = hasContent;
    }
}
