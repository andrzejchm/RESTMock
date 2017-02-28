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

import java.io.IOException;

import io.appflate.restmock.RESTMockServer;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by andrzejchm on 25/04/16.
 */
public class TestUtils {

    private static final RequestBody EMPTY_JSON_BODY = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), "");
    private static OkHttpClient okHttpClient = new OkHttpClient();

    public static Response get(String path) throws IOException {
        path = normalizePath(path);
        return executeSync(requestBuilder().url(RESTMockServer.getUrl() + path).build());
    }

    public static Response post(String path) throws IOException {
        path = normalizePath(path);
        return executeSync(requestBuilder().url(RESTMockServer.getUrl() + path).method("POST", EMPTY_JSON_BODY).build());
    }

    public static Response put(String path) throws IOException {
        path = normalizePath(path);
        return executeSync(requestBuilder().url(RESTMockServer.getUrl() + path).method("PUT", EMPTY_JSON_BODY).build());
    }

    public static Response delete(String path) throws IOException {
        path = normalizePath(path);
        return executeSync(requestBuilder().url(RESTMockServer.getUrl() + path).method("DELETE", null).build());
    }

    public static Response head(String path) throws IOException {
        path = normalizePath(path);
        return executeSync(requestBuilder().url(RESTMockServer.getUrl() + path).method("HEAD", null).build());
    }

    private static Response executeSync(Request request) throws IOException {
        return okHttpClient.newCall(request).execute();
    }

    public static void assertNotMocked(Response response) throws IOException {
        assertResponseWithBodyContains(response, 500, RESTMockServer.RESPONSE_NOT_MOCKED);
    }

    public static void assertNotMockedNoBody(Response response) throws IOException {
        assertResponseCodeIs(response, 500);
    }

    public static void assertMultipleMatches(Response response) throws IOException {
        assertResponseWithBodyContains(response, 500, RESTMockServer.MORE_THAN_ONE_RESPONSE_ERROR);

    }

    public static void assertResponseCodeIs(Response response, int code) {
        assertEquals(code, response.code());
    }

    public static void assertResponseWithBodyContains(Response response, int code, String body) throws IOException {
        assertEquals(code, response.code());
        assertTrue(response.body().string().contains(body));
    }

    private static String normalizePath(String path) {
        if (path.startsWith("/")) {
            path = "/" + path;
        }
        return path;
    }

    private static Request.Builder requestBuilder() {
        return new Request.Builder().addHeader("Connection", "close");
    }
}
