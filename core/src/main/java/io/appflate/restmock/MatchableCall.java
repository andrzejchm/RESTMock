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
import java.util.concurrent.TimeUnit;

import io.appflate.restmock.utils.RequestMatcher;
import io.appflate.restmock.utils.RestMockUtils;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.RecordedRequest;

/**
 * Represents a Http call with the {@link MockAnswer} to be returned for a HTTP request matched by {@link RequestMatcher}.
 * In order to create new {@code MatchableCall} call  one of the {@link RESTMockServer} methods.
 */
public class MatchableCall {

    final Matcher<RecordedRequest> requestMatcher;
    private final RESTMockFileParser RESTMockFileParser;
    private MatchableCallsRequestDispatcher dispatcher;
    private List<MockAnswer> answers;
    private List<Long> bodyDelays;
    private List<Long> headerDelays;
    private int responseIndex;

    MatchableCall(RESTMockFileParser RESTMockFileParser, Matcher<RecordedRequest> requestMatcher,
                  MatchableCallsRequestDispatcher dispatcher) {
        this.RESTMockFileParser = RESTMockFileParser;
        this.requestMatcher = requestMatcher;
        this.dispatcher = dispatcher;
        this.answers = new LinkedList<>();
        this.bodyDelays = new LinkedList<>();
        this.headerDelays = new LinkedList<>();
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
     * @param responseCode    a http response code to use for the response.
     * @param responseStrings strings to return for this matchableCall's request one for each consecutive request, last string
     *                        will be returned for all requests exceeding number of defined responses.
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
     * same as {@link MatchableCall#thenReturnFile(int, String...)} but with the default {@code responseCode} value set to 200.
     *
     * <p>This {@code MatchableCall} will be automatically scheduled within the {@code RESTMockServer} if you want to prevent that, see
     * {@link MatchableCall#dontSet()}</p>
     *
     * <p>If you specify more than one response, each consecutive call to server will return next response from the list, if number of
     * requests exceeds number of specified responses, the last response will be repeated</p>
     *
     * @param jsonFiles a comma-separated list of files' path to return. {@link RESTMockFileParser} is responsible of reading files for
     *                  given paths.
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
     * @param jsonFiles    a comma-separated list of json files' paths that will be returned. {@link RESTMockFileParser} is responsible of
     *                     reading files for given paths.
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
     * @deprecated this method is deprecated, use {@link #delayBody(TimeUnit, long...)} instead.
     */
    @Deprecated
    public MatchableCall delay(TimeUnit timeUnit, long... delays) {
        return delayBody(timeUnit, delays);
    }

    /**
     * Sets delays for responses' body within this {@link MatchableCall}. You can specify more than one delay, which will result in responses
     * being delayed
     * in order of specified delays, one by one, while last delay will be applied to all responses exceeding the number of specified
     * delays.
     * <p>
     * Note: This DOES NOT affect response headers. For that, use {@link #delayHeaders(TimeUnit, long...)} instead.
     *
     * @param timeUnit time unit to use for the delay, (see {@link TimeUnit#SECONDS}, {@link TimeUnit#MILLISECONDS})
     * @param delays   comma-separated list of delays to apply to consecutive responses
     */
    public MatchableCall delayBody(TimeUnit timeUnit, long... delays) {
        for (long delay : delays) {
            this.bodyDelays.add(timeUnit.toMillis(delay));
        }
        return this;
    }

    /**
     * Sets delays for responses' headers within this {@link MatchableCall}. You can specify more than one delay, which will result in responses
     * being delayed
     * in order of specified delays, one by one, while last delay will be applied to all responses exceeding the number of specified
     * delays.
     * <p>
     * Note: This DOES NOT affect response body. For that, use {@link #delayBody(TimeUnit, long...)} instead.
     *
     * @param timeUnit time unit to use for the delay, (see {@link TimeUnit#SECONDS}, {@link TimeUnit#MILLISECONDS})
     * @param delays   comma-separated list of delays to apply to consecutive responses
     */
    public MatchableCall delayHeaders(TimeUnit timeUnit, long... delays) {
        for (long delay : delays) {
            this.headerDelays.add(timeUnit.toMillis(delay));
        }
        return this;
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
            MockAnswer[] mockAnswers = new MockAnswer[mockResponses.length];
            int i = 0;
            for (MockResponse response : mockResponses) {
                if (response != null) {
                    mockAnswers[i++] = new SimpleMockResponseAnswer(response);
                }
            }
            thenAnswer(mockAnswers);
        }
        return this;
    }

    /**
     * Makes this {@code MatchableCall} return  {@link MockAnswer}
     *
     * <p>This {@code MatchableCall} will be automatically scheduled within the {@code RESTMockServer} if you want to prevent that, see
     * {@link MatchableCall#dontSet()}</p>
     *
     * <p>If you specify more than one answer, each consecutive call to server will call next answer from the list, if number of
     * requests exceeds number of specified answers, the last answer will be repeated</p>
     *
     * @param mockAnswers a {@link MockAnswer} that will be triggered with a matched {@link RecordedRequest}
     * @return this {@code MatchableCall}
     */
    public MatchableCall thenAnswer(MockAnswer... mockAnswers) {
        if (mockAnswers != null) {
            for (MockAnswer answer : mockAnswers) {
                if (answer != null) {
                    this.answers.add(answer);
                }
            }
        }
        addToDispatcher();
        return this;
    }

    MockResponse nextResponse(RecordedRequest request) {
        MockResponse mockResponse;
        if (answers.isEmpty()) {
            mockResponse = null;
        } else {
            mockResponse = currentResponse(request);
            setResponseDelayInternal(mockResponse);
        }
        responseIndex++;
        return mockResponse;
    }

    private MockResponse currentResponse(RecordedRequest request) {
        MockAnswer mockAnswer;
        if (responseIndex >= answers.size()) {
            mockAnswer = answers.get(answers.size() - 1);
        } else {
            mockAnswer = answers.get(responseIndex);
        }
        return mockAnswer.answer(request);
    }

    private void setResponseDelayInternal(MockResponse response) {
        long bodyDelay;
        long headerDelay;
        if (bodyDelays.isEmpty()) {
            bodyDelay = 0;
        } else if (responseIndex >= bodyDelays.size()) {
            bodyDelay = bodyDelays.get(bodyDelays.size() - 1);
        } else {
            bodyDelay = bodyDelays.get(responseIndex);
        }
        if (headerDelays.isEmpty()) {
            headerDelay = 0;
        } else if (responseIndex >= headerDelays.size()) {
            headerDelay = headerDelays.get(headerDelays.size() - 1);
        } else {
            headerDelay = headerDelays.get(responseIndex);
        }
        if (bodyDelay != 0) {
            response.setBodyDelay(bodyDelay, TimeUnit.MILLISECONDS);
        }
        if (headerDelay != 0) {
            response.setHeadersDelay(headerDelay, TimeUnit.MILLISECONDS);
        }
    }

    int getNumberOfAnswers() {
        return answers.size();
    }

    private void addToDispatcher() {
        dispatcher.addMatchableCall(this);
    }

    private static class SimpleMockResponseAnswer implements MockAnswer {

        private final MockResponse response;

        public SimpleMockResponseAnswer(MockResponse response) {
            this.response = response;
        }

        @Override
        public MockResponse answer(RecordedRequest request) {
            return response;
        }
    }
}
