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

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import android.widget.ViewAnimator;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.appflate.restmock.androidsample.R;
import io.appflate.restmock.androidsample.SampleApplication;
import io.appflate.restmock.androidsample.domain.GithubApi;
import io.appflate.restmock.androidsample.model.Repository;
import io.appflate.restmock.androidsample.view.adapters.ReposRecyclerAdapter;
import java.util.List;
import javax.inject.Inject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReposActivity extends AppCompatActivity implements Callback<List<Repository>> {

    private static final String PARAM_USERNAME = "username";
    private String username;

    @Inject GithubApi githubApi;

    @BindView(R.id.reposRecyclerView) RecyclerView reposRecyclerView;
    @BindView(R.id.reposAnimator) ViewAnimator reposAnimator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repos);
        ButterKnife.bind(this);
        SampleApplication.getComponent().inject(this);
        if (getIntent() != null) {
            username = getIntent().getStringExtra(PARAM_USERNAME);
        }
        setTitle(username);
        githubApi.getUserRepos(username).enqueue(this);
    }

    public static Intent intent(Activity activity, String username) {
        Intent intent = new Intent(activity, ReposActivity.class);
        intent.putExtra(PARAM_USERNAME, username);
        return intent;
    }

    @Override
    public void onResponse(Call<List<Repository>> call, Response<List<Repository>> response) {
        if (response.isSuccessful()) {
            reposRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
            reposRecyclerView.setAdapter(new ReposRecyclerAdapter(response.body()));
            reposAnimator.setDisplayedChild(1);
        } else {
            onResponseFailure();
        }
    }

    @Override
    public void onFailure(Call<List<Repository>> call, Throwable t) {
        onResponseFailure();
    }

    private void onResponseFailure() {
        Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
    }
}
