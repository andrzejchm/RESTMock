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

package io.appflate.restmock;

/**
 * Used for parsing json files from local storage to {@code String}
 */
public interface RESTMockFileParser {

    /**
     * reads the json file from {@code jsonFilePath} and returns its contents as a {@code String}
     * <p> It's {@code RESTMockFileParser}'s implementation responsibility to determine how to
     * resolve the given {@code jsonFilePath}.</p>
     *
     * @param jsonFilePath a path to json file.
     * @return json file's contents as a String.
     * @throws Exception when an error occurs while reading the file (f.e. {@link java.io.IOException})
     */
    String readJsonFile(String jsonFilePath) throws Exception;
}
