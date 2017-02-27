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

package com.gemapps.tweetysearch.ui.butter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.gemapps.tweetysearch.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by edu on 2/27/17.
 */

public class ButterActivity extends AppCompatActivity {

    private static final String TAG = "ButterActivity";
    @Nullable @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
        Log.d(TAG, "setContentView: mTOOLBAR: "+mToolbar);
        if(mToolbar != null) setSupportActionBar(mToolbar);
    }

    protected void setUpButtonToolbar(){
        if(getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    protected void setToolbarTitle(String title){
        if (getSupportActionBar() != null) getSupportActionBar().setTitle(title);
    }
}
