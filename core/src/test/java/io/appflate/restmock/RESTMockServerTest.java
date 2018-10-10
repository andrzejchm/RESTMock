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

import io.appflate.restmock.utils.TestUtils;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Collection;
import okhttp3.Response;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static io.appflate.restmock.utils.RequestMatchers.pathEndsWith;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

@RunWith(Parameterized.class)
public class RESTMockServerTest {

    static RESTMockFileParser fileParser;
    private final boolean useHttps;


    @Parameterized.Parameters(name = "useHttps={0}")
    public static Collection<Object> data() {
        return Arrays.asList(new Object[] {
            true, false
        });
    }

    public RESTMockServerTest(boolean useHttps) {
        this.useHttps = useHttps;
    }

    @Before
    public void setup() {
        fileParser = mock(RESTMockFileParser.class);
        RESTMockServerStarter.startSync(fileParser, new RESTMockOptions.Builder().useHttps(useHttps).build());
        RESTMockServer.dispatcher = spy(RESTMockServer.dispatcher);
    }

    @After
    public void teardown() throws IOException {
        RESTMockServer.shutdown();
    }

    @Test
    public void testWhenGET() throws Exception {
        String path = "/sample";
        String worksBody = "works";
        MatchableCall matchableCall = RESTMockServer.whenGET(pathEndsWith(path));
        assertNotNull(matchableCall);
        assertEquals(0, matchableCall.getNumberOfAnswers());
        verify(RESTMockServer.dispatcher, never()).addMatchableCall(matchableCall);
        TestUtils.assertNotMocked(TestUtils.get(path));
        matchableCall.thenReturnString(worksBody);
        TestUtils.assertResponseWithBodyContains(TestUtils.get(path), 200, "works");
        TestUtils.assertNotMocked(TestUtils.post(path));
        TestUtils.assertNotMocked(TestUtils.delete(path));
        TestUtils.assertNotMocked(TestUtils.put(path));
        TestUtils.assertNotMockedNoBody(TestUtils.head(path));
    }

    @Test
    public void testWhenPOST() throws Exception {
        String path = "/sample";
        String worksBody = "works";
        MatchableCall matchableCall = RESTMockServer.whenPOST(pathEndsWith(path));
        assertNotNull(matchableCall);
        assertEquals(0, matchableCall.getNumberOfAnswers());
        verify(RESTMockServer.dispatcher, never()).addMatchableCall(matchableCall);
        TestUtils.assertNotMocked(TestUtils.get(path));
        matchableCall.thenReturnString(worksBody);
        TestUtils.assertNotMocked(TestUtils.get(path));
        TestUtils.assertResponseWithBodyContains(TestUtils.post(path), 200, "works");
        TestUtils.assertNotMocked(TestUtils.delete(path));
        TestUtils.assertNotMocked(TestUtils.put(path));
        TestUtils.assertNotMockedNoBody(TestUtils.head(path));
    }

    @Test
    public void testWhenPUT() throws Exception {
        String path = "/sample";
        String worksBody = "works";
        MatchableCall matchableCall = RESTMockServer.whenPUT(pathEndsWith(path));
        assertNotNull(matchableCall);
        assertEquals(0, matchableCall.getNumberOfAnswers());
        verify(RESTMockServer.dispatcher, never()).addMatchableCall(matchableCall);
        TestUtils.assertNotMocked(TestUtils.get(path));
        matchableCall.thenReturnString(worksBody);
        TestUtils.assertNotMocked(TestUtils.get(path));
        TestUtils.assertNotMocked(TestUtils.post(path));
        TestUtils.assertNotMocked(TestUtils.delete(path));
        TestUtils.assertResponseWithBodyContains(TestUtils.put(path), 200, "works");
        TestUtils.assertNotMockedNoBody(TestUtils.head(path));
    }

    @Test
    public void testWhenDELETE() throws Exception {
        String path = "/sample";
        String worksBody = "works";
        MatchableCall matchableCall = RESTMockServer.whenDELETE(pathEndsWith(path));
        assertNotNull(matchableCall);
        assertEquals(0, matchableCall.getNumberOfAnswers());
        verify(RESTMockServer.dispatcher, never()).addMatchableCall(matchableCall);
        TestUtils.assertNotMocked(TestUtils.get(path));
        matchableCall.thenReturnString(worksBody);
        TestUtils.assertNotMocked(TestUtils.get(path));
        TestUtils.assertNotMocked(TestUtils.post(path));
        TestUtils.assertResponseWithBodyContains(TestUtils.delete(path), 200, worksBody);
        TestUtils.assertNotMocked(TestUtils.put(path));
        TestUtils.assertNotMockedNoBody(TestUtils.head(path));
    }

    @Test
    public void testWhenHEAD() throws Exception {
        String path = "/sample";
        MatchableCall matchableCall = RESTMockServer.whenHEAD(pathEndsWith(path));
        assertNotNull(matchableCall);
        assertEquals(0, matchableCall.getNumberOfAnswers());
        verify(RESTMockServer.dispatcher, never()).addMatchableCall(matchableCall);
        TestUtils.assertNotMocked(TestUtils.get(path));
        matchableCall.thenReturnEmpty(200);
        TestUtils.assertNotMocked(TestUtils.get(path));
        TestUtils.assertNotMocked(TestUtils.post(path));
        TestUtils.assertResponseCodeIs(TestUtils.head(path), 200);
        TestUtils.assertNotMocked(TestUtils.put(path));
        TestUtils.assertNotMocked(TestUtils.delete(path));
    }

    @Test
    public void testMultipleMatches() throws Exception {
        String path = "sample";
        RESTMockServer.whenRequested(pathEndsWith(path)).thenReturnString("multiple calls");
        RESTMockServer.whenRequested(pathEndsWith(path)).thenReturnString("multiple calls another");
        TestUtils.assertMultipleMatches(TestUtils.get(path));
        TestUtils.assertMultipleMatches(TestUtils.post(path));
        TestUtils.assertMultipleMatches(TestUtils.delete(path));
        TestUtils.assertMultipleMatches(TestUtils.put(path));
    }

    @Test
    public void testThenAnswer() throws Exception {
        String path = "sample";
        RESTMockServer.whenGET(pathEndsWith(path)).thenAnswer(new MockAnswer() {

            @Override
            public MockResponse answer(RecordedRequest request) {
                if ("True".equals(request.getHeaders().get("header"))) {
                    return new MockResponse().setBody("OK").setResponseCode(200);
                } else {
                    return new MockResponse().setBody("NOT OK").setResponseCode(400);
                }
            }
        });
        Response responseTrue = TestUtils.get(path, new AbstractMap.SimpleEntry<>("header", "True"));
        Response responseFalse = TestUtils.get(path, new AbstractMap.SimpleEntry<>("header", "False"));
        assertEquals(200, responseTrue.code());
        assertEquals(400, responseFalse.code());
        assertEquals("OK", responseTrue.body().string());
        assertEquals("NOT OK", responseFalse.body().string());
    }
}
