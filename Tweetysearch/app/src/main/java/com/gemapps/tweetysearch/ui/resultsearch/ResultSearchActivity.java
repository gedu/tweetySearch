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

import android.app.ActivityOptions;
import android.os.Bundle;
import android.view.View;

import com.gemapps.tweetysearch.R;
import com.gemapps.tweetysearch.networking.TwitterSearchManager;
import com.gemapps.tweetysearch.ui.butter.ButterActivity;
import com.gemapps.tweetysearch.ui.detailsearch.DetailSearchActivity;
import com.gemapps.tweetysearch.ui.model.TweetItem;
import com.gemapps.tweetysearch.util.Util;

public class ResultSearchActivity extends ButterActivity
        implements ResultSearchFragment.ResultSearchListener {

    private static final String TAG = "ResultSearchActivity";
    private static final String RESULT_FRAGMENT_TAG = "tweety.RESULT_FRAGMENT_TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_search);
        setupFragment(savedInstanceState);
        setupToolbar();
//        if (Util.isLollipop()) {
//            getWindow().setExitTransition(new Explode());
//        }
    }

    private void setupToolbar(){
        setUpButtonToolbar();
        setToolbarTitle(TwitterSearchManager.getInstance()
                .getSavableParameter().getHumanParams());
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

    @Override
    public void onImageClicked(View v, TweetItem tweet) {
        if (Util.isLollipop()) {
            startActivity(DetailSearchActivity.getInstance(v, tweet.bundleMainContent(), DetailSearchActivity.class),
                    ActivityOptions.makeSceneTransitionAnimation(this, v,
                            getResources().getString(R.string.tweet_pic_trans_name)).toBundle());
        }else{
            startActivity(DetailSearchActivity.getInstance(v, DetailSearchActivity.class));
        }
    }
}
