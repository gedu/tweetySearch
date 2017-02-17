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

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by edu on 2/16/17.
 */

public class DateUtil {

    private static final String TAG = "DateUtil";
    private static final SimpleDateFormat DAY_MONTH_DATE_FORMAT = new SimpleDateFormat("dd MMM", Locale.US);
    private static final SimpleDateFormat UTC_DATE_FORMAT = new SimpleDateFormat("EEE MMM w HH:mm:ss z yyyy");
    static {
        UTC_DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    public static String formatDayMonthFrom(long ts){
        return DAY_MONTH_DATE_FORMAT.format(new Date(ts));
    }

    public static String formatDayMonthFrom(String utcDate){
        try {
            return formatDayMonthFrom(UTC_DATE_FORMAT.parse(utcDate).getTime());
        } catch (ParseException e) {
            Log.w(TAG, "formatDayMonthFrom with String Failed! ", e);
            return utcDate.substring(0, 10);
        }
    };
}
