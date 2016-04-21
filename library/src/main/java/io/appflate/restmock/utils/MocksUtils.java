
/*
 * Copyright (C) 2016 Appflate
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

package io.appflate.restmock.utils;

import com.squareup.okhttp.mockwebserver.MockResponse;

import java.io.IOException;

import io.appflate.restmock.MocksFileParser;

public class MocksUtils {
    public static MockResponse createResponseFromFile(MocksFileParser mocksFileParser,
                                                      String jsonFilePath,
                                                      int responseCode)
            throws IOException {
        String fileContents = mocksFileParser.readJsonFile(jsonFilePath);
        return new MockResponse().setResponseCode(responseCode).setBody(fileContents);
    }
}
