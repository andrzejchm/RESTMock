package io.appflate.restmock.androidsample.di;

import dagger.Module;
import dagger.Provides;
import io.appflate.restmock.androidsample.domain.GithubApi;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by andrzejchm on 23/04/16.
 */
@Module
public class AppModule {

    private String baseUrl;

    public AppModule(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    @Provides
    GithubApi provideRestService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        GithubApi githubApi = retrofit.create(GithubApi.class);
        return githubApi;
    }
}
