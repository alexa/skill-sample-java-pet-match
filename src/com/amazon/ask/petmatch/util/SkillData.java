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

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SkillData {

    public static final Map<String, String> petMatchApi = new HashMap<String, String>(){
        {
            put("hostname", "e4v7rdwl7l.execute-api.us-east-1.amazonaws.com");
            put("pets", "/Test");
        }
    };

    public static final Set<String> requiredSlots = new HashSet<>(Arrays.asList("energy", "size", "temperament"));

    public static final Map<String, Map<String, List<String>>> slotsMeta = new HashMap<String, Map<String, List<String>>>(){
        {
            put("pet", new HashMap<String, List<String>>(){
                {
                    put("invalid_responses", Arrays.asList("I'm sorry, but I'm not qualified to match you with %s.",
                            "Ah yes, %s are splendid creatures, but unfortunately owning one as a pet is outlawed.",
                            "I'm sorry I can't match you with %s."));
                    put("error_default", Collections.singletonList("I'm sorry I can't match you with %s"));
                }
            });
        }
    };

    public static final Set<String> slotNames = new HashSet<>(Arrays.asList("article", "at_the", "I_Want"));
}
