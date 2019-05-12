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
import com.amazon.ask.model.DialogState;
import com.amazon.ask.model.Intent;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;
import com.amazon.ask.model.SlotConfirmationStatus;
import com.amazon.ask.model.dialog.DelegateDirective;
import com.amazon.ask.model.dialog.ElicitSlotDirective;
import com.amazon.ask.model.slu.entityresolution.Resolution;
import com.amazon.ask.model.slu.entityresolution.StatusCode;
import com.amazon.ask.petmatch.util.SkillData;
import com.amazon.ask.request.RequestHelper;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class InProgressPetMatchIntentHandler implements IntentRequestHandler {

    @Override
    public boolean canHandle(HandlerInput handlerInput, IntentRequest intentRequest) {
        final RequestHelper helper = RequestHelper.forHandlerInput(handlerInput);
        return helper.getRequestType().equals("IntentRequest") &&
                helper.getIntentName().equals("PetMatchIntent") &&
                !helper.getDialogState().equals(DialogState.COMPLETED);
    }

    @Override
    public Optional<Response> handle(HandlerInput handlerInput, IntentRequest intentRequest) {
        final Intent currentIntent = intentRequest.getIntent();
        for(Map.Entry<String, Slot> entry: currentIntent.getSlots().entrySet()) {
            if(!SkillData.slotNames.contains(entry.getKey())) {
                final Slot currentSlot = entry.getValue();
                final Optional<Resolution> resolution = Optional.ofNullable(currentSlot.getResolutions().getResolutionsPerAuthority().get(0));
                if(!currentSlot.getConfirmationStatus().equals(SlotConfirmationStatus.CONFIRMED) && resolution.isPresent()) {
                    if(resolution.get().getStatus().getCode().equals(StatusCode.ER_SUCCESS_MATCH)) {
                        if(resolution.get().getValues().size() > 0) {
                            final String values = String.join(" or ", resolution.get().getValues().stream()
                                    .map(e -> e.getValue().getName())
                                    .collect(Collectors.toSet()));
                            String speech = String.format("Which would you like %s ?", values);
                            return handlerInput.getResponseBuilder()
                                    .withSpeech(speech)
                                    .withReprompt(speech)
                                    .addDirective(ElicitSlotDirective.builder().withSlotToElicit(currentSlot.getName()).build())
                                    .build();
                        } else if(resolution.get().getStatus().getCode().equals(StatusCode.ER_SUCCESS_NO_MATCH)) {
                            if(SkillData.slotNames.contains(currentSlot.getName())) {
                                final String speech = String.format("what %s are you looking for", currentSlot.getName());
                                return handlerInput.getResponseBuilder()
                                        .withSpeech(speech)
                                        .withReprompt(speech)
                                        .addDirective(ElicitSlotDirective.builder().withSlotToElicit(currentSlot.getName()).build())
                                        .build();
                            }
                        }
                    }
                }
            }
        }
        return handlerInput.getResponseBuilder()
                .addDirective(DelegateDirective.builder().withUpdatedIntent(currentIntent).build())
                .build();
    }
}
