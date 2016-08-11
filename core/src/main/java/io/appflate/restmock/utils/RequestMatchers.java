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

import java.util.Locale;

import okhttp3.mockwebserver.RecordedRequest;

public class RequestMatchers {
    private RequestMatchers() {
        throw new UnsupportedOperationException();
    }

    public static RequestMatcher pathContains(final String urlPart) {
        return new RequestMatcher("url contains: " + urlPart) {
            @Override
            protected boolean matchesSafely(RecordedRequest item) {
                return item.getPath().toLowerCase(Locale.US).contains(urlPart.toLowerCase(Locale.US));
            }
        };
    }

    public static RequestMatcher pathDoesNotContain(final String urlPart) {
        return new RequestMatcher("url does not contain: " + urlPart) {
            @Override
            protected boolean matchesSafely(RecordedRequest item) {
                return !item.getPath().toLowerCase(Locale.US).contains(urlPart.toLowerCase(Locale.US));
            }
        };
    }

    public static RequestMatcher pathEndsWith(final String urlPart) {
        return new RequestMatcher("url ends with: " + urlPart) {
            @Override
            protected boolean matchesSafely(RecordedRequest item) {
                String urlPartWithoutEndingSlash =
                        urlPart.endsWith("/") ? urlPart.substring(0,
                                urlPart.length() - 1) : urlPart;
                String itemPathWithoutEndingSlash = item.getPath().endsWith("/") ? item.getPath()
                        .substring(0, item.getPath().length() - 1) : item.getPath();
                return itemPathWithoutEndingSlash.toLowerCase(Locale.US).endsWith(urlPartWithoutEndingSlash.toLowerCase(Locale.US));
            }
        };
    }

    public static RequestMatcher pathStartsWith(final String urlPart) {
        return new RequestMatcher("url starts with: " + urlPart) {
            @Override
            protected boolean matchesSafely(RecordedRequest item) {
                return item.getPath().toLowerCase(Locale.US).startsWith(urlPart.toLowerCase(Locale.US));
            }
        };
    }

    public static RequestMatcher httpMethodIs(final String method) {
        return new RequestMatcher("HTTP method is: " + method) {
            @Override
            protected boolean matchesSafely(final RecordedRequest item) {
                return item.getMethod().equalsIgnoreCase(method);
            }
        };
    }

    public static RequestMatcher isGET() {
        return httpMethodIs("GET");
    }

    public static RequestMatcher isPOST() {
        return httpMethodIs("POST");
    }

    public static RequestMatcher isPATCH() {
        return httpMethodIs("PATCH");
    }

    public static RequestMatcher isDELETE() {
        return httpMethodIs("DELETE");
    }

    public static RequestMatcher isPUT() {
        return httpMethodIs("PUT");
    }
}
