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

package io.appflate.restmock.androidsample.di;

import dagger.Module;
import dagger.Provides;
import io.appflate.restmock.androidsample.domain.GithubApi;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by andrzejchm on 23/04/16.
 */
@Module
public class AppModule {

    private String baseUrl;

    private SSLSocketFactory socketFactory;

    private X509TrustManager trustManager;

    public AppModule(String baseUrl, SSLSocketFactory socketFactory, X509TrustManager trustManager) {
        this.baseUrl = baseUrl;
        this.socketFactory = socketFactory;
        this.trustManager = trustManager;
    }

    @Provides
    GithubApi provideRestService() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        if (socketFactory != null && trustManager != null) {
            clientBuilder.sslSocketFactory(socketFactory, trustManager).addInterceptor(interceptor);
        }

        Retrofit retrofit = new Retrofit.Builder().baseUrl(baseUrl)
            .client(clientBuilder.build())
            .addConverterFactory(GsonConverterFactory.create())
            .build();

        return retrofit.create(GithubApi.class);
    }
}
