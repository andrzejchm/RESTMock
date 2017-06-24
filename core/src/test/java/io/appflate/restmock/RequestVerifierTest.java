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
import java.util.List;
import java.util.Locale;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.appflate.restmock.exceptions.RequestInvocationCountMismatchException;
import io.appflate.restmock.exceptions.RequestInvocationCountNotEnoughException;
import io.appflate.restmock.exceptions.RequestNotInvokedException;
import io.appflate.restmock.utils.RequestMatchers;
import io.appflate.restmock.utils.TestUtils;
import okhttp3.mockwebserver.RecordedRequest;

import static io.appflate.restmock.RequestsVerifier.verifyDELETE;
import static io.appflate.restmock.RequestsVerifier.verifyGET;
import static io.appflate.restmock.RequestsVerifier.verifyPOST;
import static io.appflate.restmock.RequestsVerifier.verifyPUT;
import static io.appflate.restmock.RequestsVerifier.verifyRequest;
import static io.appflate.restmock.utils.RequestMatchers.pathEndsWith;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

/**
 * Created by andrzejchm on 26/04/16.
 */
public class RequestVerifierTest {

    private static final String path = "sample";
    private static final Matcher<RecordedRequest> INVOKED_MATCHER = pathEndsWith(path);
    private static final Matcher<RecordedRequest> NOT_INVOKED_MATCHER = pathEndsWith("else");
    static RESTMockFileParser fileParser;

    @BeforeClass
    public static void setupClass() {
        fileParser = mock(RESTMockFileParser.class);
        RESTMockServerStarter.startSync(fileParser);
        RESTMockServer.dispatcher = spy(RESTMockServer.dispatcher);
    }

    @AfterClass
    public static void teardownClass() throws IOException {
        RESTMockServer.shutdown();
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
        verifyGET(INVOKED_MATCHER).exactly(1);
        verifyPOST(INVOKED_MATCHER).exactly(2);
        verifyPUT(INVOKED_MATCHER).exactly(3);
        verifyDELETE(INVOKED_MATCHER).exactly(4);
    }

    @Test
    public void takesLastNumOfElementsWhenHistoryNotEmpty() throws Exception {
        RESTMockServer.whenRequested(pathEndsWith(path)).thenReturnString("a single call");
        TestUtils.get(path);
        TestUtils.post(path);
        TestUtils.delete(path);
        TestUtils.head(path);

        List<RecordedRequest> recordedRequests = RequestsVerifier.takeLast(3);
        assertEquals(3, recordedRequests.size());
        assertEquals("POST", recordedRequests.get(0).getMethod().toUpperCase(Locale.US));
        assertEquals("DELETE", recordedRequests.get(1).getMethod().toUpperCase(Locale.US));
        assertEquals("HEAD", recordedRequests.get(2).getMethod().toUpperCase(Locale.US));
    }

    @Test
    public void takeLastNumOfElementsExceedsHistorySizeTakesWholeHistory() throws Exception {
        RESTMockServer.whenRequested(pathEndsWith(path)).thenReturnString("a single call");
        TestUtils.get(path);
        TestUtils.post(path);
        TestUtils.delete(path);
        TestUtils.head(path);

        List<RecordedRequest> recordedRequests = RequestsVerifier.takeLast(10);
        assertEquals(4, recordedRequests.size());
        assertEquals("GET", recordedRequests.get(0).getMethod().toUpperCase(Locale.US));
        assertEquals("POST", recordedRequests.get(1).getMethod().toUpperCase(Locale.US));
        assertEquals("DELETE", recordedRequests.get(2).getMethod().toUpperCase(Locale.US));
        assertEquals("HEAD", recordedRequests.get(3).getMethod().toUpperCase(Locale.US));
    }

    @Test(expected = IllegalArgumentException.class)
    @SuppressFBWarnings("RV_RETURN_VALUE_IGNORED_NO_SIDE_EFFECT")
    public void takeLastNumOfElementsWithInvalidCountThrowsException() throws Exception {
        RESTMockServer.whenRequested(pathEndsWith(path)).thenReturnString("a single call");
        TestUtils.get(path);
        TestUtils.post(path);
        TestUtils.delete(path);
        TestUtils.head(path);

        RequestsVerifier.takeLast(-10);
    }

    @Test
    public void takesFirstNumOfElementsWhenHistoryNotEmpty() throws Exception {
        RESTMockServer.whenRequested(pathEndsWith(path)).thenReturnString("a single call");
        TestUtils.get(path);
        TestUtils.post(path);
        TestUtils.delete(path);
        TestUtils.head(path);

        List<RecordedRequest> recordedRequests = RequestsVerifier.takeFirst(3);
        assertEquals(3, recordedRequests.size());
        assertEquals("GET", recordedRequests.get(0).getMethod().toUpperCase(Locale.US));
        assertEquals("POST", recordedRequests.get(1).getMethod().toUpperCase(Locale.US));
        assertEquals("DELETE", recordedRequests.get(2).getMethod().toUpperCase(Locale.US));
    }

    @Test
    public void takeFirstNumOfElementsExceedsHistorySizeTakesAllHistory() throws Exception {
        RESTMockServer.whenRequested(pathEndsWith(path)).thenReturnString("a single call");
        TestUtils.get(path);
        TestUtils.post(path);
        TestUtils.delete(path);
        TestUtils.head(path);

        List<RecordedRequest> recordedRequests = RequestsVerifier.takeFirst(10);
        assertEquals(4, recordedRequests.size());
        assertEquals("GET", recordedRequests.get(0).getMethod().toUpperCase(Locale.US));
        assertEquals("POST", recordedRequests.get(1).getMethod().toUpperCase(Locale.US));
        assertEquals("DELETE", recordedRequests.get(2).getMethod().toUpperCase(Locale.US));
        assertEquals("HEAD", recordedRequests.get(3).getMethod().toUpperCase(Locale.US));
    }

    @Test
    public void takesSubsetOfRequests() throws Exception {
        RESTMockServer.whenRequested(pathEndsWith(path)).thenReturnString("a single call");
        TestUtils.get(path);
        TestUtils.post(path);
        TestUtils.delete(path);
        TestUtils.head(path);

        List<RecordedRequest> recordedRequests = RequestsVerifier.take(1, 4);
        assertEquals(3, recordedRequests.size());
        assertEquals("POST", recordedRequests.get(0).getMethod().toUpperCase(Locale.US));
        assertEquals("DELETE", recordedRequests.get(1).getMethod().toUpperCase(Locale.US));
        assertEquals("HEAD", recordedRequests.get(2).getMethod().toUpperCase(Locale.US));
    }

    @Test(expected = IllegalArgumentException.class)
    @SuppressFBWarnings("RV_RETURN_VALUE_IGNORED_NO_SIDE_EFFECT")
    public void takesSubsetOfRequestsWithInvalidRangeThrowsError() throws Exception {
        RESTMockServer.whenRequested(pathEndsWith(path)).thenReturnString("a single call");
        TestUtils.get(path);
        TestUtils.post(path);
        TestUtils.delete(path);
        TestUtils.head(path);

        RequestsVerifier.take(5, 3);
    }

    @Test
    public void takeMatchingFindsAllRelevantRequests() throws Exception {
        RESTMockServer.whenRequested(pathEndsWith(path)).thenReturnString("a single call");
        TestUtils.get(path);
        TestUtils.post(path);
        TestUtils.delete(path);
        TestUtils.get(path);
        TestUtils.head(path);
        TestUtils.get(path);
        List<RecordedRequest> recordedRequests = RequestsVerifier.takeAllMatching(RequestMatchers.isGET());
        assertEquals(3, recordedRequests.size());
        assertEquals("GET", recordedRequests.get(0).getMethod().toUpperCase(Locale.US));
        assertEquals("GET", recordedRequests.get(1).getMethod().toUpperCase(Locale.US));
        assertEquals("GET", recordedRequests.get(2).getMethod().toUpperCase(Locale.US));
    }

    @Test(expected = IllegalArgumentException.class)
    @SuppressFBWarnings("RV_RETURN_VALUE_IGNORED_NO_SIDE_EFFECT")
    public void takeFirstNumOfElementsWithInvalidCountThrowsException() throws Exception {
        RESTMockServer.whenRequested(pathEndsWith(path)).thenReturnString("a single call");
        TestUtils.get(path);
        TestUtils.post(path);
        TestUtils.delete(path);
        TestUtils.head(path);

        RequestsVerifier.takeFirst(-10);
    }

    @Test
    public void takeSingleFirstWhenNoHistoryIsNull() throws Exception {
        RESTMockServer.whenRequested(pathEndsWith(path)).thenReturnString("a single call");
        assertNull(RequestsVerifier.takeFirst());
    }

    @Test
    public void takeSingleLastWhenNoHistoryIsNull() throws Exception {
        RESTMockServer.whenRequested(pathEndsWith(path)).thenReturnString("a single call");
        assertNull(RequestsVerifier.takeLast());
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
        TestUtils.get(path + "something");
        TestUtils.get(path + "else");
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
}
