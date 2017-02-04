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

package io.appflate.restmock.utils;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import okhttp3.mockwebserver.RecordedRequest;

/**
 * <p>A RequestMatcher is an extension of {@link org.hamcrest.TypeSafeMatcher} making it easier to specify
 * the matcher without the need of implementing {@link #describeTo(Description)} method.</p>
 * <p>See {@link org.hamcrest.Matcher} for more info about hamcrest's matchers</p>
 */
public abstract class RequestMatcher extends TypeSafeMatcher<RecordedRequest> {

    private final String description;

    public RequestMatcher(String description) {
        this.description = description;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText(this.description);
    }
}
