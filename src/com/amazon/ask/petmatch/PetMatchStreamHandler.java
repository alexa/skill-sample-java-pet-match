/*
     Copyright 2019 Amazon.com, Inc. or its affiliates. All Rights Reserved.

     Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
     except in compliance with the License. A copy of the License is located at

         http://aws.amazon.com/apache2.0/

     or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
     the specific language governing permissions and limitations under the License.
*/

package com.amazon.ask.petmatch;

import com.amazon.ask.Skill;
import com.amazon.ask.SkillStreamHandler;
import com.amazon.ask.Skills;
import com.amazon.ask.petmatch.handlers.exception.CatchAllExceptionHandler;
import com.amazon.ask.petmatch.handlers.request.CompletedPetMatchIntentHandler;
import com.amazon.ask.petmatch.handlers.request.ExitIntentHandler;
import com.amazon.ask.petmatch.handlers.request.FallbackIntentHandler;
import com.amazon.ask.petmatch.handlers.request.HelpIntentHandler;
import com.amazon.ask.petmatch.handlers.request.InProgressPetMatchIntentHandler;
import com.amazon.ask.petmatch.handlers.request.LaunchHandler;
import com.amazon.ask.petmatch.handlers.request.MythicalCreaturesHandler;
import com.amazon.ask.petmatch.handlers.request.SessionEndedHandler;
import com.amazon.ask.petmatch.requestinterceptors.RequestLogger;
import com.amazon.ask.petmatch.responseinterceptors.ResponseLogger;

public class PetMatchStreamHandler extends SkillStreamHandler {
    private static Skill getSkill() {
        return Skills.standard()
                .addRequestInterceptor(new RequestLogger())
                .addRequestHandlers(
                        new LaunchHandler(),
                        new InProgressPetMatchIntentHandler(),
                        new MythicalCreaturesHandler(),
                        new CompletedPetMatchIntentHandler(),
                        new FallbackIntentHandler(),
                        new HelpIntentHandler(),
                        new ExitIntentHandler(),
                        new SessionEndedHandler()
                )
                .addResponseInterceptor(new ResponseLogger())
                .addExceptionHandler(new CatchAllExceptionHandler())
                // Add your skill id below
                // .withSkillId("")
                .build();
    }

    public PetMatchStreamHandler() {
        super(getSkill());
    }
}
