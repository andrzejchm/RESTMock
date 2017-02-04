package io.appflate.restmock;

import org.junit.Test;

import java.net.URL;
import java.util.List;

import io.appflate.restmock.utils.QueryParam;
import io.appflate.restmock.utils.RestMockUtils;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

public class QueryParamTest {

    @Test
    public void testBasicQueryParamSplit() throws Exception {
        URL url = new URL("https://www.jwir3.com/someRequest?flag=true&session_length=2");

        List<QueryParam> params = RestMockUtils.splitQuery(url);

        QueryParam expectedParam1 = new QueryParam("flag", "true");
        QueryParam expectedParam2 = new QueryParam("session_length", "2");

        assertEquals(2, params.size());
        assertTrue(params.contains(expectedParam1));
        assertTrue(params.contains(expectedParam2));
    }

    @Test
    public void testMultipleValueQueryParamSplit() throws Exception {
        URL url = new URL("https://www.jwir3.com/someRequest?user_id=1&user_id=2");

        List<QueryParam> params = RestMockUtils.splitQuery(url);

        QueryParam expectedParam1 = new QueryParam("user_id", "2", "1");

        assertEquals(1, params.size());
        assertTrue(params.contains(expectedParam1));
    }
}
