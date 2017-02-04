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
import org.hamcrest.core.AllOf;

import io.appflate.restmock.exceptions.RequestInvocationCountMismatchException;
import io.appflate.restmock.exceptions.RequestInvocationCountNotEnoughException;
import io.appflate.restmock.exceptions.RequestNotInvokedException;
import io.appflate.restmock.utils.RequestMatchers;
import okhttp3.mockwebserver.RecordedRequest;

/**
 * Created by andrzejchm on 26/04/16.
 */
public class RequestsVerifier {

    public static class RequestVerification {

        Matcher<RecordedRequest> matcher;

        /**
         * @param requestMatcher needed to match the interesting request.
         * @return how many times the request was invoked
         */
        private static int requestInvocationCount(Matcher<RecordedRequest> requestMatcher) {
            int count = 0;
            for (RecordedRequest recordedRequest : dispatcher.getRequestHistory()) {
                if (requestMatcher.matches(recordedRequest)) {
                    count++;
                }
            }
            return count;
        }

        RequestVerification(Matcher<RecordedRequest> matcher) {
            this.matcher = matcher;
        }

        /**
         * Checks whether the request matched by {@code requestMatcher} was never called.
         * Will throw an exception if it was called.
         */
        public void never() {
            exactly(0);
        }

        /**
         * {@link #exactly(int)} with the default {@code times} value of 1.
         */
        public void invoked() {
            exactly(1);
        }

        /**
         * Checks whether the request matched by {@code requestMatcher} was called at least {@code times} times.
         *
         * @param times At least how many times the request should be invoked.
         */
        public void atLeast(int times) {
            if (times < 1) {
                throw new IllegalArgumentException("number of times should be greater than 1! is: " + times);
            }
            int count = requestInvocationCount(matcher);
            if (count < times) {
                throw new RequestInvocationCountNotEnoughException(matcher, count, times, dispatcher.getRequestHistory());
            }

        }

        /**
         * Checks whether the request matched by {@code requestMatcher} was called exactly {@code times} times.
         *
         * @param times how many times the request was supposed to be invoked.
         */
        public void exactly(int times) {
            checkValidNumberOfTimes(times);
            int count = requestInvocationCount(matcher);
            if (count != times) {
                if (count == 0) {
                    throw new RequestNotInvokedException(matcher, dispatcher.getRequestHistory());
                } else {
                    throw new RequestInvocationCountMismatchException(count, times, matcher, dispatcher.getRequestHistory());
                }
            }
        }

        private void checkValidNumberOfTimes(int times) {
            if (times < 0) {
                throw new IllegalArgumentException("number of times should be greater than 0! is: " + times);
            }
        }

    }

    private static MatchableCallsRequestDispatcher dispatcher;

    static void init(MatchableCallsRequestDispatcher dispatcher) {
        RequestsVerifier.dispatcher = dispatcher;
    }

    public static RequestVerification verifyRequest(Matcher<RecordedRequest> matcher) {
        return new RequestVerification(matcher);
    }

    public static RequestVerification verifyDELETE(Matcher<RecordedRequest> matcher) {
        return verifyRequest(AllOf.allOf(RequestMatchers.isDELETE(), matcher));
    }

    public static RequestVerification verifyGET(Matcher<RecordedRequest> matcher) {
        return verifyRequest(AllOf.allOf(RequestMatchers.isGET(), matcher));
    }

    public static RequestVerification verifyPATCH(Matcher<RecordedRequest> matcher) {
        return verifyRequest(AllOf.allOf(RequestMatchers.isPATCH(), matcher));
    }

    public static RequestVerification verifyPOST(Matcher<RecordedRequest> matcher) {
        return verifyRequest(AllOf.allOf(RequestMatchers.isPOST(), matcher));
    }

    public static RequestVerification verifyPUT(Matcher<RecordedRequest> matcher) {
        return verifyRequest(AllOf.allOf(RequestMatchers.isPUT(), matcher));
    }

    private RequestsVerifier() {
        throw new UnsupportedOperationException();
    }
}
