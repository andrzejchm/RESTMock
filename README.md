
# RESTMock
[![](https://jitpack.io/v/andrzejchm/RESTMock.svg)](https://jitpack.io/#andrzejchm/RESTMock)
[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-RESTMock-green.svg?style=true)](https://android-arsenal.com/details/1/3468) [![Circle CI](https://circleci.com/gh/andrzejchm/RESTMock.svg?style=svg)](https://circleci.com/gh/andrzejchm/RESTMock)

REST API mocking made easy.

RESTMock is a library working on top of Square's [okhttp/MockWebServer](https://github.com/square/okhttp/tree/master/mockwebserver). It allows you to specify [Hamcrest](https://github.com/hamcrest/JavaHamcrest) matchers to match HTTP requests and specify what response to return. It is as easy as:


```java
RESTMockServer.whenGET(pathContains("users/defunkt"))
            .thenReturnFile(200, "users/defunkt.json");
```
**Article**
- [ITDD - Instrumentation TDD for Android](https://medium.com/@andrzejchm/ittd-instrumentation-ttd-for-android-4894cbb82d37)

## Table of contents
<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->


- [Setup](#setup)
    - [Step 1: Repository](#step-1-repository)
    - [Step 2: Dependencies](#step-2-dependencies)
    - [Step 3: Start the server](#step-3-start-the-server)
      - [a) RESTMockTestRunner](#a-restmocktestrunner)
      - [b) RESTMockServerStarter](#b-restmockserverstarter)
    - [Step 4: Specify Mocks](#step-4-specify-mocks)
      - [a) Files](#a-files)
      - [b) Strings](#b-strings)
      - [c) MockResponse](#c-mockresponse)
      - [c) MockAnswer](#c-mockanswer)
    - [Step 5: Request Matchers](#step-5-request-matchers)
    - [Step 6: Specify API Endpoint](#step-6-specify-api-endpoint)
- [Response chains](#response-chains)
- [Response delays](#response-delays)
    - [Interleaving delays with responses](#interleaving-delays-with-responses)
- [Request verification](#request-verification)
- [Logging](#logging)
- [Android Sample Project](#android-sample-project)
- [Donation](#donation)
- [License](#license)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->

## Setup
Here are the basic rules to set up RESTMock for Android

#### Step 1: Repository
Add it in your root build.gradle at the end of repositories:

```groovy  
allprojects {
	repositories {
		...
		maven { url "https://jitpack.io" }
	}
}
```
#### Step 2: Dependencies
Add the dependency

```groovy  
dependencies {
	androidTestCompile 'com.github.andrzejchm.RESTMock:android:${LATEST_VERSION}'
}
```

#### Step 3: Start the server
It's good to start server before the tested application starts, there are few methods:

##### a) RESTMockTestRunner
To make it simple you can just use the predefined `RESTMockTestRunner` in your UI tests. It extends `AndroidJUnitRunner`:

```groovy
defaultConfig {
		...
    	testInstrumentationRunner 'io.appflate.restmock.android.RESTMockTestRunner'
    }
```
##### b) RESTMockServerStarter
If you have your custom test runner and you can't extend `RESTMockTestRunner`, you can always just call the `RESTMockServerStarter`. Actually `RESTMockTestRunner` is doing exactly the same thing:

```java
public class MyAppTestRunner extends AndroidJUnitRunner {
	...
	@Override
	public void onCreate(Bundle arguments) {
		super.onCreate(arguments);
		RESTMockServerStarter.startSync(new AndroidAssetsFileParser(getContext()),new AndroidLogger());
		...
	}
	...
}

```


#### Step 4: Specify Mocks

##### a) Files
By default, the `RESTMockTestRunner` uses `AndroidAssetsFileParser` as a mocks file parser, which reads the files from the assets folder. To make them visible for the RESTMock you have to put them in the correct folder in your project, for example:

	.../src/androidTest/assets/users/defunkt.json
This can be accessed like this:

```java
RESTMockServer.whenGET(pathContains("users/defunkt"))
            .thenReturnFile(200, "users/defunkt.json");
```

##### b) Strings
If the response You wish to return is simple, you can just specify a string:

```java
RESTMockServer.whenGET(pathContains("users/defunkt"))
            .thenReturnString(200, "{}");
```
##### c) MockResponse
If you wish to have a greater control over the response, you can pass the `MockResponse`
```java
RESTMockServer.whenGET(pathContains("users/defunkt")).thenReturn(new MockResponse().setBody("").setResponseCode(401).addHeader("Header","Value"));
```
##### c) MockAnswer
You can always build dynamic `MockResponse`s by using the `RecordedRequest` object
```java
RESTMockServer.whenGET(pathContains("users/defunkt")).thenAnswer(new MockAnswer() {

            @Override
            public MockResponse answer(RecordedRequest request) {
                    return new MockResponse()
                            .setBody(request.getHeaders().get("header1"))
                            .setResponseCode(200);
            }
        });
```


#### Step 5: Request Matchers
You can either use some of the predefined matchers from `RequestMatchers` util class, or create your own. remember to extend from `RequestMatcher`

#### Step 6: Specify API Endpoint
The most important step, in order for your app to communicate with the testServer, you have to specify it as an endpoint for all your API calls. For that, you can use the ` RESTMockServer.getUrl()`. If you use Retrofit, it is as easy as:

```java
RestAdapter adapter = new RestAdapter.Builder()
                .baseUrl(RESTMockServer.getUrl())
                ...
                .build();
```

take a look at [#68](https://github.com/andrzejchm/RESTMock/issues/68) for better reference

## HTTPS

By default, RESTMockServer will serve responses using Http. In order to use HTTPS, during initialization you have to pass RESTMockOptions` object with `useHttps` set to true:
```java
RESTMockServerStarter.startSync(new AndroidAssetsFileParser(getContext()),new AndroidLogger(), new RESTMockOptions.Builder().useHttps(true).build());
```

there is a possibility to set up your own `SSLSocketFactory` and `TrustManager`, if you do not specify those, then default ones will be created for you. You can easly retrieve them with `RESTMockServer.getSSLSocketFactory()` and `RESTMockServer.getTrustManager()` to be able to build your client that will accept the default certificate:

```java
new OkHttpClient.Builder().sslSocketFactory(RESTMockServer.getSSLSocketFactory(), RESTMockServer.getTrustManager()).build();
```

A sample how to use https with RESTMock in android tests can be found in `androidsample` gradle module within this repository.

## Response chains
You can chain different responses for a single request matcher, all the `thenReturn*()` methods accept varags parameter with response, or you can call those methods multiple times on a single matcher, examples:

```java
RESTMockServer.whenGET(pathEndsWith(path))
                .thenReturnString("a single call")
                .thenReturnEmpty(200)
                .thenReturnFile("jsonFile.json");
```

or 

```java
RESTMockServer.whenGET(pathEndsWith(path))
                .thenReturnString("a single call", "answer no 2", "answer no 3");
```

## Response delays
Delaying responses is accomplished with the `delay(TimeUnit timeUnit, long delay)` method. Delays can be specified in chain, just like chaining responses:
 
```java
RESTMockServer.whenGET(pathEndsWith(path))
                .thenReturnString("a single call")
                .delay(TimeUnit.SECONDS, 5)
                .delay(TimeUnit.SECONDS, 10)
                .delay(TimeUnit.SECONDS, 15);
```

or

```java
RESTMockServer.whenGET(pathEndsWith(path))
                .thenReturnString("a single call")
                .delay(TimeUnit.SECONDS, 5, 10, 15);
```

Which will result in 1st response being delayed by 5 seconds, 2nd response by 10 seconds and 3rd, 4th, 5th... by 15 seconds.

#### Interleaving delays with responses
Check out this example:

```java
RESTMockServer.whenGET(pathEndsWith(path))
                .thenReturnString("1st call")
                .delay(TimeUnit.SECONDS, 5)
                .thenReturnString("2nd call")
                .delay(TimeUnit.SECONDS, 10)
                .delay(TimeUnit.SECONDS, 15)
                .thenReturnString("3rd call")
                .delay(TimeUnit.SECONDS, 20, 30, 40)
```
this will result in `1st call` being delayed by 5 seconds, `2nd call` delayed by 10 seconds, `3rd call` delayed by 15 seconds, another one by 20 seconds, and another by 30 seconds, and then every consecutive response with 40 seconds delay

## Request verification
It is possible to verify which requests were called and how many times thanks to `RequestsVerifier`. All you have to do is call one of these:

```java
//cheks if the GET request was invoked exactly 2 times
RequestsVerifier.verifyGET(pathEndsWith("users")).exactly(2);

//cheks if the GET request was invoked at least 3 times
RequestsVerifier.verifyGET(pathEndsWith("users")).atLeast(3);

//cheks if the GET request was invoked exactly 1 time
RequestsVerifier.verifyGET(pathEndsWith("users")).invoked();

//cheks if the GET request was never invoked
RequestsVerifier.verifyGET(pathEndsWith("users")).never();
```

Additionaly, you can manualy inspect requests received by RESTMockServer. All you have to do is to obtain them trough:

```java
//gets 5 most recent requests received. (ordered from oldest to newest)
RequestsVerifier.takeLast(5);

//gets 5 oldest requests received. (ordered from oldest to newest)
RequestsVerifier.takeFirst(5);

//gets all GET requests.  (ordered from oldest to newest)
RequestsVerifier.takeAllMatching(isGET());
```

## Logging
RESTMock supports logging events. You just have to provide the RESTMock with the implementation of `RESTMockLogger`. For Android there is an `AndroidLogger` implemented already. All you have to do is use the `RESTMockTestRunner` or call

```java
RESTMockServerStarter.startSync(new AndroidAssetsFileParser(getContext()),new AndroidLogger(), new RESTMockOptions());
```

or

```java
RESTMockServer.enableLogging(RESTMockLogger)
RESTMockServer.disableLogging()
```

## Android Sample Project
You can check out the sample Android app with tests [here](androidsample/)

## Donation
If you think the library is awesome and want to buy me a beer, you can do so by sending some...
* ![Ethereum](https://files.coinmarketcap.com/static/img/coins/16x16/1027.png) **ETH** here: `0xf7354a0F7B34A380f6d68a2661bE465C10D6AEd7`
* ![Bitcoin](https://files.coinmarketcap.com/static/img/coins/16x16/1.png) **BTC** here: `12bU3BMibFqbBBymaftXTDnoHojFymD7a6`
* ![NEO](https://files.coinmarketcap.com/static/img/coins/16x16/1376.png) **NEO** or **GAS** here: `AX1ovzRN2N28WJrtehjYXjwtHSvcqva6Ri`

## License

	Copyright (C) 2016 Appflate.io

 	Licensed under the Apache License, Version 2.0 (the "License");
 	you may not use this file except in compliance with the License.
 	You may obtain a copy of the License at

	http://www.apache.org/licenses/LICENSE-2.0

	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.
