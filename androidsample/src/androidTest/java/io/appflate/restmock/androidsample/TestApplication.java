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

package io.appflate.restmock.androidsample;

import io.appflate.restmock.RESTMockServer;
import io.appflate.restmock.androidsample.di.AppModule;
import io.appflate.restmock.androidsample.di.DaggerAppComponent;

/**
 * Created by andrzejchm on 23/04/16.
 */
public class TestApplication extends SampleApplication {

    @Override
    protected void setupGraph() {
        //here I'm supplying the AppModule with RESTMock's url instead of github's API url
        appComponent = DaggerAppComponent.builder()
            .appModule(new AppModule(RESTMockServer.getUrl(), RESTMockServer.getSSLSocketFactory(), RESTMockServer.getTrustManager()))
            .build();
    }
}
