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

import android.os.Bundle;
import android.support.test.runner.AndroidJUnitRunner;
import io.appflate.restmock.RESTMockOptions;
import io.appflate.restmock.RESTMockServerStarter;

/**
 * Created by andrzejchm on 22/04/16.
 */
public class RESTMockTestRunner extends AndroidJUnitRunner {

    @Override
    public void onCreate(Bundle arguments) {
        super.onCreate(arguments);
        RESTMockServerStarter.startSync(new AndroidAssetsFileParser(getContext()), new AndroidLogger(),
            new RESTMockOptions.Builder()
                .useHttps(true)
                .build());
    }
}
