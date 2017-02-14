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

package com.gemapps.tweetysearch.ui.resultsearch;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.gemapps.tweetysearch.R;

public class ResultSearchActivity extends AppCompatActivity {

    private static final String TAG = "ResultSearchActivity";
    private static final String RESULT_FRAGMENT_TAG = "tweety.RESULT_FRAGMENT_TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_search);
        setupFragment(savedInstanceState);
    }

    private void setupFragment(Bundle savedInstanceState){
        if(savedInstanceState == null){
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.main_fragment_container,
                            ResultSearchFragment.newInstance(),
                            RESULT_FRAGMENT_TAG)
                    .commit();
        }
    }

}
