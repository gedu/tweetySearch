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

import com.gemapps.tweetysearch.networking.model.NetworkResponseBridge;
import com.gemapps.tweetysearch.ui.model.TweetCollection;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by edu on 2/25/17.
 */

public class EventUtil {

    public static void sendNetworkEvent(int tag, TweetCollection tweets){
        EventBus.getDefault().post(new NetworkResponseBridge<>(tag, tweets));
    }

    public static void sendErrorEvent(){
        EventBus.getDefault()
                .post(new NetworkResponseBridge<>(NetworkResponseBridge.TWEETS_LOAD_ERROR, null));
    }

    public static void sendSearchAndNotSaveEvent(TweetCollection tweets){
        EventBus.getDefault()
                .post(new NetworkResponseBridge<>(NetworkResponseBridge.TWEETS_SEARCH_NOT_SAVE, tweets));
    }
}
