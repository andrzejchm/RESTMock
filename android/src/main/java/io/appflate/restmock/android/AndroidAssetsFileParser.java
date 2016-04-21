/*
 * Copyright (C) 2016 Appflate
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

package io.appflate.restmock.android;

import android.content.Context;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import io.appflate.restmock.MocksFileParser;

/**
 * Created by andrzejchm on 21/04/16.
 */
public class AndroidAssetsFileParser implements MocksFileParser {
    private Context testContext;

    public AndroidAssetsFileParser(Context testContexts) {
        this.testContext = testContext;
    }

    @Override
    public String readJsonFile(String jsonFilePath) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(
                testContext.getAssets().open(
                        jsonFilePath)));
        String line;
        StringBuilder text = new StringBuilder();

        while ((line = br.readLine()) != null) {
            text.append(line);
        }
        br.close();
        return text.toString();
    }
}
