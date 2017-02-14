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

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gemapps.tweetysearch.R;
import com.gemapps.tweetysearch.ui.butter.ButterViewHolder;
import com.gemapps.tweetysearch.ui.model.TweetItem;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import io.realm.RealmList;

/**
 * Created by edu on 2/14/17.
 */
public class ResultRecyclerAdapter extends RecyclerView.Adapter<ResultRecyclerAdapter.TweetViewHolder> {

    private Context mContext;
    private RealmList<TweetItem> mTweetItems;

    public ResultRecyclerAdapter(Context context, RealmList<TweetItem> items) {
        mContext = context;
        this.mTweetItems = items;
    }

    @Override
    public TweetViewHolder onCreateViewHolder(ViewGroup parent,
                                              int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.result_item_list, parent, false);
        return new TweetViewHolder(v);
    }

    @Override
    public void onBindViewHolder(TweetViewHolder holder, int position) {
        TweetItem item = mTweetItems.get(position);

        holder.mUserNameText.setText(item.getUser().getName());
        holder.mTweetCreatedDateText.setText(item.getCreatedAt());
        holder.mTweetDescriptionText.setText(item.getText());
        holder.mUserFavouriteCountText.setText(item.getUser().getFollowersCount());
        Picasso.with(mContext)
                .load(item.getUser().getProfileImageUrl())
                .placeholder(R.color.colorAccent)
                .into(holder.mUserAvatarImage);
    }

    @Override
    public int getItemCount() {
        return (mTweetItems == null) ? 0 : mTweetItems.size();
    }

    public void addItems(RealmList<TweetItem> newItems) {
        mTweetItems.addAll(newItems);
    }

    class TweetViewHolder extends ButterViewHolder {

        @BindView(R.id.user_avatar_image)
        ImageView mUserAvatarImage;
        @BindView(R.id.user_name_text)
        TextView mUserNameText;
        @BindView(R.id.tweet_description_text)
        TextView mTweetDescriptionText;
        @BindView(R.id.tweet_created_date_text)
        TextView mTweetCreatedDateText;
        @BindView(R.id.user_favourite_count_text)
        TextView mUserFavouriteCountText;

        public TweetViewHolder(View view) {
            super(view);
        }
    }
}