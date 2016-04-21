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

import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.RecordedRequest;

import org.hamcrest.Matcher;

import io.appflate.restmock.utils.MocksUtils;


public class MatchableCall {
    public final Matcher<RecordedRequest> requestMatcher;
    private final MocksFileParser mocksFileParser;
    public MatchableCallsRequestDispatcher dispatcher;
    public MockResponse response;

    public MatchableCall(MocksFileParser mocksFileParser,
                         Matcher<RecordedRequest> requestMatcher,
                         MatchableCallsRequestDispatcher dispatcher) {
        this.mocksFileParser = mocksFileParser;
        this.requestMatcher = requestMatcher;
        this.dispatcher = dispatcher;
    }

    public MatchableCall thenReturnJson(String json) {
        return thenReturnJson(200, json);
    }

    public MatchableCall thenReturnJson(int responseCode,
                                        String json) {
        MockResponse response = new MockResponse();
        if (json != null) {
            response.setBody(json);
        }
        response.setResponseCode(responseCode);
        return thenReturn(response);
    }

    public MatchableCall thenReturn(MockResponse resp) {
        if (response != null) {
            response = dispatcher.createErrorResponse(
                    new IllegalStateException("response is already set!"));
        } else {
            this.response = resp;
        }
        setResponse();
        return this;
    }

    public MatchableCall thenReturnFile(String jsonFile) {
        return thenReturnFile(200, jsonFile);
    }

    public MatchableCall thenReturnFile(int responseCode,
                                        String jsonFile) {
        if (response != null) {
            response = dispatcher.createErrorResponse(
                    new IllegalStateException("response is already set!"));
        } else {
            try {
                response = MocksUtils.createResponseFromFile(mocksFileParser,
                        jsonFile,
                        responseCode);
            } catch (Exception e) {
                response = dispatcher.createErrorResponse(e);
            }
        }
        setResponse();
        return this;
    }

    private void setResponse() {
        dispatcher.addMatchableCall(this);
    }

    public MatchableCall dontSet() {
        dispatcher.removeMatchableCall(this);
        return this;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final MatchableCall that = (MatchableCall) o;

        if (!requestMatcher.equals(that.requestMatcher)) return false;
        if (!response.equals(that.response)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = requestMatcher.hashCode();
        result = 31 * result + response.hashCode();
        return result;
    }
}