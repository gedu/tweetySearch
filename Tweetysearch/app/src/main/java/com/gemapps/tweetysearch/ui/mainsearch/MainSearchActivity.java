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

package com.gemapps.tweetysearch.ui.mainsearch;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.gemapps.tweetysearch.R;
import com.gemapps.tweetysearch.networking.TwitterSearchManager;
import com.gemapps.tweetysearch.networking.searchquery.UrlParameter;
import com.gemapps.tweetysearch.ui.resultsearch.ResultSearchActivity;

public class MainSearchActivity extends AppCompatActivity
        implements MainSearchFragment.OnSearchListener {

    private static final String TAG = "MainSearchActivity";
    private static final String SEARCH_FRAGMENT_TAG = "tweety.SEARCH_FRAGMENT_TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_search);
        addSearchContent(savedInstanceState);
    }

    private void addSearchContent(Bundle savedInstanceState){

        if(savedInstanceState == null){
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.main_fragment_container,
                            MainSearchFragment.newInstance(),
                            SEARCH_FRAGMENT_TAG)
                    .commit();
        }
    }

    @Override
    public void onSearch(UrlParameter urlParameter) {
        TwitterSearchManager.getInstance().search(urlParameter);
        startActivity(new Intent(this, ResultSearchActivity.class));
    }
}
