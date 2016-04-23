package io.appflate.restmock.androidsample.di;

import javax.inject.Singleton;

import dagger.Component;
import io.appflate.restmock.androidsample.view.activities.MainActivity;
import io.appflate.restmock.androidsample.view.activities.ReposActivity;
import io.appflate.restmock.androidsample.domain.GithubApi;

/**
 * Created by andrzejchm on 23/04/16.
 */
@Singleton
@Component(modules = { AppModule.class})
public interface AppComponent {
    GithubApi getRestService();

    void inject(MainActivity mainActivity);
    void inject(ReposActivity reposActivity);
}
