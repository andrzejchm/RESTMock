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

import com.squareup.okhttp.mockwebserver.Dispatcher;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.RecordedRequest;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.LinkedList;
import java.util.List;

class MatchableCallsRequestDispatcher extends Dispatcher {
    private List<MatchableCall> matchableCalls;

    public MatchableCallsRequestDispatcher() {
        matchableCalls = new LinkedList<>();
    }

    @Override
    public MockResponse dispatch(RecordedRequest recordedRequest) throws InterruptedException {
        List<MatchableCall> matchedRequests = getMatchedRequests(recordedRequest);
        if (matchedRequests.size() == 1) {
            return matchedRequests.get(0).response;
        } else if (matchedRequests.size() > 1) {
            return createErrorResponse(
                    new IllegalStateException(prepareTooManyMatchesMessage(matchedRequests)));
        } else {
            MockResponse mockResponse =
                    new MockResponse().setResponseCode(500).setBody("NOT_MOCKED");
            return mockResponse;
        }
    }

    private String prepareTooManyMatchesMessage(final List<MatchableCall> matchedRequests) {
        StringBuilder sb =
                new StringBuilder("there are more than one responses matching this request!\n");
        for (MatchableCall match : matchedRequests) {
            sb.append(match.requestMatcher.toString()).append("\n");
        }
        return sb.toString();
    }

    private List<MatchableCall> getMatchedRequests(RecordedRequest recordedRequest) {
        List<MatchableCall> matched = new LinkedList<>();
        for (MatchableCall request : matchableCalls) {
            if (request.requestMatcher.matches(recordedRequest)) {
                matched.add(request);
            }
        }
        return matched;
    }

    void addMatchableCall(MatchableCall matchableCall) {
        if (!matchableCalls.contains(matchableCall)) {
            matchableCalls.add(matchableCall);
        }
    }

    void removeAllMatchableCalls() {
        matchableCalls.clear();
    }

    MockResponse createErrorResponse(Exception e) {
        MockResponse response = new MockResponse();
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        response.setBody(sw.toString());
        response.setResponseCode(500);
        response.addHeader("Exception", e.getLocalizedMessage());
        return response;
    }

    boolean removeMatchableCall(final MatchableCall call) {
        return matchableCalls.remove(call);
    }
}