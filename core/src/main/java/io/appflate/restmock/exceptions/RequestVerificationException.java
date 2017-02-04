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

package io.appflate.restmock.exceptions;

import org.hamcrest.Matcher;

import java.util.List;
import java.util.Locale;

import okhttp3.mockwebserver.RecordedRequest;

/**
 * Created by andrzejchm on 27/04/16.
 */
public class RequestVerificationException extends RuntimeException {

    protected static String composeMessage(String messageFormat, Matcher<RecordedRequest> requestMatcher, int count, int times,
                                           List<RecordedRequest> requestHistory) {
        StringBuilder sb = new StringBuilder();
        sb.append("Request = \"").append(requestMatcher).append("\":\n");
        sb.append(String.format(Locale.US, messageFormat, times, count));
        appendHistoryRequests(requestMatcher, requestHistory, sb);
        return sb.toString();
    }

    protected static void appendHistoryRequests(Matcher<RecordedRequest> matcher, List<RecordedRequest> requestHistory, StringBuilder sb) {
        sb.append("\n\nAll invocations: (\"#\" at the beginning means the request was matched)\n[");
        if (!requestHistory.isEmpty()) {
            sb.append("\n");
        }
        for (RecordedRequest recordedRequest : requestHistory) {
            sb.append("\t");
            if (matcher.matches(recordedRequest)) {
                sb.append("# ");
            }
            sb.append(recordedRequest.getRequestLine());
            if (matcher.matches(recordedRequest)) {
                sb.append(" \t| #MATCH");
            }
            sb.append("\n");
        }
        sb.append("]");
    }

    public RequestVerificationException(String message) {
        super(message);
    }
}
