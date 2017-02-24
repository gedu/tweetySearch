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

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gemapps.tweetysearch.R;
import com.gemapps.tweetysearch.ui.butter.ButterViewHolder;
import com.gemapps.tweetysearch.ui.model.MediaEntity;
import com.gemapps.tweetysearch.ui.model.TweetItem;
import com.gemapps.tweetysearch.ui.widget.LinkClickTransformation;
import com.gemapps.tweetysearch.util.DateUtil;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import io.realm.RealmList;

/**
 * Created by edu on 2/14/17.
 */
public class ResultRecyclerAdapter extends RecyclerView.Adapter<ButterViewHolder> {

    private static final String TAG = "ResultRecyclerAdapter";
    private static final int VIEW_LOADING_TYPE = 0;
    private static final int VIEW_TWEET_TYPE = 1;
    private Context mContext;
    private RealmList<TweetItem> mTweetItems;
    private TweetItem mProgressItem;//TODO: update this to manage in a better way

    public ResultRecyclerAdapter(Context context, RealmList<TweetItem> items) {
        mContext = context;
        this.mTweetItems = items;
        mProgressItem = new TweetItem();
    }

    @Override
    public ButterViewHolder onCreateViewHolder(ViewGroup parent,
                                              int viewType) {
        if(viewType == VIEW_TWEET_TYPE) {
            View tweetView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.result_item_list, parent, false);
            return new TweetViewHolder(tweetView);
        }else{
            View progressView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.bottom_progress_item_list, parent, false);
            return new LoadingViewHolder(progressView);
        }
    }

    @Override
    public void onBindViewHolder(ButterViewHolder holder, int position) {

        if(holder instanceof TweetViewHolder) {
            TweetViewHolder tweetHolder = (TweetViewHolder) holder;
            TweetItem item = mTweetItems.get(position);

            tweetHolder.mUserNameText.setText(item.getUser().getName());
            tweetHolder.mTweetCreatedDateText.setText(DateUtil.formatDayMonthFrom(item.getCreatedAt()));
            tweetHolder.mTweetDescriptionText.setText(item.getText());
            tweetHolder.mUserFavouriteCountText.setText(item.getUser().getFollowersCount());
            loadImageInto(tweetHolder.mUserAvatarImage, item.getUser().getProfileImageUrl());
            if(item.getEntity() != null && item.getEntity().hasMedia()) {
                tweetHolder.mTweetImages.setVisibility(View.VISIBLE);
                loadImageInto(tweetHolder.mTweetImages,
                        item.getEntity().getMediaEntity().get(0).getMediaUrl());


                if(item.getEntity().getMediaEntity().size() > 2){
                    for (MediaEntity me :
                            item.getEntity().getMediaEntity()) {
                        Log.d(TAG, "onBindViewHolder: MEDIA URL: "+me.getMediaUrl());
                    }
                }
            }
        }
    }

    private void loadImageInto(ImageView target, String imageUrl){
        Picasso.with(mContext)
                .load(imageUrl)
                .placeholder(R.drawable.ic_landscape_black_24px)
                .into(target);
    }

    @Override
    public int getItemCount() {
        return (mTweetItems == null) ? 0 : mTweetItems.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mTweetItems.get(position).getUser() == null ? VIEW_LOADING_TYPE : VIEW_TWEET_TYPE;
    }

    public void addTweetsAtEnd(RealmList<TweetItem> newTweets) {
        int insertedPosition = getItemCount();
        Log.d(TAG, "addTweetsAtEnd: insertedPos "+insertedPosition+" size: "+newTweets.size() );
        mTweetItems.addAll(newTweets);
        notifyItemRangeInserted(insertedPosition, newTweets.size());
    }

    public void addTweetsAtStart(RealmList<TweetItem> newTweets) {
        mTweetItems.addAll(0, newTweets);
        Log.d(TAG, "addTweetsAtStart: SIZE: "+newTweets.size());
        notifyItemRangeInserted(0, newTweets.size());
    }

    public void addProgressItem(){
        mTweetItems.add(mProgressItem);
        notifyItemInserted(mTweetItems.size());
    }

    public void removeProgressItem(){
        mTweetItems.remove(mProgressItem);
        notifyItemRemoved(mTweetItems.size() + 1);
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
        @BindView(R.id.tweet_picture_image)
        ImageView mTweetImages;

        public TweetViewHolder(View view) {
            super(view);
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                mTweetDescriptionText
                        .setTransformationMethod(new LinkClickTransformation((Activity) mContext));
            }
            mTweetDescriptionText.setMovementMethod(LinkMovementMethod.getInstance());
//            Linkify.addLinks(mTweetDescriptionText, Linkify.WEB_URLS);
        }
    }

    class LoadingViewHolder extends ButterViewHolder {

        @BindView(R.id.progressBar)
        ProgressBar mProgressBar;

        public LoadingViewHolder(View itemView) {
            super(itemView);
        }
    }
}