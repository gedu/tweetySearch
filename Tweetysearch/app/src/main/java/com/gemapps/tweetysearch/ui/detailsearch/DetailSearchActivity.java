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

package com.gemapps.tweetysearch.ui.detailsearch;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.gemapps.tweetysearch.R;
import com.gemapps.tweetysearch.ui.widget.BaseCardActivity;
import com.gemapps.tweetysearch.util.rotationinfo.Rotatable;
import com.gemapps.tweetysearch.util.rotationinfo.RotationFactory;

public class DetailSearchActivity extends BaseCardActivity {

    private static final String TAG = "DetailSearchActivity";
    public static final String BUNDLE_DATA_KEY = "tweety.BUNDLE_DATA_KEY";

    private Rotatable mRotationInfo;

    public static Intent getInstance(View view, Bundle data, Class<?> className){
        Intent intent = BaseCardActivity.getInstance(view, className);
        intent.putExtra(BUNDLE_DATA_KEY, data);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_search);
        loackRotation();
        doEntryAnimation();
        overrideTrans();
    }

    private void loackRotation(){
        mRotationInfo = RotationFactory.getRotationInfo(this);
        mRotationInfo.lockInCurrentRotation(this);
    }

    @Override
    protected void onPause() {
        mRotationInfo.enableFullRotation(this);
        super.onPause();
    }
}
