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
package io.appflate.restmock.androidsample;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.appflate.restmock.RESTMockServer;
import io.appflate.restmock.androidsample.di.AppModule;
import io.appflate.restmock.androidsample.di.DaggerAppComponent;

/**
 * An derivative of {@link SampleApplication} that sets up an environment for testing using RESTMock
 * and the {@link io.appflate.restmock.android.AndroidLocalFileParser}.
 */
public class RobolectricTestApplication extends SampleApplication {

  @Override
  @SuppressFBWarnings("ST_WRITE_TO_STATIC_FROM_INSTANCE_METHOD")
  protected void setupGraph() {
    appComponent = DaggerAppComponent.builder()
                                     .appModule(new AppModule(RESTMockServer.getUrl()))
                                     .build();
  }
}
