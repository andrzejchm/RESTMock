## 0.1.1 (22nd May 2016)

* Added convienience methods for verifying `GET, POST, PUT, DELETE` methods to `RequestVerifier` (credits to @matir91)


## 0.1.0 (13th May 2016)

* Added support for unit tests/robolectric tests. See [Unit Tests with Robolectric](README.md#unit-tests-with-robolectric) for more info (credits to @jwir3)

## 0.0.5 (5th May 2016)
**Bugfixes**

* Removed unnecesary <application> tag from AndroidManifest.xml to no longer cause Manifest merge conflicts with espresso-intents dependency

## 0.0.4 (27th April 2016)
* **Request verification**
It is possible to verify which requests were called and how many times thanks to `RequestVerifier`.


**Quality**

* added Continuous Integration (#8)
* bumped `mockwebserver` version to `3.2.0` (#6)
* Added unit tests for `RESTMockServer` and `RequestVerifier` (#5 #8),
* added hamcrest as a dependency since not all are using Espresso (#5),
* added findbugs checks for all modules (#8),

**Misc**

* renamed method `RESTMockServer.removeAllMatchableCalls()` to `RESTMockServer.reset()` (#5)
* generified `when*` methods in `RESTMockServer` to make it possible to use meta-matchers like `allOf` and `anyOf` (#5).

## 0.0.3 (23th April 2016)
* Sample android app
* Added logging capabilities to `RESTMockServer`. `RESTMockTestRunner` has enabled logging by default, if you use a custom TestRunner, use:

	 	RESTMockServerStarter.startSync(new AndroidAssetsFileParser(getContext()),new AndroidLogger());
	 	
Or you can enable/disable it anytime with `RESTMockServer.enableLogging(RESTMockLogger)` and `RESTMockServer.disableLogging()`
	
* bugfixes

## 0.0.2 (22th April 2016)
Initial release