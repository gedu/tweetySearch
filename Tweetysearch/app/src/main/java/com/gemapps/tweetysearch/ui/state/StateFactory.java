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

import com.gemapps.tweetysearch.R;

/**
 * Created by edu on 3/2/17.
 */

public class StateFactory {


    public static ActionState getDeviceState(Context context){
        boolean isPhone = context.getResources().getBoolean(R.bool.is_phone);
        if(isPhone) return new PhoneState();
        else return new TabletState();
    }
}
