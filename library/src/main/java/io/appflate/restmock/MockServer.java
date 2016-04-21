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

package io.appflate.restmock;

import com.squareup.okhttp.mockwebserver.MockWebServer;
import com.squareup.okhttp.mockwebserver.RecordedRequest;

import org.hamcrest.Matcher;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static io.appflate.restmock.utils.RequestMatchers.isDELETE;
import static io.appflate.restmock.utils.RequestMatchers.isGET;
import static io.appflate.restmock.utils.RequestMatchers.isPATCH;
import static io.appflate.restmock.utils.RequestMatchers.isPOST;
import static io.appflate.restmock.utils.RequestMatchers.isPUT;
import static org.hamcrest.core.AllOf.allOf;


public class MockServer {

    private static MockServer instance;

    protected static MockWebServer mockWebServer;
    private static MatchableCallsRequestDispatcher dispatcher;
    private static String serverBaseUrl;
    private static MocksFileParser mocksFileParser;

    public static void init(MocksFileParser mocksFileParser) throws IOException {
        new MockServer(mocksFileParser);
    }

    private MockServer(MocksFileParser mocksFileParser) throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
        serverBaseUrl = mockWebServer.getUrl("").toString();
        dispatcher = new MatchableCallsRequestDispatcher();
        mockWebServer.setDispatcher(dispatcher);
        try {
            RecordedRequest request = takeLastRequest();
            while (request != null) {
                request = takeLastRequest();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        instance = this;
        MockServer.mocksFileParser = mocksFileParser;
    }

    public static RecordedRequest takeLastRequest() throws InterruptedException {
        if (instance != null) return mockWebServer.takeRequest(1, TimeUnit.MICROSECONDS);
        return null;
    }

    public static MatchableCall whenGET(Matcher<RecordedRequest> requestMatcher) {
        return whenRequested(allOf(isGET(), requestMatcher));
    }

    public static MatchableCall whenPOST(Matcher<RecordedRequest> requestMatcher) {
        return whenRequested(allOf(isPOST(), requestMatcher));
    }

    public static MatchableCall whenPUT(Matcher<RecordedRequest> requestMatcher) {
        return whenRequested(allOf(isPUT(), requestMatcher));
    }

    public static MatchableCall whenPATCH(Matcher<RecordedRequest> requestMatcher) {
        return whenRequested(allOf(isPATCH(), requestMatcher));
    }

    public static MatchableCall whenDELETE(Matcher<RecordedRequest> requestMatcher) {
        return whenRequested(allOf(isDELETE(), requestMatcher));
    }

    public static MatchableCall whenRequested(Matcher<RecordedRequest> requestMatcher) {
        return new MatchableCall(MockServer.mocksFileParser, requestMatcher, dispatcher);
    }

    public static void removeAllMatchableCalls() {
        dispatcher.removeAllMatchableCalls();
    }

    public static boolean removeMatchableCall(MatchableCall call) {
        return dispatcher.removeMatchableCall(call);
    }

    public static void replaceMatchableCall(MatchableCall call,
                                            MatchableCall replacement) {
        removeMatchableCall(call);
        dispatcher.addMatchableCall(replacement);
    }

    public static String getUrl() {
        return serverBaseUrl;
    }

    public static void addMatchableCall(final MatchableCall call) {
        dispatcher.addMatchableCall(call);
    }
}
