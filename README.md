# RESTMock

REST API mocking made easy.

##About
RESTMock is a library working on top of Square's [okhttp/MockWebServer](https://github.com/square/okhttp/tree/master/mockwebserver). It allows you to specify [Hamcrest](https://github.com/hamcrest/JavaHamcrest) matchers to match HTTP requests and specify what response to return. It is as easy as:

```java
RESTMockServer.whenGET(pathContains("users/defunkt"))
            .thenReturnFile(200, "users/defunkt.json");
```
 
## Setup
TBD 

##Android
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
	androidTestCompile 'com.github.andrzejchm.RESTMock:android:0.3'
	androidTestCompile('com.github.andrzejchm.RESTMock:core:0.3') {
        exclude group: 'org.bouncycastle', module: 'bcprov-jdk15on'
    }
}
```

####Step 3:
TBD

##Maven
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
    <version>0.3</version>
</dependency>
```