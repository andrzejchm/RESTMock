package io.appflate.restmock.androidsample.domain;

import java.util.List;

import io.appflate.restmock.androidsample.model.Repository;
import io.appflate.restmock.androidsample.model.User;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by andrzejchm on 22/04/16.
 */
public interface GithubApi {

    @GET("users/{username}")
    Call<User> getUserProfile(@Path("username") String username);

    @GET("users/{username}/repos")
    Call<List<Repository>>  getUserRepos(@Path("username") String username);

}
