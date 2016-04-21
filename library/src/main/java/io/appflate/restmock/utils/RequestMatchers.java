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

import com.squareup.okhttp.mockwebserver.RecordedRequest;

import org.hamcrest.Matcher;

public class RequestMatchers {
    private RequestMatchers() {
        throw new UnsupportedOperationException();
    }

    public static RequestMatcher pathContains(final String urlPart) {
        return new RequestMatcher("url contains: " + urlPart) {
            @Override
            protected boolean matchesSafely(RecordedRequest item) {
                return item.getPath().toLowerCase().contains(urlPart.toLowerCase());
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
                return itemPathWithoutEndingSlash.toLowerCase().endsWith(urlPartWithoutEndingSlash.toLowerCase());
            }
        };
    }

    public static RequestMatcher pathStartsWith(final String urlPart) {
        return new RequestMatcher("url starts with: " + urlPart) {
            @Override
            protected boolean matchesSafely(RecordedRequest item) {
                return item.getPath().toLowerCase().startsWith(urlPart.toLowerCase());
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

    public static Matcher<? super RecordedRequest> isGET() {
        return httpMethodIs("GET");
    }

    public static Matcher<? super RecordedRequest> isPOST() {
        return httpMethodIs("POST");
    }

    public static Matcher<? super RecordedRequest> isPATCH() {
        return httpMethodIs("PATCH");
    }

    public static Matcher<? super RecordedRequest> isDELETE() {
        return httpMethodIs("DELETE");
    }

    public static Matcher<? super RecordedRequest> isPUT() {
        return httpMethodIs("PUT");
    }
}