/*
 *	Copyright (C) 2016 Scott Johnson, jaywir3@gmail.com
 *
 *	Licensed under the Apache License, Version 2.0 (the "License");
 *	you may not use this file except in compliance with the License.
 *	You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *	distributed under the License is distributed on an "AS IS" BASIS,
 * 	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * 	See the License for the specific language governing permissions and
 *	limitations under the License.
 *
 */
package io.appflate.restmock.androidsample.junit;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import javax.inject.Inject;

import io.appflate.restmock.JVMFileParser;
import io.appflate.restmock.RESTMockServer;
import io.appflate.restmock.RESTMockServerStarter;
import io.appflate.restmock.androidsample.domain.GithubApi;
import io.appflate.restmock.androidsample.model.Repository;
import io.appflate.restmock.androidsample.model.User;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static io.appflate.restmock.utils.RequestMatchers.pathEndsWith;
import static junit.framework.Assert.assertEquals;

public class JUnitExampleTest {

    public static final String USERNAME_JWIR3 = "jwir3";
    public static final String PATH_JWIR3_PROFILE = "users/jwir3/profile.json";
    public static final String PATH_JWIR3_REPOS = "users/jwir3/repos.json";
    private static final String PATH_USER_NOT_FOUND = "users/user_not_found.json";
    private static final String REPOS = "/repos";

    // Data about my github profile to check against.
    public static final String JWIR3_NAME = "Scott Johnson";
    public static final String JWIR3_COMPANY = "Aperture Science";
    public static final String JWIR3_BLOG = "www.jwir3.com";
    public static final String JWIR3_LOCATION = "Burnsville, MN, USA";
    public static final String JWIR3_EMAIL = "jaywir3@gmail.com";

    @Inject
    GithubApi api;

    @Before
    public void setUp() {
        // Be sure to reset the server before each test
        RESTMockServerStarter.startSync(new JVMFileParser());
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RESTMockServer.getUrl())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        api = retrofit.create(GithubApi.class);
    }

    @Test
    public void testValidUser() throws Exception {
        RESTMockServer.whenGET(pathEndsWith(USERNAME_JWIR3)).thenReturnFile(200, PATH_JWIR3_PROFILE);

        // Note: This is not recommended in non-test code, since this is a blocking call.
        // TODO: Use RxJava magic here to make this easier to show how to accomplish asynchronously.
        Response<User> response = api.getUserProfile(USERNAME_JWIR3).execute();
        assertEquals(200, response.code());

        User jwir3 = response.body();
        assertEquals(JWIR3_NAME, jwir3.name);
        assertEquals(JWIR3_BLOG, jwir3.blog);
        assertEquals(JWIR3_COMPANY, jwir3.company);
        assertEquals(JWIR3_EMAIL, jwir3.email);
        assertEquals(JWIR3_LOCATION, jwir3.location);
    }

    @Test
    public void testNotFound() throws Exception {
        RESTMockServer.whenGET(pathEndsWith(USERNAME_JWIR3)).thenReturnFile(404, PATH_USER_NOT_FOUND);

        // Note: This is not recommended in non-test code, since this is a blocking call.
        // TODO: Use RxJava magic here to make this easier to show how to accomplish asynchronously.
        Response<User> res = api.getUserProfile(USERNAME_JWIR3).execute();
        assertEquals(404, res.code());
    }

    @Test
    public void testShowRepos() throws Exception {
        RESTMockServer.whenGET(pathEndsWith(REPOS)).thenReturnFile(200, PATH_JWIR3_REPOS);

        // Note: This is not recommended in non-test code, since this is a blocking call.
        // TODO: Use RxJava magic here to make this easier to show how to accomplish asynchronously.
        Response<List<Repository>> res = api.getUserRepos(USERNAME_JWIR3).execute();
        assertEquals(200, res.code());

        List<Repository> repos = res.body();
        assertEquals(29, repos.size());
    }
}