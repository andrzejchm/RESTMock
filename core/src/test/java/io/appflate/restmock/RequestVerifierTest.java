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
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;

import io.appflate.restmock.exceptions.RequestInvocationCountMismatchException;
import io.appflate.restmock.exceptions.RequestInvocationCountNotEnoughException;
import io.appflate.restmock.exceptions.RequestNotInvokedException;
import io.appflate.restmock.utils.TestUtils;
import okhttp3.mockwebserver.RecordedRequest;

import static io.appflate.restmock.RequestsVerifier.verifyDELETERequest;
import static io.appflate.restmock.RequestsVerifier.verifyGETRequest;
import static io.appflate.restmock.RequestsVerifier.verifyPOSTRequest;
import static io.appflate.restmock.RequestsVerifier.verifyPUTRequest;
import static io.appflate.restmock.RequestsVerifier.verifyRequest;
import static io.appflate.restmock.utils.RequestMatchers.pathEndsWith;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

/**
 * Created by andrzejchm on 26/04/16.
 */
public class RequestVerifierTest {

    static RESTMockFileParser fileParser;
    private static final String path = "sample";
    private static final Matcher<RecordedRequest> INVOKED_MATCHER = pathEndsWith(path);
    private static final Matcher<RecordedRequest> NOT_INVOKED_MATCHER = pathEndsWith("else");

    @BeforeClass
    public static void setupClass() {
        fileParser = mock(RESTMockFileParser.class);
        RESTMockServerStarter.startSync(fileParser);
        RESTMockServer.dispatcher = spy(RESTMockServer.dispatcher);
    }

    @Before
    public void setup() {
        RESTMockServer.reset();
    }

    @Test
    public void testValidAssertions() throws Exception {
        RESTMockServer.whenRequested(pathEndsWith(path)).thenReturnString("a single call");
        verifyRequest(INVOKED_MATCHER).never();
        verifyRequest(INVOKED_MATCHER).exactly(0);
        TestUtils.get(path);
        verifyRequest(INVOKED_MATCHER).invoked();
        verifyRequest(INVOKED_MATCHER).exactly(1);
        TestUtils.get(path);
        verifyRequest(INVOKED_MATCHER).exactly(2);
        TestUtils.get(path);
        verifyRequest(INVOKED_MATCHER).exactly(3);
        verifyRequest(INVOKED_MATCHER).atLeast(1);
        verifyRequest(INVOKED_MATCHER).atLeast(2);
        verifyRequest(INVOKED_MATCHER).atLeast(3);
    }

    @Test
    public void testHTTPMethodVerifier() throws Exception {
        RESTMockServer.whenRequested(pathEndsWith(path)).thenReturnString("a single call");
        verifyRequest(INVOKED_MATCHER).never();
        verifyRequest(INVOKED_MATCHER).exactly(0);
        TestUtils.get(path);
        TestUtils.post(path);
        TestUtils.post(path);
        TestUtils.put(path);
        TestUtils.put(path);
        TestUtils.put(path);
        TestUtils.delete(path);
        TestUtils.delete(path);
        TestUtils.delete(path);
        TestUtils.delete(path);
        verifyRequest(INVOKED_MATCHER).exactly(10);
        verifyGETRequest(INVOKED_MATCHER).exactly(1);
        verifyPOSTRequest(INVOKED_MATCHER).exactly(2);
        verifyPUTRequest(INVOKED_MATCHER).exactly(3);
        verifyDELETERequest(INVOKED_MATCHER).exactly(4);
    }

    @Test
    public void testReset() throws Exception {
        RESTMockServer.whenRequested(pathEndsWith(path)).thenReturnString("a single call");
        TestUtils.get(path);
        verifyRequest(INVOKED_MATCHER).exactly(1);
        verifyRequest(INVOKED_MATCHER).invoked();
        TestUtils.get(path);
        TestUtils.get(path);
        TestUtils.get(path);
        verifyRequest(INVOKED_MATCHER).exactly(4);
        RESTMockServer.reset();
        verifyRequest(INVOKED_MATCHER).never();
        TestUtils.get(path);
        verifyRequest(INVOKED_MATCHER).invoked();
    }

    @Test(expected = RequestNotInvokedException.class)
    public void testInvoked_NotInvokedException() throws Exception {
        verifyRequest(NOT_INVOKED_MATCHER).invoked();
    }

    @Test(expected = RequestInvocationCountMismatchException.class)
    public void testInvoked_InvocationCountMismatchException() throws Exception {
        RESTMockServer.whenRequested(INVOKED_MATCHER).thenReturnString("a single call");
        TestUtils.get(path);
        verifyRequest(INVOKED_MATCHER).exactly(3);
    }

    @Test(expected = RequestInvocationCountNotEnoughException.class)
    public void testAtLeast_InvocationCountMismatchException() throws Exception {
        RESTMockServer.whenRequested(INVOKED_MATCHER).thenReturnString("a single call");
        RESTMockServer.whenRequested(INVOKED_MATCHER).thenReturnString("a single call");
        TestUtils.get(path);
        TestUtils.get(path+"something");
        TestUtils.get(path+"else");
        TestUtils.get(path);
        verifyRequest(INVOKED_MATCHER).atLeast(3);
    }

    @Test(expected = RequestNotInvokedException.class)
    public void testExactly_NotInvokedException() throws Exception {
        verifyRequest(NOT_INVOKED_MATCHER).exactly(3);
    }

    @Test(expected = RequestInvocationCountMismatchException.class)
    public void testExactly_MismatchException() throws Exception {
        RESTMockServer.whenRequested(INVOKED_MATCHER).thenReturnString("a single call");
        TestUtils.get(path);
        verifyRequest(INVOKED_MATCHER).exactly(3);
    }

    @AfterClass
    public static void teardownClass() throws IOException {
        RESTMockServer.shutdown();
    }
}
