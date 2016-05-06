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

package io.appflate.restmock.androidsample.view.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ViewAnimator;

import java.util.Locale;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.appflate.restmock.androidsample.R;
import io.appflate.restmock.androidsample.SampleApplication;
import io.appflate.restmock.androidsample.domain.GithubApi;
import io.appflate.restmock.androidsample.model.User;
import io.appflate.restmock.androidsample.utils.Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final int POSITION_CONTENT_VIEW = 0;
    private static final int POSITION_PROGRESS_VIEW = 1;
    @Inject GithubApi githubApi;

    @Bind(R.id.fullNameText) TextView fullNameTextView;
    @Bind(R.id.showReposButton) Button showReposButton;
    @Bind(R.id.usernameEditText) EditText usernameEditText;
    @Bind(R.id.resultAnimator) ViewAnimator resultAnimator;
    private String currentUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        SampleApplication.getComponent().inject(this);
    }

    @OnClick(R.id.submitButton)
    public void onSubmitClicked() {
        Utils.hideKeyboard(this);
        currentUsername = usernameEditText.getText().toString();
        resultAnimator.setDisplayedChild(POSITION_PROGRESS_VIEW);
        githubApi.getUserProfile(currentUsername).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call,
                                   Response<User> response) {
                if (response.isSuccessful()) {
                    User body = response.body();
                    String name = body.name == null ? body.login : body.name;
                    fullNameTextView.setText(String.format(Locale.US,
                            "Hello %s!",
                            name));
                    showReposButton.setVisibility(View.VISIBLE);
                    resultAnimator.setDisplayedChild(POSITION_CONTENT_VIEW);
                } else {
                    onRequestError();
                }
            }

            @Override
            public void onFailure(Call<User> call,
                                  Throwable t) {
                onRequestError();
            }
        });
    }

    @OnClick(R.id.showReposButton)
    public void onShowReposClicked() {
        startActivity(ReposActivity.intent(this, currentUsername));

    }

    private void onRequestError() {
        fullNameTextView.setText(R.string.something_went_wrong);
        showReposButton.setVisibility(View.GONE);
        resultAnimator.setDisplayedChild(POSITION_CONTENT_VIEW);
    }

    public GithubApi getApi() {
        return githubApi;
    }
}
