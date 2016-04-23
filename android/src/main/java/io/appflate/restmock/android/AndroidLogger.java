/*
 * Copyright (C) 2016 Appflate.io
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.appflate.restmock.android;

import android.util.Log;

import io.appflate.restmock.logging.RESTMockLogger;

/**
 * Created by andrzejchm on 23/04/16.
 */
public class AndroidLogger implements RESTMockLogger {
    private static final String TAG_RESTMOCK = "RESTMock";

    @Override
    public void log(String message) {
        Log.d(TAG_RESTMOCK, message);
    }

    @Override
    public void error(String errorMessage) {
        Log.e(TAG_RESTMOCK, errorMessage);
    }

    @Override
    public void error(String errorMessage,
                      Throwable exception) {
        Log.e(TAG_RESTMOCK, errorMessage, exception);
    }
}
