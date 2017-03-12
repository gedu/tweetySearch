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

package com.gemapps.tweetysearch.ui.mainsearch.presenter;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.internal.RealmCore;
import io.realm.log.RealmLog;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;


/**
 * Created by edu on 3/12/17.
 */

public class MockRealmUtil {

    public static Realm getMockRealm(){
        // Setup Realm to be mocked. The order of these matters
        PowerMockito.mockStatic(RealmCore.class);
        PowerMockito.mockStatic(RealmLog.class);
        PowerMockito.mockStatic(Realm.class);
        PowerMockito.mockStatic(RealmConfiguration.class);

        // Create the mock
        final Realm mockRealm = Mockito.mock(Realm.class);

        // Anytime getInstance is called with any configuration, then return the mockRealm
        Mockito.when(Realm.getDefaultInstance()).thenReturn(mockRealm);

        return mockRealm;
    }
}
