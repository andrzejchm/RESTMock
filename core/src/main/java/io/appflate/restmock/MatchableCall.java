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

import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.RecordedRequest;

import org.hamcrest.Matcher;

import io.appflate.restmock.utils.RestMockUtils;

/**
 * Represents a Http call with the {@link MockResponse} to be returned for a HTTP request matched by {@link io.appflate.restmock.utils.RequestMatcher RequestMatcher}.
 * In order to create new {@code MatchableCall} call  one of the {@link RESTMockServer} methods.
 */
public class MatchableCall {
    public final Matcher<RecordedRequest> requestMatcher;
    private final RESTMockFileParser RESTMockFileParser;
    public MatchableCallsRequestDispatcher dispatcher;
    public MockResponse response;

    MatchableCall(RESTMockFileParser RESTMockFileParser,
                         Matcher<RecordedRequest> requestMatcher,
                         MatchableCallsRequestDispatcher dispatcher) {
        this.RESTMockFileParser = RESTMockFileParser;
        this.requestMatcher = requestMatcher;
        this.dispatcher = dispatcher;
    }

    /**
     * Same as {@link MatchableCall#thenReturnString(int, String)}, but with the default {@code responseCode} of 200
     *
     * <p>This {@code MatchableCall} will be automatically scheduled within the {@link RESTMockServer} if you want to prevent that, see {@link MatchableCall#dontSet()}</p>
     */
    public MatchableCall thenReturnString(String json) {
        return thenReturnString(200, json);
    }

    /**
     * <p>Makes this {@code MatchableCall} return the given {@code responseString} response with the specified {@code responseCode} as a http status code</p>
     *
     * <p>This {@code MatchableCall} will be automatically scheduled within the {@code RESTMockServer} if you want to prevent that, see {@link MatchableCall#dontSet()}</p>
     *
     * @param responseCode a http response code to use for the response.
     * @param responseString         responseString string to return for this matchableCall's request.
     * @return his {@code MatchableCall}
     */
    public MatchableCall thenReturnString(int responseCode,
                                          String responseString) {
        MockResponse response = new MockResponse();
        if (responseString != null) {
            response.setBody(responseString);
        }
        response.setResponseCode(responseCode);
        return thenReturn(response);
    }

    /**
     * Makes this {@code MatchableCall} return  {@link MockResponse}
     *
     * <p>This {@code MatchableCall} will be automatically scheduled within the {@code RESTMockServer} if you want to prevent that, see {@link MatchableCall#dontSet()}</p>
     *
     * @param resp a {@link MockResponse} that will be returned with this {@code MatchableCall}
     * @return this {@code MatchableCall}
     */
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

    /**
     * same as {@link MatchableCall#thenReturnFile(int, String)} but with the default {@code responseCode} value set to 200.
     *
     * <p>This {@code MatchableCall} will be automatically scheduled within the {@code RESTMockServer} if you want to prevent that, see {@link MatchableCall#dontSet()}</p>
     *
     * @param jsonFile a json file's path to return. {@link RESTMockFileParser} is responsible of reading files for given paths.
     * @return a {@link MatchableCall} thath will return given {@code jsonFile} as a response.
     */
    public MatchableCall thenReturnFile(String jsonFile) {
        return thenReturnFile(200, jsonFile);
    }

    /**
     * Makes this MatchableCall return the {@code jsonFile}'s contents with the {@code responseCode} as a http status code.
     *
     * <p>This {@code MatchableCall} will be automatically scheduled within the {@code RESTMockServer} if you want to prevent that, see {@link MatchableCall#dontSet()}</p>
     * @param responseCode http status code
     * @param jsonFile a json file's path to return. {@link RESTMockFileParser} is responsible of reading files for given paths.
     * @return this {@code MatchableCall}
     */
    public MatchableCall thenReturnFile(int responseCode,
                                        String jsonFile) {
        if (response != null) {
            response = dispatcher.createErrorResponse(
                    new IllegalStateException("response is already set!"));
        } else {
            try {
                response = RestMockUtils.createResponseFromFile(RESTMockFileParser,
                        jsonFile,
                        responseCode);
            } catch (Exception e) {
                response = dispatcher.createErrorResponse(e);
            }
        }
        setResponse();
        return this;
    }

    /**
     * removes this {@code MatchableCall} from being scheduled within {@link RESTMockServer}.
     * @return this {@code MatchableCall}
     */
    public MatchableCall dontSet() {
        dispatcher.removeMatchableCall(this);
        return this;
    }

    private void setResponse() {
        dispatcher.addMatchableCall(this);
    }
}