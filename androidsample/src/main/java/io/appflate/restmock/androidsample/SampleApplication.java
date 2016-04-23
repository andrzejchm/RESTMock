package io.appflate.restmock.androidsample;

import android.app.Application;

import io.appflate.restmock.androidsample.di.AppComponent;
import io.appflate.restmock.androidsample.di.AppModule;
import io.appflate.restmock.androidsample.di.DaggerAppComponent;

/**
 * Created by andrzejchm on 22/04/16.
 */
public class SampleApplication extends Application {
    private static final String BASE_URL = "https://api.github.com/";
    private static AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        setupGraph();
    }

    private void setupGraph() {
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(BASE_URL))
                .build();
    }

    public static AppComponent getComponent() {
        return appComponent;
    }
}
