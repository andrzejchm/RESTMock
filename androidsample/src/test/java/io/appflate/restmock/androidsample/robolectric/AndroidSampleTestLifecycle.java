/*
 *	Copyright (C) 2016 Scott Johnson, jaywir3@gmail.com
 *
 *	Licensed under the Apache License, Version 2.0 (the "License");
 *	you may not use this file except in compliance with the License.
 *	You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *	distributed under the License is distributed on an "AS IS" BASIS,
 * 	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * 	See the License for the specific language governing permissions and
 *	limitations under the License.
 *
 */
package io.appflate.restmock.androidsample.robolectric;

import android.app.Application;

import org.robolectric.DefaultTestLifecycle;
import org.robolectric.annotation.Config;
import org.robolectric.manifest.AndroidManifest;

import java.lang.reflect.Method;

import io.appflate.restmock.RESTMockServer;
import io.appflate.restmock.RESTMockServerStarter;
import io.appflate.restmock.android.AndroidLocalFileParser;
import io.appflate.restmock.android.AndroidLogger;
import io.appflate.restmock.androidsample.SampleApplication;
import io.appflate.restmock.androidsample.di.AppComponent;
import io.appflate.restmock.androidsample.di.AppModule;
import io.appflate.restmock.androidsample.di.DaggerAppComponent;

/**
 * A {@link org.robolectric.TestLifecycle} implementation that sets up the dagger modules necessary
 * to use RESTMock.
 */
public class AndroidSampleTestLifecycle extends DefaultTestLifecycle {
  AppComponent appComponent;

  @Override
  public Application createApplication(Method method, AndroidManifest appManifest, Config config) {
    SampleApplication app = (SampleApplication)super.createApplication(method, appManifest, config);

    RESTMockServerStarter.startSync(new AndroidLocalFileParser(app), new AndroidLogger());

    appComponent = DaggerAppComponent.builder()
                                     .appModule(new AppModule(RESTMockServer.getUrl()))
                                     .build();


    return app;
  }
}
