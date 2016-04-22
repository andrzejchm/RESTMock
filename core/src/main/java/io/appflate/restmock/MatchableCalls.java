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

package io.appflate.restmock;

import org.hamcrest.Matcher;

import io.appflate.restmock.utils.RequestMatcher;

import static io.appflate.restmock.utils.RequestMatchers.isDELETE;
import static io.appflate.restmock.utils.RequestMatchers.isGET;
import static io.appflate.restmock.utils.RequestMatchers.isPATCH;
import static io.appflate.restmock.utils.RequestMatchers.isPOST;
import static io.appflate.restmock.utils.RequestMatchers.isPUT;
import static org.hamcrest.core.AllOf.allOf;


/**
 * Util class for convienient creating {@link MatchableCall} instances.
 */
public final class MatchableCalls {

    private MatchableCalls() {
        throw new UnsupportedOperationException("ლ(ಠ益ಠ)ლ what have You done?!");
    }

    /**
     * Helper method to create MatchableCall that will be matched only for GET requests along with the specified {@code requestMatcher}
     * @param requestMatcher matcher to match a GET request
     * @return {@code MatchableCall} that will match GET requests along with {@code requestMatcher}
     */
    public static MatchableCall whenGET(RequestMatcher requestMatcher) {
        return RESTMockServer.whenRequested((RequestMatcher) allOf(new Matcher[]{isGET(), requestMatcher}));
    }

    /**
     * Helper method to create MatchableCall that will be matched only for POST requests along with the specified {@code requestMatcher}
     * @param requestMatcher matcher to match a POST request
     * @return {@code MatchableCall} that will match POST requests along with {@code requestMatcher}
     */
    public static MatchableCall whenPOST(RequestMatcher requestMatcher) {
        return RESTMockServer.whenRequested((RequestMatcher) allOf(new Matcher[]{isPOST(), requestMatcher}));
    }
    /**
     * Helper method to create MatchableCall that will be matched only for PUT requests along with the specified {@code requestMatcher}
     * @param requestMatcher matcher to match a PUT request
     * @return {@code MatchableCall} that will match PUT requests along with {@code requestMatcher}
     */
    public static MatchableCall whenPUT(RequestMatcher requestMatcher) {
        return RESTMockServer.whenRequested((RequestMatcher) allOf(new Matcher[]{isPUT(), requestMatcher}));
    }
    /**
     * Helper method to create MatchableCall that will be matched only for PATCH requests along with the specified {@code requestMatcher}
     * @param requestMatcher matcher to match a PATCH request
     * @return {@code MatchableCall} that will match PATCH requests along with {@code requestMatcher}
     */
    public static MatchableCall whenPATCH(RequestMatcher requestMatcher) {
        return RESTMockServer.whenRequested((RequestMatcher) allOf(new Matcher[]{isPATCH(), requestMatcher}));
    }
    /**
     * Helper method to create MatchableCall that will be matched only for DELETE requests along with the specified {@code requestMatcher}
     * @param requestMatcher matcher to match a DELETE request
     * @return {@code MatchableCall} that will match DELETE requests along with {@code requestMatcher}
     */
    public static MatchableCall whenDELETE(RequestMatcher requestMatcher) {
        return RESTMockServer.whenRequested((RequestMatcher) allOf(new Matcher[]{isDELETE(), requestMatcher}));
    }
}
