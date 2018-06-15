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

import android.app.Application;

import java.io.IOException;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.appflate.restmock.RESTMockServer;
import io.appflate.restmock.RESTMockServerStarter;
import io.appflate.restmock.android.AndroidAssetsFileParser;
import io.appflate.restmock.android.AndroidLogger;
import io.appflate.restmock.androidsample.di.AppComponent;
import io.appflate.restmock.androidsample.di.AppModule;
import io.appflate.restmock.androidsample.di.DaggerAppComponent;
import io.appflate.restmock.utils.RequestMatchers;

/**
 * Created by andrzejchm on 22/04/16.
 */
public class SampleApplication extends Application {
    private static final String BASE_URL = "https://api.github.com/";
    static AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        RESTMockServerStarter.startSync(new AndroidAssetsFileParser(this), new AndroidLogger());


        RESTMockServer.whenGET(RequestMatchers.pathEndsWith("users/andrzejchm"))
                      .thenReturnFile(200, "mocks/users/andrzejchm/index.json");

        RESTMockServer.whenGET(RequestMatchers.pathEndsWith("users/andrzejchm/repos"))
                      .thenReturnFile(200, "mocks/users/andrzejchm/repos.json");


        setupGraph();
    }

    @SuppressFBWarnings("ST_WRITE_TO_STATIC_FROM_INSTANCE_METHOD")
    protected void setupGraph() {
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(RESTMockServer.getUrl()))
                .build();
    }

    public static AppComponent getComponent() {
        return appComponent;
    }

    @Override
    public void onTerminate() {
        try {
            RESTMockServer.shutdown();
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.onTerminate();
    }
}
