## 0.0.3 (23 April 2016)
* Sample android app
* Added logging capabilities to `RESTMockServer`. `RESTMockTestRunner` has enabled logging by default, if you use a custom TestRunner, use:

	 	RESTMockServerStarter.startSync(new AndroidAssetsFileParser(getContext()),new AndroidLogger());
	 	
Or you can enable/disable it anytime with `RESTMockServer.enableLogging(RESTMockLogger)` and `RESTMockServer.disableLogging()`
	
* bugfixes

## 0.0.2 (22 April 2016)
Initial release