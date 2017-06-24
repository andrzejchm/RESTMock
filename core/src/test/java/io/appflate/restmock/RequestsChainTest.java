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
import java.util.concurrent.TimeUnit;

import io.appflate.restmock.utils.TestUtils;
import okhttp3.Response;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.RecordedRequest;

import static io.appflate.restmock.utils.RequestMatchers.pathEndsWith;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

/**
 * Created by andrzejchm on 26/04/16.
 */
public class RequestsChainTest {

    private static final String path = "sample";
    private static final Matcher<RecordedRequest> INVOKED_MATCHER = pathEndsWith(path);

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
    public void setUp() {
        RESTMockServer.reset();
    }

    @Test
    public void noResponsesReturnsNotMocked() throws Exception {
        RESTMockServer.whenRequested(pathEndsWith(path)).thenReturn((MockResponse) null);
        TestUtils.assertNotMocked(TestUtils.get(path));
    }

    @Test
    public void multipleResponsesReturnedOneByOne() throws Exception {
        RESTMockServer.whenGET(pathEndsWith(path))
                .thenReturnString("a single call")
                .thenReturnString("answer no 2")
                .thenReturnString("answer no 3");

        String response = TestUtils.get(path).body().string();
        assertEquals("a single call", response);

        response = TestUtils.get(path).body().string();
        assertEquals("answer no 2", response);

        response = TestUtils.get(path).body().string();
        assertEquals("answer no 3", response);
    }

    @Test
    public void multipleResponsesByVarargsReturnedOneByOne() throws Exception {
        RESTMockServer.whenGET(pathEndsWith(path))
                .thenReturnString("a single call", "answer no 2", "answer no 3");

        String response = TestUtils.get(path).body().string();
        assertEquals("a single call", response);

        response = TestUtils.get(path).body().string();
        assertEquals("answer no 2", response);

        response = TestUtils.get(path).body().string();
        assertEquals("answer no 3", response);
    }

    @Test
    public void exceedingNumberOfDefinedResponsesReturnsLastOne() throws Exception {
        String lastResponse = "answer no 3";
        RESTMockServer.whenGET(pathEndsWith(path))
                .thenReturnString("a single call")
                .thenReturnString(lastResponse);

        TestUtils.get(path);

        String response = TestUtils.get(path).body().string();
        assertEquals(lastResponse, response);

        response = TestUtils.get(path).body().string();
        assertEquals(lastResponse, response);
    }

    @Test
    public void multipleResponsesWithDifferentDelays() throws Exception {
        RESTMockServer.whenGET(pathEndsWith(path))
                .thenReturnString("a single call")
                .delay(TimeUnit.SECONDS, 1)
                .thenReturnString("answer no 2")
                .delay(TimeUnit.MILLISECONDS, 100)
                .thenReturnString("answer no 3")
                .delay(TimeUnit.MILLISECONDS, 500);

        long a = System.currentTimeMillis();
        String response = TestUtils.get(path).body().string();
        assertEquals("a single call", response);
        long b = System.currentTimeMillis();
        assertEquals(1000, b - a, 50);

        a = System.currentTimeMillis();
        response = TestUtils.get(path).body().string();
        assertEquals("answer no 2", response);
        b = System.currentTimeMillis();
        assertEquals(100, b - a, 50);

        a = System.currentTimeMillis();
        response = TestUtils.get(path).body().string();
        assertEquals("answer no 3", response);
        b = System.currentTimeMillis();
        assertEquals(500, b - a, 50);
    }

    @Test
    public void multipleResponsesWithOneDelay() throws Exception {
        RESTMockServer.whenGET(pathEndsWith(path))
                .thenReturnString("a single call")
                .delay(TimeUnit.MILLISECONDS, 300)
                .thenReturnString("answer no 2")
                .thenReturnString("answer no 3");

        long a = System.currentTimeMillis();
        String response = TestUtils.get(path).body().string();
        assertEquals("a single call", response);
        long b = System.currentTimeMillis();
        assertEquals(300, b - a, 50);

        a = System.currentTimeMillis();
        response = TestUtils.get(path).body().string();
        assertEquals("answer no 2", response);
        b = System.currentTimeMillis();
        assertEquals(300, b - a, 50);

        a = System.currentTimeMillis();
        response = TestUtils.get(path).body().string();
        assertEquals("answer no 3", response);
        b = System.currentTimeMillis();
        assertEquals(300, b - a, 50);
    }

    @Test
    public void delaysBodyNotResponse() throws Exception {
        RESTMockServer.whenGET(pathEndsWith(path))
                .thenReturnString("a single call")
                .delay(TimeUnit.MILLISECONDS, 500)
                .thenReturnString("answer no 2")
                .thenReturnString("answer no 3");

        long a = System.currentTimeMillis();
        Response response = TestUtils.get(path);
        assertEquals(200, response.code());
        long b = System.currentTimeMillis();
        assertEquals(0, b - a, 100);

        String body = response.body().string();
        assertNotNull(body);
        long c = System.currentTimeMillis();
        assertEquals(500, c - b, 100);
    }

}
