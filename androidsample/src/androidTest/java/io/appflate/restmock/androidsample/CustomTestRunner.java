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
import android.content.Context;

import io.appflate.restmock.android.RESTMockTestRunner;

/**
 * Here I'm extending RESTMockTestRunner to change the Application class used within app while testing.
 *
 * Created by andrzejchm on 23/04/16.
 */
public class CustomTestRunner extends RESTMockTestRunner {
    @Override
    public Application newApplication(ClassLoader cl,
                                      String className,
                                      Context context) throws InstantiationException, IllegalAccessException, ClassNotFoundException {

        //I'm changing the application class only for test purposes. there I'll instantiate AppModule with RESTMock's url.
        String testApplicationClassName = TestApplication.class.getCanonicalName();
        return super.newApplication(cl, testApplicationClassName, context);
    }
}
