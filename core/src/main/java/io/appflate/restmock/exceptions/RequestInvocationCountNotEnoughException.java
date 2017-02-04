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

import okhttp3.mockwebserver.RecordedRequest;

/**
 * Exception called when the RequestVerifier verification fails due to
 * count of requests was not as many as expected number.
 */
public class RequestInvocationCountNotEnoughException extends RequestVerificationException {

    public RequestInvocationCountNotEnoughException(Matcher<RecordedRequest> matcher, int count, int times,
                                                    List<RecordedRequest> requestHistory) {
        super(composeMessage("Wanted to be invoked at least %1$s times, but was only %2$s", matcher, count, times, requestHistory));
    }
}
