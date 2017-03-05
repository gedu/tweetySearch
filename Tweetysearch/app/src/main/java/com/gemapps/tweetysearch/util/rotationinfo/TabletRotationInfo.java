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

package com.gemapps.tweetysearch.util.rotationinfo;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.view.Surface;

/**
 * Created by edu on 3/5/17.
 */

public class TabletRotationInfo implements Rotatable {

    @Override
    public void lockInCurrentRotation(Activity activity) {
        int rotationToLock;
        int currentRotation = activity.getWindowManager()
                .getDefaultDisplay().getRotation();
        switch (currentRotation){
            case Surface.ROTATION_90: rotationToLock = ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT;
                break;
            case Surface.ROTATION_180: rotationToLock = ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;
                break;
            case Surface.ROTATION_270: rotationToLock = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
                break;
            default: rotationToLock = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
        }
        activity.setRequestedOrientation(rotationToLock);
    }

    @Override
    public void enableFullRotation(Activity activity) {
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
    }
}
