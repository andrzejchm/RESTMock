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

package io.appflate.restmock.utils;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import io.appflate.restmock.RESTMockFileParser;
import okhttp3.mockwebserver.MockResponse;

public final class RestMockUtils {

    public static MockResponse createResponseFromFile(RESTMockFileParser RESTMockFileParser, String jsonFilePath,
                                                      int responseCode) throws Exception {
        String fileContents = RESTMockFileParser.readJsonFile(jsonFilePath);
        return new MockResponse().setResponseCode(responseCode).setBody(fileContents);
    }

    /**
     * Extract query parameters from a {@link URL}.
     *
     * @param url The {@link URL} to retrieve query parameters for.
     * @return A {@link List} of {@link QueryParam} objects. Each parameter has one key, and zero or
     * more values.
     * @throws UnsupportedEncodingException If unable to decode from UTF-8. This should never happen.
     */
    public static List<QueryParam> splitQuery(URL url) throws UnsupportedEncodingException {
        final Map<String, List<String>> queryPairs = new LinkedHashMap<>();
        final String[] pairs = url.getQuery().split("&");
        for (String pair : pairs) {
            final int idx = pair.indexOf("=");
            final String key = idx > 0 ? URLDecoder.decode(pair.substring(0, idx), "UTF-8") : pair;
            List<String> valueList = new LinkedList<>();

            if (queryPairs.containsKey(key)) {
                valueList = queryPairs.get(key);
            }

            final String value = idx > 0 && pair.length() > idx + 1 ? URLDecoder.decode(pair.substring(idx + 1), "UTF-8") : null;
            valueList.add(value);

            queryPairs.put(key, valueList);
        }

        List<QueryParam> finalParamList = new LinkedList<>();
        for (Map.Entry<String, List<String>> entry : queryPairs.entrySet()) {
            QueryParam nextFinalParam = new QueryParam(entry.getKey(), entry.getValue());
            finalParamList.add(nextFinalParam);
        }

        return finalParamList;
    }

    private RestMockUtils() {
        throw new UnsupportedOperationException("(╯‵Д′)╯ PLEASE STAHP!");

    }
}
