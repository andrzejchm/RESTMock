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

import com.squareup.okhttp.mockwebserver.MockWebServer;

import org.hamcrest.Matcher;

import java.io.IOException;

import io.appflate.restmock.utils.RequestMatcher;

import static io.appflate.restmock.utils.RequestMatchers.isDELETE;
import static io.appflate.restmock.utils.RequestMatchers.isGET;
import static io.appflate.restmock.utils.RequestMatchers.isPATCH;
import static io.appflate.restmock.utils.RequestMatchers.isPOST;
import static io.appflate.restmock.utils.RequestMatchers.isPUT;
import static org.hamcrest.core.AllOf.allOf;


public class RESTMockServer {

    protected static MockWebServer mockWebServer;
    private static MatchableCallsRequestDispatcher dispatcher;
    private static String serverBaseUrl;
    private static RESTMockFileParser RESTMockFileParser;

    public static void init(RESTMockFileParser RESTMockFileParser) throws IOException {
        RESTMockServer.mockWebServer = new MockWebServer();
        RESTMockServer.mockWebServer.start();
        RESTMockServer.serverBaseUrl = mockWebServer.getUrl("").toString();
        RESTMockServer.dispatcher = new MatchableCallsRequestDispatcher();
        RESTMockServer.mockWebServer.setDispatcher(dispatcher);

        RESTMockServer.RESTMockFileParser = RESTMockFileParser;
    }

    private RESTMockServer() {

    }

    /**
     * removes all matchable calls stored in this {@code RESTMockServer}
     */
    public static void removeAllMatchableCalls() {
        dispatcher.removeAllMatchableCalls();
    }

    /**
     * removes the given {@code MatchableCall} from this {@code RESTMockServer}
     * @param call {@code MatchableCall} to be removed
     * @return true if the {@code MatchableCall} was successfuly removed, false if it was not found
     */
    public static boolean removeMatchableCall(MatchableCall call) {
        return dispatcher.removeMatchableCall(call);
    }

    /**
     * replaces {@code call} with {@code replacement} in this {@code RESTMockServer}
     * @param call {@code MatchableCall} to be removed from {@code RESTMockServer}
     * @param replacement {@code MatchableCall} to be added to {@code RESTMockServer}
     */
    public static void replaceMatchableCall(MatchableCall call,
                                            MatchableCall replacement) {
        removeMatchableCall(call);
        dispatcher.addMatchableCall(replacement);
    }

    /**
     * @return this {@code RESTMockServer} url to use as an endpoint in your tests, or null, if the instance wasn't started yet
     */
    public static String getUrl() {
        return serverBaseUrl;
    }

    /**
     * adds {@code call} to this {@code RESTMockServer}
     * @param call to be added to this {@code RESTMockServer}
     */
    public static void addMatchableCall(final MatchableCall call) {
        dispatcher.addMatchableCall(call);
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
    /**
     * Creates a new {@link MatchableCall} for a given {@code requestMatcher}.
     * In order to schedule this call within this {@code RESTMockServer},
     * be sure to call one of the returned {@code MatchableCall}'s {@code thenReturn*} methods
     *
     * @param requestMatcher a request matcher to match a HTTP request
     * @return a MatchableCall that will get matched by the {@code requestMatcher}
     */
    public static MatchableCall whenRequested(RequestMatcher requestMatcher) {
        return new MatchableCall(RESTMockServer.RESTMockFileParser, requestMatcher, dispatcher);
    }
}
