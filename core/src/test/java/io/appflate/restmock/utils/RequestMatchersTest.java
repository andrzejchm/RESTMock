package io.appflate.restmock.utils;

import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

import okhttp3.Headers;
import okhttp3.mockwebserver.RecordedRequest;

import static io.appflate.restmock.utils.RequestMatchers.hasHeaderNames;
import static junit.framework.Assert.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RequestMatchersTest {

    @Test
    public void endingSlashNotTakenIntoAccount() throws IOException {
        // given
        RecordedRequest recordedRequest = createRecordedRequest("foo/bar/");

        // when
        RequestMatcher matcher = RequestMatchers.pathEndsWith("bar");

        // then
        assertTrue(matcher.matches(recordedRequest));
    }

    @Test
    public void queryParamsAreTakenIntoAccount() throws IOException {
        // given
        RecordedRequest recordedRequest = createRecordedRequest("foo/bar?baz=boo");

        // when
        RequestMatcher matcher = RequestMatchers.pathEndsWith("boo");

        // then
        assertTrue(matcher.matches(recordedRequest));
    }

    @Test
    public void queryParamsAreIgnored() throws IOException {
        // given
        RecordedRequest recordedRequest = createRecordedRequest("foo/bar?baz=boo");

        // when
        RequestMatcher matcher = RequestMatchers.pathEndsWithIgnoringQueryParams("bar");

        // then
        assertTrue(matcher.matches(recordedRequest));
    }

    @Test
    public void shouldRecognizeProperQueryParameters() throws IOException {
        // given
        RecordedRequest recordedRequest = createRecordedRequest("foo/bar?baz=ban");

        // when
        RequestMatcher matcher = RequestMatchers.hasQueryParameters();

        // then
        assertTrue(matcher.matches(recordedRequest));
    }

    @Test
    public void shouldMatchProperSubsetOfQueryParametersNames() throws IOException {
        // given
        RecordedRequest recordedRequest = createRecordedRequest("foo/?bar=bar&baz=baz&boo=boo");

        // when
        RequestMatcher matcher = RequestMatchers.hasQueryParameterNames("bar", "baz");

        // then
        assertTrue(matcher.matches(recordedRequest));
    }

    @Test
    public void shouldNotMatchInproperSubsetOfQueryParametersNames() throws IOException {
        // given
        RecordedRequest recordedRequest = createRecordedRequest("foo/?bar=bar&baz=baz&boo=boo");

        // when
        RequestMatcher matcher = RequestMatchers.hasQueryParameterNames("bar", "ban");

        // then
        assertFalse(matcher.matches(recordedRequest));
    }

    @Test
    public void hasHeaderNamesFailWhenNoHeaders() {
        //given
        RecordedRequest request = createRecordedRequest("path");

        //when
        RequestMatcher matcher = hasHeaderNames("header1");

        //then
        assertFalse(matcher.matches(request));
    }

    @Test
    public void hasHeaderNamesSuccessWhenMatchesAll() {
        //given
        RecordedRequest request = createRecordedRequest("path", "h1", "v1", "h2", "v2", "h3", "v3");

        //when
        RequestMatcher matcher = hasHeaderNames("h1", "h2", "h3");

        //then
        assertTrue(matcher.matches(request));
    }

    @Test
    public void hasHeaderNamesSuccessWhenMatchesAllAsSubset() {
        //given
        RecordedRequest request = createRecordedRequest("path", "h1", "v1", "h2", "v2", "h3", "v3");

        //when
        RequestMatcher matcher = hasHeaderNames("h1", "h2");

        //then
        assertTrue(matcher.matches(request));
    }

    private RecordedRequest createRecordedRequest(String path, String... headerNamesAndValues) {
        Socket socket = Mockito.mock(Socket.class);
        when(socket.getInetAddress()).thenReturn(mock(InetAddress.class));
        Headers headers = null;
        if (headerNamesAndValues != null && headerNamesAndValues.length >= 2) {
            headers = Headers.of(headerNamesAndValues);
        }
        return new RecordedRequest("GET " + path + " HTTP/1.1", headers, null, 0, null, 0, socket);
    }
}
