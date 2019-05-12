/*
     Copyright 2019 Amazon.com, Inc. or its affiliates. All Rights Reserved.

     Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
     except in compliance with the License. A copy of the License is located at

         http://aws.amazon.com/apache2.0/

     or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
     the specific language governing permissions and limitations under the License.
*/

package com.amazon.ask.petmatch.handlers.request;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.impl.IntentRequestHandler;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import com.amazon.ask.petmatch.util.SkillData;
import com.amazon.ask.request.RequestHelper;

import java.util.Map;
import java.util.Optional;

import static com.amazon.ask.petmatch.util.SkillUtils.getRandomObject;

public class MythicalCreaturesHandler implements IntentRequestHandler {

    @Override
    public boolean canHandle(HandlerInput handlerInput, IntentRequest intentRequest) {
        final RequestHelper helper = RequestHelper.forHandlerInput(handlerInput);
        if(!helper.getRequestType().equals("IntentRequest") || !helper.getIntentName().equals("PetMatchIntent")) {
            return false;
        }
        final String slotName = "pet";
        final String slotValue = "mythical_creature";
        Optional<String> resolutionsSlotValue = getResolvedValue(helper, slotName);
        if(resolutionsSlotValue.isPresent() && resolutionsSlotValue.get().equals(slotValue)) {
            handlerInput.getAttributesManager().getSessionAttributes().put(slotValue, helper.getSlot(slotName).get().getValue());
            return true;
        }
        return false;
    }

    @Override
    public Optional<Response> handle(HandlerInput handlerInput, IntentRequest intentRequest) {
        Map<String, Object> sessionAttributes = handlerInput.getAttributesManager().getSessionAttributes();
        final String speechText = String.format(getRandomObject(SkillData.slotsMeta.get("pet").get("invalid_responses")),
                sessionAttributes.get("mythical_creature"));
        return handlerInput.getResponseBuilder().withSpeech(speechText).build();
    }

    private Optional<String> getResolvedValue(RequestHelper requestHelper, String slotName) {
        return Optional.ofNullable(requestHelper.getSlot(slotName))
                .map(Optional::get)
                .map(slot -> slot.getResolutions().getResolutionsPerAuthority().get(0).getValues().get(0).getValue().getName());
    }
}
