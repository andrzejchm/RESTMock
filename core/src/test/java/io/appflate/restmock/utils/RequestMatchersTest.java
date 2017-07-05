package io.appflate.restmock.utils;


import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

import okhttp3.mockwebserver.RecordedRequest;

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
    public void shouldRecognizeProperQueryParameters() throws IOException {
        // given
        RecordedRequest recordedRequest = createRecordedRequest("foo/bar?baz=ban");

        // when
        RequestMatcher matcher = RequestMatchers.hasQueryParameters();

        // then
        assertTrue(matcher.matches(recordedRequest));
    }

    private RecordedRequest createRecordedRequest(String path) {
        Socket socket = Mockito.mock(Socket.class);
        when(socket.getInetAddress()).thenReturn(mock(InetAddress.class));
        return new RecordedRequest("GET " + path + " HTTP/1.1", null, null, 0, null, 0, socket);
    }
}
