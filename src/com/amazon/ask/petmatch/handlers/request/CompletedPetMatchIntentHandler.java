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
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;
import com.amazon.ask.model.slu.entityresolution.StatusCode;
import com.amazon.ask.petmatch.util.HttpUtils;
import com.amazon.ask.request.RequestHelper;
import com.amazon.ask.util.impl.ObjectMapperFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.CloseableHttpResponse;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.amazon.ask.petmatch.util.SkillUtils.buildPetMatchOptions;

public class CompletedPetMatchIntentHandler implements IntentRequestHandler {

    private static final ObjectMapper MAPPER = ObjectMapperFactory.getMapper();

    @Override
    public boolean canHandle(HandlerInput handlerInput, IntentRequest request) {
        final RequestHelper helper = RequestHelper.forHandlerInput(handlerInput);
        return helper.getRequestType().equals("IntentRequest") &&
                helper.getIntentName().equals("PetMatchIntent") &&
                helper.getDialogState().equals(DialogState.COMPLETED);
    }

    @Override
    public Optional<Response> handle(HandlerInput handlerInput, IntentRequest request) {
        final Map<String, Slot> filledSlots = request.getIntent().getSlots();
        final Map<String, HashMap<String, String>> slotValues = getSlotValues(filledSlots);
        final Map<String, String> petMatchOptions = buildPetMatchOptions(slotValues);
        final String endpoint = String.format("http://%s:%s%s",
                petMatchOptions.get("hostname"),
                petMatchOptions.get("port"),
                petMatchOptions.get("path"));
        String speechText;
        try (CloseableHttpResponse response = HttpUtils.doGet(endpoint)) {
            JsonNode node = MAPPER.readTree(response.getEntity().getContent());
            if(response.getStatusLine().getStatusCode() == 200 && response.getEntity().getContentLength() > 0) {
                speechText = String.format("So a %s %s %s energy dog sounds good for you. Consider a %s",
                        slotValues.get("size").get("resolved"),
                        slotValues.get("temperament").get("resolved"),
                        slotValues.get("energy").get("resolved"),
                        node.path(0).get("breed"));
            } else {
                speechText = String.format("I'm sorry I couldn't find a match for a %s %s %s dog",
                        slotValues.get("size").get("resolved"),
                        slotValues.get("temperament").get("resolved"),
                        slotValues.get("energy").get("resolved"));
            }
        } catch (IOException ex) {
            throw new RuntimeException("Could not parse response", ex);
        }
        return handlerInput.getResponseBuilder()
                .withSpeech(speechText)
                .build();
    }

    private Map<String, HashMap<String, String>> getSlotValues(Map<String, Slot> filledSlots) {
        for(Map.Entry<String, Slot> slots: filledSlots.entrySet()) {
            final Slot slot = slots.getValue();
            final StatusCode statusCode = slot.getResolutions().getResolutionsPerAuthority().get(0).getStatus().getCode();
            if(statusCode.equals(StatusCode.ER_SUCCESS_MATCH)) {
                return new HashMap<String, HashMap<String, String>>(){
                    {
                        put("name", new HashMap<String, String>(){
                            {
                                put("synonym", slot.getValue());
                                put("resolved", slot.getResolutions().getResolutionsPerAuthority().get(0).getValues().get(0).getValue().getName());
                                put("is_validated", "True");
                            }
                        });
                    }
                };
            }
            if(statusCode.equals(StatusCode.ER_SUCCESS_NO_MATCH)) {
                return new HashMap<String, HashMap<String, String>>(){
                    {
                        put("name", new HashMap<String, String>(){
                            {
                                put("synonym", slot.getValue());
                                put("resolved", slot.getValue());
                                put("is_validated", "False");
                            }
                        });
                    }
                };
            }
        }
        return Collections.emptyMap();
    }
}
