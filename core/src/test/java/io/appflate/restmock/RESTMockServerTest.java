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

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;

import io.appflate.restmock.utils.TestUtils;
import okhttp3.OkHttpClient;

import static io.appflate.restmock.utils.RequestMatchers.pathEndsWith;
import static junit.framework.Assert.assertNull;
import static junit.framework.TestCase.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

/**
 * Created by andrzejchm on 25/04/16.
 */
public class RESTMockServerTest {
    static RESTMockFileParser fileParser;

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
    public void testWhenGET() throws Exception {
        String path = "/sample";
        String worksBody = "works";
        MatchableCall matchableCall = RESTMockServer.whenGET(pathEndsWith(path));
        assertNotNull(matchableCall);
        assertNull(matchableCall.response);
        verify(RESTMockServer.dispatcher, never()).addMatchableCall(matchableCall);
        TestUtils.assertNotMocked(TestUtils.get(path));
        matchableCall.thenReturnString(worksBody);
        TestUtils.assertResponseWithBodyContains(TestUtils.get(path), 200, "works");
        TestUtils.assertNotMocked(TestUtils.post(path));
        TestUtils.assertNotMocked(TestUtils.delete(path));
        TestUtils.assertNotMocked(TestUtils.put(path));
    }

    @Test
    public void testWhenPOST() throws Exception {
        String path = "/sample";
        String worksBody = "works";
        MatchableCall matchableCall = RESTMockServer.whenPOST(pathEndsWith(path));
        assertNotNull(matchableCall);
        assertNull(matchableCall.response);
        verify(RESTMockServer.dispatcher, never()).addMatchableCall(matchableCall);
        TestUtils.assertNotMocked(TestUtils.get(path));
        matchableCall.thenReturnString(worksBody);
        TestUtils.assertNotMocked(TestUtils.get(path));
        TestUtils.assertResponseWithBodyContains(TestUtils.post(path), 200, "works");
        TestUtils.assertNotMocked(TestUtils.delete(path));
        TestUtils.assertNotMocked(TestUtils.put(path));
    }

    @Test
    public void testWhenPUT() throws Exception {
        String path = "/sample";
        String worksBody = "works";
        MatchableCall matchableCall = RESTMockServer.whenPUT(pathEndsWith(path));
        assertNotNull(matchableCall);
        assertNull(matchableCall.response);
        verify(RESTMockServer.dispatcher, never()).addMatchableCall(matchableCall);
        TestUtils.assertNotMocked(TestUtils.get(path));
        matchableCall.thenReturnString(worksBody);
        TestUtils.assertNotMocked(TestUtils.get(path));
        TestUtils.assertNotMocked(TestUtils.post(path));
        TestUtils.assertNotMocked(TestUtils.delete(path));
        TestUtils.assertResponseWithBodyContains(TestUtils.put(path), 200, "works");
    }

    @Test
    public void testWhenDELETE() throws Exception {
        String path = "/sample";
        String worksBody = "works";
        MatchableCall matchableCall = RESTMockServer.whenDELETE(pathEndsWith(path));
        assertNotNull(matchableCall);
        assertNull(matchableCall.response);
        verify(RESTMockServer.dispatcher, never()).addMatchableCall(matchableCall);
        TestUtils.assertNotMocked(TestUtils.get(path));
        matchableCall.thenReturnString(worksBody);
        TestUtils.assertNotMocked(TestUtils.get(path));
        TestUtils.assertNotMocked(TestUtils.post(path));
        TestUtils.assertResponseWithBodyContains(TestUtils.delete(path), 200, worksBody);
        TestUtils.assertNotMocked(TestUtils.put(path));
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

    @AfterClass
    public static void teardownClass() throws IOException {
        RESTMockServer.shutdown();
    }
}
