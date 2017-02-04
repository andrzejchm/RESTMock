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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import io.appflate.restmock.utils.RestMockUtils;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.RecordedRequest;

/**
 * Represents a Http call with the {@link MockResponse} to be returned for a HTTP request matched by {@link
 * io.appflate.restmock.utils.RequestMatcher RequestMatcher}.
 * In order to create new {@code MatchableCall} call  one of the {@link RESTMockServer} methods.
 */
public class MatchableCall {

    final Matcher<RecordedRequest> requestMatcher;
    private final RESTMockFileParser RESTMockFileParser;
    private MatchableCallsRequestDispatcher dispatcher;
    private List<MockResponse> responses;
    private int responseIndex;

    MatchableCall(RESTMockFileParser RESTMockFileParser, Matcher<RecordedRequest> requestMatcher,
                  MatchableCallsRequestDispatcher dispatcher) {
        this.RESTMockFileParser = RESTMockFileParser;
        this.requestMatcher = requestMatcher;
        this.dispatcher = dispatcher;
        this.responses = new LinkedList<>();
        this.responseIndex = 0;
    }

    /**
     * Same as {@link MatchableCall#thenReturnString(int, String...)}, but with the default {@code responseCode} of 200
     *
     * <p>This {@code MatchableCall} will be automatically scheduled within the {@link RESTMockServer} if you want to prevent that, see
     * {@link MatchableCall#dontSet()}</p>
     *
     * <p>If you specify more than one response, each consecutive call to server will return next response from the list, if number of
     * requests exceeds number of specified responses, the last response will be repeated</p>
     *
     * @param strings body contents to be returned with the response one after another
     * @return this {@code MatchableCall}
     */
    public MatchableCall thenReturnString(String... strings) {
        return thenReturnString(200, strings);
    }

    /**
     * <p>Makes this {@code MatchableCall} return  given {@code responseStrings} responses (one by one) with the specified {@code
     * responseCode} as a
     * http status code</p>
     *
     * <p>This {@code MatchableCall} will be automatically scheduled within the {@code RESTMockServer} if you want to prevent that, see
     * {@link MatchableCall#dontSet()}</p>
     *
     * <p>If you specify more than one response, each consecutive call to server will return next response from the list, if number of
     * requests exceeds number of specified responses, the last response will be repeated</p>
     *
     * @param responseCode a http response code to use for the response.
     * @param responseStrings strings to return for this matchableCall's request one for each consecutive request, last string
     * will be returned for all requests exceeding number of defined responses.
     * @return this {@code MatchableCall}
     */
    public MatchableCall thenReturnString(int responseCode, String... responseStrings) {
        MockResponse[] mockResponses = new MockResponse[responseStrings.length];
        int i = 0;
        for (String responseString : responseStrings) {
            MockResponse response = new MockResponse();
            response.setBody(responseString);
            response.setResponseCode(responseCode);
            mockResponses[i++] = response;
        }
        return thenReturn(mockResponses);
    }

    /**
     * same as {@link MatchableCall#thenReturnString(int, String...)}, but with {@code ""} set as response}
     */
    public MatchableCall thenReturnEmpty(int responseCode) {
        return thenReturnString(responseCode, "");
    }

    /**
     * Makes this {@code MatchableCall} return  {@link MockResponse}
     *
     * <p>This {@code MatchableCall} will be automatically scheduled within the {@code RESTMockServer} if you want to prevent that, see
     * {@link MatchableCall#dontSet()}</p>
     *
     * <p>If you specify more than one response, each consecutive call to server will return next response from the list, if number of
     * requests exceeds number of specified responses, the last response will be repeated</p>
     *
     * @param mockResponses a {@link MockResponse} that will be returned with this {@code MatchableCall}
     * @return this {@code MatchableCall}
     */
    public MatchableCall thenReturn(MockResponse... mockResponses) {
        if (mockResponses != null) {
            for (MockResponse response : mockResponses) {
                if (response != null) {
                    this.responses.add(response);
                }
            }
        }
        addToDispatcher();
        return this;
    }

    /**
     * same as {@link MatchableCall#thenReturnFile(int, String...)} but with the default {@code responseCode} value set to 200.
     *
     * <p>This {@code MatchableCall} will be automatically scheduled within the {@code RESTMockServer} if you want to prevent that, see
     * {@link MatchableCall#dontSet()}</p>
     *
     * <p>If you specify more than one response, each consecutive call to server will return next response from the list, if number of
     * requests exceeds number of specified responses, the last response will be repeated</p>
     *
     * @param jsonFiles a comma-separated list of files' path to return. {@link RESTMockFileParser} is responsible of reading files for
     * given paths.
     * @return a {@link MatchableCall} that will return given {@code jsonFile} as a response.
     */
    public MatchableCall thenReturnFile(String... jsonFiles) {
        return thenReturnFile(200, jsonFiles);
    }

    /**
     * Makes this MatchableCall return the {@code jsonFile}'s contents with the {@code responseCode} as a http status code.
     *
     * <p>This {@code MatchableCall} will be automatically scheduled within the {@code RESTMockServer} if you want to prevent that, see
     * {@link MatchableCall#dontSet()}</p>
     *
     * <p>If you specify more than one response, each consecutive call to server will return next response from the list, if number of
     * requests exceeds number of specified responses, the last response will be repeated</p>
     *
     * @param responseCode http status code
     * @param jsonFiles a comma-separated list of json files' paths that will be returned. {@link RESTMockFileParser} is responsible of
     * reading files for given paths.
     * @return this {@code MatchableCall}
     */
    public MatchableCall thenReturnFile(int responseCode, String... jsonFiles) {
        List<MockResponse> responseFromFiles = new ArrayList<>(jsonFiles.length);
        for (String jsonFile : jsonFiles) {
            try {
                responseFromFiles.add(RestMockUtils.createResponseFromFile(RESTMockFileParser, jsonFile, responseCode));
            } catch (Exception e) {
                RESTMockServer.getLogger().error("<- Response FILE READ ERROR", e);
                responseFromFiles.add(dispatcher.createErrorResponse(e));
            }
        }
        return thenReturn(responseFromFiles.toArray(new MockResponse[responseFromFiles.size()]));
    }

    /**
     * removes this {@code MatchableCall} from being scheduled within {@link RESTMockServer}.
     *
     * @return this {@code MatchableCall}
     */
    public MatchableCall dontSet() {
        dispatcher.removeMatchableCall(this);
        return this;
    }

    MockResponse nextResponse() {
        MockResponse mockResponse = peekNextResponse();
        responseIndex++;
        return mockResponse;
    }

    MockResponse peekNextResponse() {
        if (responses.size() == 0) {
            return null;
        } else if (responseIndex >= responses.size()) {
            return responses.get(responses.size() - 1);
        } else {
            return responses.get(responseIndex);
        }
    }

    List<MockResponse> getResponses() {
        return responses;
    }

    private void addToDispatcher() {
        dispatcher.addMatchableCall(this);
    }
}
