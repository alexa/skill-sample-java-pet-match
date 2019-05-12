/*
     Copyright 2019 Amazon.com, Inc. or its affiliates. All Rights Reserved.

     Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
     except in compliance with the License. A copy of the License is located at

         http://aws.amazon.com/apache2.0/

     or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
     the specific language governing permissions and limitations under the License.
*/

package com.amazon.ask.petmatch.util;

import com.amazon.ask.exception.AskSdkException;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class SkillUtils {
    public static <T> T getRandomObject(List<T> list) {
        return list.get(new Random().nextInt(list.size()));
    }

    public static Map<String, String> buildPetMatchOptions(Map<String, HashMap<String, String>> slotValues) {
        final String[] params = buildPetMatchParams(slotValues);
        return buildHttpGetOptions(SkillData.petMatchApi.get("hostname"), SkillData.petMatchApi.get("pets"), "443", params);
    }

    private static String[] buildPetMatchParams(Map<String, HashMap<String, String>> slotValues) {
        return new String[]{"SSET", String.format("canine-%s-%s-%s",
                slotValues.get("energy").get("resolved"),
                slotValues.get("size").get("resolved"),
                slotValues.get("temperament").get("resolved"))};
    }

    private static Map<String, String> buildHttpGetOptions(String hostname, String path, String port, String[] params) {
        return new HashMap<String, String>() {
            {
                put("hostname", hostname);
                put("path", path + buildQueryString(params));
                put("port", port);
                put("method", "GET");
            }
        };
    }

    private static String buildQueryString(String[] params) {
        String queryString = "";
        for(int i = 0; i < params.length; i++) {
            queryString += i == 0 ? "?" : "&";
            try {
                queryString += String.format("%s=%s", URLEncoder.encode(params[0], "UTF-8"), URLEncoder.encode(params[1], "UTF-8"));
            } catch(UnsupportedEncodingException e) {
                throw new AskSdkException("Unable to encode the URL");
            }
        }
        return queryString;
    }
}
