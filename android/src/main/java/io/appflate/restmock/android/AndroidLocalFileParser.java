/*
 *	Copyright (C) 2016 Scott Johnson, jaywir3@gmail.com
 *
 *	Licensed under the Apache License, Version 2.0 (the "License");
 *	you may not use this file except in compliance with the License.
 *	You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *	distributed under the License is distributed on an "AS IS" BASIS,
 * 	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * 	See the License for the specific language governing permissions and
 *	limitations under the License.
 *
 */

package io.appflate.restmock.android;

import android.app.Application;

import java.io.File;
import java.net.URL;
import java.util.Scanner;

import io.appflate.restmock.RESTMockFileParser;

/**
 * An implementation of {@link RESTMockFileParser} that allows the retrieval and parsing of files on
 * the local filesystem. This does require an Android virtual file system to be set up, so it can be
 * used in lieu of the {@link AndroidAssetsFileParser} when running within Unit Tests.
 */
public class AndroidLocalFileParser implements RESTMockFileParser {
  private Application application;

  public AndroidLocalFileParser(Application application) {
    this.application = application;
  }

  @Override
  public String readJsonFile(String jsonFilePath) throws Exception {
    ClassLoader classLoader = application.getClass().getClassLoader();
    URL resource = classLoader.getResource(jsonFilePath);
    File file = new File(resource.getPath());

    StringBuilder fileContents = new StringBuilder((int) file.length());
    Scanner scanner = new Scanner(file, "UTF-8");
    String lineSeparator = System.getProperty("line.separator");

    try {
      while (scanner.hasNextLine()) {
        fileContents.append(scanner.nextLine()).append(lineSeparator);
      }
      return fileContents.toString();
    } finally {
      scanner.close();
    }
  }
}
