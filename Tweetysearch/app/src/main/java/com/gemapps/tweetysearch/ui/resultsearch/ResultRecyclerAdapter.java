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

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gemapps.tweetysearch.R;
import com.gemapps.tweetysearch.ui.butter.ButterViewHolder;
import com.gemapps.tweetysearch.ui.model.TweetItem;

import butterknife.BindView;
import io.realm.RealmList;

/**
 * Created by edu on 2/14/17.
 */
public class ResultRecyclerAdapter extends RecyclerView.Adapter<ResultRecyclerAdapter.TweetViewHolder> {
    private RealmList<TweetItem> mTweetItems;

    public ResultRecyclerAdapter(RealmList<TweetItem> items) {
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

        holder.mTitleView.setText(item.getText());
    }

    @Override
    public int getItemCount() {
        return (mTweetItems == null) ? 0 : mTweetItems.size();
    }

    public void addItems(RealmList<TweetItem> newItems){
        mTweetItems.addAll(newItems);
    }

    class TweetViewHolder extends ButterViewHolder {

        @BindView(R.id.result_title_text_view)
        TextView mTitleView;

        public TweetViewHolder(View view) {
            super(view);

        }
    }
}