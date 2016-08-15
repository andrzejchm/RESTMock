
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

    private RestMockUtils() {
        throw new UnsupportedOperationException("(╯‵Д′)╯ PLEASE STAHP!");

    }

    public static MockResponse createResponseFromFile(RESTMockFileParser RESTMockFileParser,
                                                      String jsonFilePath,
                                                      int responseCode)
            throws Exception {
        String fileContents = RESTMockFileParser.readJsonFile(jsonFilePath);
        return new MockResponse().setResponseCode(responseCode).setBody(fileContents);
    }

    /**
     * Extract query parameters from a {@link URL}.
     *
     * Note: Gratuitously stolen from http://stackoverflow.com/a/13592567/281460
     *
     * @param url The {@link URL} to retrieve query parameters for.
     *
     * @return A {@link Map} of {@link String} key / a {@link List} of {@link String} value pairs
     *         appearing in the query parameters of the given URL. This will never be null.
     *
     * @throws UnsupportedEncodingException If unable to decode from UTF-8. This should never happen.
     */
    public static Map<String, List<String>> splitQuery(URL url) throws UnsupportedEncodingException {
      final Map<String, List<String>> query_pairs = new LinkedHashMap<String, List<String>>();
      final String[] pairs = url.getQuery().split("&");
      for (String pair : pairs) {
        final int idx = pair.indexOf("=");
        final String key = idx > 0 ? URLDecoder.decode(pair.substring(0, idx), "UTF-8") : pair;
        if (!query_pairs.containsKey(key)) {
          query_pairs.put(key, new LinkedList<String>());
        }
        final String value = idx > 0 && pair.length() > idx + 1 ? URLDecoder.decode(pair.substring(idx + 1), "UTF-8") : null;
        query_pairs.get(key).add(value);
      }
      return query_pairs;
    }
}
