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


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gemapps.tweetysearch.R;
import com.gemapps.tweetysearch.ui.butter.ButterFragment;
import com.gemapps.tweetysearch.ui.model.TweetItem;
import com.gemapps.tweetysearch.util.DateUtil;
import com.squareup.picasso.Picasso;

import butterknife.BindView;

import static com.gemapps.tweetysearch.ui.detailsearch.DetailSearchActivity.BUNDLE_DATA_KEY;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailSearchFragment extends ButterFragment {

    private static final String TAG = "DetailSearchFragment";

    @BindView(R.id.tweet_image)
    ImageView mImageView;
    @BindView(R.id.tweet_created_date_text)
    TextView mCreatedDateView;
    @BindView(R.id.user_name_text)
    TextView mUserNameView;

    private String mImageUrl;
    private String mUserName;
    private String mCreationDate;

    public DetailSearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getActivity().getIntent().getExtras();
        setupBundleValues(bundle.getBundle(BUNDLE_DATA_KEY));
    }

    private void setupBundleValues(Bundle tweetBundle){
        if(tweetBundle != null) {
            mImageUrl = tweetBundle.getString(TweetItem.TWEET_MEDIA_URL_KEY);
            mUserName = tweetBundle.getString(TweetItem.TWEET_USER_NAME_KEY);
            mCreationDate = tweetBundle.getString(TweetItem.TWEET_CREATION_DATE_KEY);
            mCreationDate = DateUtil.formatDayMonthFrom(mCreationDate);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return createView(inflater, container, R.layout.fragment_detail_search);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mUserNameView.setText(mUserName);
        mCreatedDateView.setText(mCreationDate);

        Picasso.with(getContext())
                .load(mImageUrl)
                .placeholder(R.color.grey_light_54)
                .into(mImageView);
    }
}
