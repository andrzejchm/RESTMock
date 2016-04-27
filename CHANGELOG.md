## 0.0.4 (27 April 2016)
**Features**

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

## 0.0.3 (23 April 2016)
* Sample android app
* Added logging capabilities to `RESTMockServer`. `RESTMockTestRunner` has enabled logging by default, if you use a custom TestRunner, use:

	 	RESTMockServerStarter.startSync(new AndroidAssetsFileParser(getContext()),new AndroidLogger());
	 	
Or you can enable/disable it anytime with `RESTMockServer.enableLogging(RESTMockLogger)` and `RESTMockServer.disableLogging()`
	
* bugfixes

## 0.0.2 (22 April 2016)
Initial release