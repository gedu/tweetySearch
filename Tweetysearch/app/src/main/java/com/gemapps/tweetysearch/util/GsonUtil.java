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

package com.gemapps.tweetysearch.util;

import com.gemapps.tweetysearch.networking.model.Bearer;
import com.gemapps.tweetysearch.ui.model.TweetCollection;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by edu on 2/13/17.
 */

public class GsonUtil {

    public static final Gson BEARER_GSON = new GsonBuilder().registerTypeAdapter(Bearer.class,
            new TwitterDeserializer<Bearer>(null)).create();

    public static final Gson TWEETS_GSON = new GsonBuilder().registerTypeAdapter(TweetCollection.class,
            new TweetsDeserializer()).create();
}
