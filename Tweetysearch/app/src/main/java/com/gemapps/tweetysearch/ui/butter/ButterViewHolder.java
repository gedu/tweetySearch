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

import android.support.v7.widget.RecyclerView;
import android.view.View;

import butterknife.ButterKnife;

/**
 * Created by edu on 2/14/17.
 */

public class ButterViewHolder extends RecyclerView.ViewHolder {
    public ButterViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}