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

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gemapps.tweetysearch.R;
import com.gemapps.tweetysearch.networking.searchquery.RecentlySearchedItem;
import com.gemapps.tweetysearch.ui.butter.ButterViewHolder;

import butterknife.BindView;
import butterknife.OnClick;
import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

/**
 * Created by edu on 2/23/17.
 */

public class RecentlySearchedAdapter
        extends RealmRecyclerViewAdapter<RecentlySearchedItem, RecentlySearchedAdapter.RecentlyViewHolder> {

    private static final String TAG = "RecentlySearchedAdapter";
    public interface RecentlySearchedListener {
        void onClicked(int position);

        void onDeleted(int position);
    }

    private RecentlySearchedListener mListener;

    public RecentlySearchedAdapter(@NonNull Context context,
                                   @Nullable OrderedRealmCollection<RecentlySearchedItem> data,
                                   RecentlySearchedListener listener) {
        super(context, data, true);
        mListener = listener;
    }

    @Override
    public RecentlyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recently_searched_item_list, parent, false);
        return new RecentlyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecentlyViewHolder holder, int position) {

        try {
            setupViewUsing(holder, getContentAt(position));
        }catch (NullPointerException e){
            Log.w(TAG, "Failed to get OBJ at position: "+position, e);
        }
    }

    private RecentlySearchedItem getContentAt(int position) throws NullPointerException {
        return getData().get(position);
    }

    private void setupViewUsing(RecentlyViewHolder holder, final RecentlySearchedItem searchedItem){

        holder.mSearchedQueryText.setText(searchedItem.getUrlParams());
    }

    class RecentlyViewHolder extends ButterViewHolder
            implements View.OnClickListener {

        @BindView(R.id.searched_query_text)
        TextView mSearchedQueryText;

        public RecentlyViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(mListener != null) mListener.onClicked(getAdapterPosition());
        }

        @OnClick(R.id.delete_button)
        public void onClick() {
            if(mListener != null) mListener.onDeleted(getAdapterPosition());
        }

    }
}
