# RESTMock
[![](https://jitpack.io/v/andrzejchm/RESTMock.svg)](https://jitpack.io/#andrzejchm/RESTMock)
[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-RESTMock-green.svg?style=true)](https://android-arsenal.com/details/1/3468)

REST API mocking made easy.

##About
RESTMock is a library working on top of Square's [okhttp/MockWebServer](https://github.com/square/okhttp/tree/master/mockwebserver). It allows you to specify [Hamcrest](https://github.com/hamcrest/JavaHamcrest) matchers to match HTTP requests and specify what response to return. It is as easy as:

```java
RESTMockServer.whenGET(pathContains("users/defunkt"))
            .thenReturnFile(200, "users/defunkt.json");
```
 
## Setup
Here are the basic rules to set up RESTMock for various platforms

##Android Instrumentation Tests
####Step 1: Repository
Add it in your root build.gradle at the end of repositories:

```groovy  
allprojects {
	repositories {
		...
		maven { url "https://jitpack.io" }
	}
}
```
####Step 2: Dependencies
Add the dependency

```groovy  
dependencies {
	androidTestCompile 'com.github.andrzejchm.RESTMock:android:0.0.3'
	androidTestCompile('com.github.andrzejchm.RESTMock:core:0.0.3') {
        exclude group: 'org.bouncycastle', module: 'bcprov-jdk15on'
    }
}
```

####Step 3: Start the server
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
		RESTMockServerStarter.startSync(new AndroidAssetsFileParser(InstrumentationRegistry.getContext()),new AndroidLogger());
		...
	}
	...
}
    
```
`Context` here is needed to read the mock json files from the assets. By using `InstrumentationRegistry.getContext()` you point to a Test apk's context, which means the files should be placed in `androidTest` subfolder, while using `InstrumentationRegistry.getTargetContext()` would require to put the files in `main` subfolder. More on that later.

####Step 4: Specify Mocks

#####a) Files
By default, the `RESTMockTestRunner` uses `AndroidAssetsFileParser` as a mocks file parser, which reads the files from the assets folder. To make them visible for the RESTMock you have to put them in the correct folder in your project, for example:

	.../src/androidTest/assets/users/defunkt.json
This can be accessed like this:

```java
RESTMockServer.whenGET(pathContains("users/defunkt"))
            .thenReturnFile(200, "users/defunkt.json");
```

#####b) Strings
If the response You wish to return is simple, you can just specify a string:

```java
RESTMockServer.whenGET(pathContains("users/defunkt"))
            .thenReturnString(200, "{}");
```
#####c) MockResponse
If you wish to have a greater control over the response, you can pass the `MockResponse`
```java
RESTMockServer.whenGET(pathContains("users/defunkt")).thenReturn(new MockResponse().setBody("").setResponseCode(401).addHeader("Header","Value"));
```

####Step 5: Request Matchers
You can either use some of the predefined matchers from `RequestMatchers` util class, or create your own. remember to extend from `RequestMatcher`

####Step 6: Specify API Endpoint
The most important step, in order for your app to communicate with the testServer, you have to specify it as an endpoint for all your API calls. For that, you can use the ` RESTMockServer.getUrl()`. If you use Retrofit, it is as easy as:

	RestAdapter adapter = new RestAdapter.Builder()
		...
                .setEndpoint(RESTMockServer.getUrl())
                ...
                .build();
	
##Android Unit Tests
TBD (Pullrequests welcomed)
##Java
TBD (Pullrequests welcomed)

####Step 1: Repository
```xml
<repositories>
	<repository>
	    <id>jitpack.io</id>
	    <url>https://jitpack.io</url>
	</repository>
</repositories>
```
####Step 2: Dependency
Add the dependency

```xml
<dependency>
    <groupId>com.github.andrzejchm.RESTMock</groupId>
    <artifactId>core</artifactId>
    <version>0.0.3</version>
</dependency>
```
####Step 3: TBD
TBD (Pullrequests welcomed)

#TODO
* setup CI
* create some Units
* add something simmilar to Mockito's `verify()`
* add android example

#License

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
