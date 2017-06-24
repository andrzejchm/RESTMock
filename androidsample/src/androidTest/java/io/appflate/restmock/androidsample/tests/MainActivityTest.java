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

package io.appflate.restmock.androidsample.tests;

import android.support.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import io.appflate.restmock.RESTMockServer;
import io.appflate.restmock.RequestsVerifier;
import io.appflate.restmock.androidsample.pageobjects.MainActivityPageObject;
import io.appflate.restmock.androidsample.view.activities.MainActivity;

import static io.appflate.restmock.utils.RequestMatchers.pathEndsWith;

/**
 * Created by andrzejchm on 23/04/16.
 */
public class MainActivityTest {

    public static final String USERNAME_ANDRZEJCHM = "andrzejchm";
    public static final String PATH_ANDRZEJCHM_PROFILE = "mocks/users/andrzejchm/index.json";
    private static final String NAME_ANDRZEJ_CHMIELEWSKI = "RESTMock: Andrzej Chmielewski";
    private static final String PATH_USER_NOT_FOUND = "mocks/users/user_not_found.json";
    private static final String REPOS = "/repos";
    @Rule
    public ActivityTestRule<MainActivity> rule = new ActivityTestRule<>(
            MainActivity.class,
            true,
            false);
    private MainActivityPageObject pageObject;

    @Before
    public void setUp() throws Exception {
        pageObject = new MainActivityPageObject();
        //be sure to reset it before each test!
        RESTMockServer.reset();
    }

    @Test
    public void testGoodAnswer() throws Exception {
        RESTMockServer.whenGET(pathEndsWith(USERNAME_ANDRZEJCHM)).thenReturnFile(
                PATH_ANDRZEJCHM_PROFILE);
        //launches activity with default intent
        rule.launchActivity(null);
        pageObject.typeUsername(USERNAME_ANDRZEJCHM);
        pageObject.pressOk();
        pageObject.verifyWelcomeMessageForUser(NAME_ANDRZEJ_CHMIELEWSKI);
        RequestsVerifier.verifyRequest(pathEndsWith(USERNAME_ANDRZEJCHM)).invoked();
    }

    @Test
    public void testNotFound() throws Exception {
        RESTMockServer.whenGET(pathEndsWith(USERNAME_ANDRZEJCHM)).thenReturnFile(404, PATH_USER_NOT_FOUND);
        //launches activity with default intent
        rule.launchActivity(null);
        pageObject.typeUsername(USERNAME_ANDRZEJCHM);
        pageObject.pressOk();
        pageObject.verifyErrorWelcomeMessage();
        RequestsVerifier.verifyRequest(pathEndsWith(USERNAME_ANDRZEJCHM)).invoked();
    }

    @Test
    public void testShowEmptyRepos() throws Exception {
        RESTMockServer.whenGET(pathEndsWith(USERNAME_ANDRZEJCHM)).thenReturnFile(
                PATH_ANDRZEJCHM_PROFILE);
        RESTMockServer.whenGET(pathEndsWith(REPOS)).thenReturnString("[]");
        //launches activity with default intent
        rule.launchActivity(null);
        pageObject.typeUsername(USERNAME_ANDRZEJCHM);
        pageObject.pressOk();
        pageObject.verifyWelcomeMessageForUser(NAME_ANDRZEJ_CHMIELEWSKI);
        pageObject.clickShowRepos();
        RequestsVerifier.verifyRequest(pathEndsWith(USERNAME_ANDRZEJCHM)).invoked();
    }
}
