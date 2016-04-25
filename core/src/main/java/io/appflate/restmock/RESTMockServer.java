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
import com.squareup.okhttp.mockwebserver.RecordedRequest;

import org.hamcrest.Matcher;

import java.io.IOException;

import io.appflate.restmock.logging.NOOpLogger;
import io.appflate.restmock.logging.RESTMockLogger;
import io.appflate.restmock.utils.RequestMatcher;

import static io.appflate.restmock.utils.RequestMatchers.isDELETE;
import static io.appflate.restmock.utils.RequestMatchers.isGET;
import static io.appflate.restmock.utils.RequestMatchers.isPATCH;
import static io.appflate.restmock.utils.RequestMatchers.isPOST;
import static io.appflate.restmock.utils.RequestMatchers.isPUT;
import static org.hamcrest.core.AllOf.allOf;


public class RESTMockServer {

    private static MockWebServer mockWebServer;
    private static MatchableCallsRequestDispatcher dispatcher;
    private static String serverBaseUrl;
    private static RESTMockFileParser RESTMockFileParser;
    static RESTMockLogger logger;

    public static void init(RESTMockFileParser RESTMockFileParser,
                            RESTMockLogger logger) throws IOException {
        RESTMockServer.mockWebServer = new MockWebServer();
        if (logger == null) {
            RESTMockServer.logger = new NOOpLogger();
        } else {
            RESTMockServer.logger = logger;
        }
        RESTMockServer.logger.log("## Starting RESTMock server...");
        RESTMockServer.dispatcher = new MatchableCallsRequestDispatcher();
        RESTMockServer.mockWebServer.setDispatcher(dispatcher);
        RESTMockServer.mockWebServer.start();
        RESTMockServer.serverBaseUrl = mockWebServer.url("/").toString();

        RESTMockServer.RESTMockFileParser = RESTMockFileParser;
        RESTMockServer.logger.log("## RESTMock successfully started!\turl: " + RESTMockServer.serverBaseUrl);
    }

    /**
     * Enables logging for the RESTMock
     * @param logger a logger that will be responsible for logging. for Android use AndroidLogger from "com.github.andrzejchm.RESTMock:android" dependency
     */
    public static void enableLogging(RESTMockLogger logger) {
        RESTMockServer.logger = logger;
    }

    /**
     * Disables logging for the RESTMock
     */
    public static void disableLogging() {
        RESTMockServer.logger = new NOOpLogger();
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
     *
     * @param call {@code MatchableCall} to be removed
     * @return true if the {@code MatchableCall} was successfuly removed, false if it was not found
     */
    public static boolean removeMatchableCall(MatchableCall call) {
        return dispatcher.removeMatchableCall(call);
    }

    /**
     * replaces {@code call} with {@code replacement} in this {@code RESTMockServer}
     *
     * @param call        {@code MatchableCall} to be removed from {@code RESTMockServer}
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
     *
     * @param call to be added to this {@code RESTMockServer}
     */
    public static void addMatchableCall(final MatchableCall call) {
        dispatcher.addMatchableCall(call);
    }


    /**
     * Helper method to create MatchableCall that will be matched only for GET requests along with the specified {@code requestMatcher}
     *
     * @param requestMatcher matcher to match a GET request
     * @return {@code MatchableCall} that will match GET requests along with {@code requestMatcher}
     */
    public static MatchableCall whenGET(Matcher<RecordedRequest> requestMatcher) {
        return RESTMockServer.whenRequested(allOf(isGET(), requestMatcher));
    }

    /**
     * Helper method to create MatchableCall that will be matched only for POST requests along with the specified {@code requestMatcher}
     *
     * @param requestMatcher matcher to match a POST request
     * @return {@code MatchableCall} that will match POST requests along with {@code requestMatcher}
     */
    public static MatchableCall whenPOST(Matcher<RecordedRequest>  requestMatcher) {
        return RESTMockServer.whenRequested(allOf(isPOST(), requestMatcher));
    }

    /**
     * Helper method to create MatchableCall that will be matched only for PUT requests along with the specified {@code requestMatcher}
     *
     * @param requestMatcher matcher to match a PUT request
     * @return {@code MatchableCall} that will match PUT requests along with {@code requestMatcher}
     */
    public static MatchableCall whenPUT(Matcher<RecordedRequest>  requestMatcher) {
        return RESTMockServer.whenRequested(allOf(isPUT(), requestMatcher));
    }

    /**
     * Helper method to create MatchableCall that will be matched only for PATCH requests along with the specified {@code requestMatcher}
     *
     * @param requestMatcher matcher to match a PATCH request
     * @return {@code MatchableCall} that will match PATCH requests along with {@code requestMatcher}
     */
    public static MatchableCall whenPATCH(Matcher<RecordedRequest>  requestMatcher) {
        return RESTMockServer.whenRequested(allOf(isPATCH(), requestMatcher));
    }

    /**
     * Helper method to create MatchableCall that will be matched only for DELETE requests along with the specified {@code requestMatcher}
     *
     * @param requestMatcher matcher to match a DELETE request
     * @return {@code MatchableCall} that will match DELETE requests along with {@code requestMatcher}
     */
    public static MatchableCall whenDELETE(Matcher<RecordedRequest>  requestMatcher) {
        return RESTMockServer.whenRequested(allOf(isDELETE(), requestMatcher));
    }

    /**
     * Creates a new {@link MatchableCall} for a given {@code requestMatcher}.
     * In order to schedule this call within this {@code RESTMockServer},
     * be sure to call one of the returned {@code MatchableCall}'s {@code thenReturn*} methods
     *
     * @param requestMatcher a request matcher to match a HTTP request
     * @return a MatchableCall that will get matched by the {@code requestMatcher}
     */
    public static MatchableCall whenRequested(Matcher<RecordedRequest> requestMatcher) {
        return new MatchableCall(RESTMockServer.RESTMockFileParser, requestMatcher, dispatcher);
    }
}
