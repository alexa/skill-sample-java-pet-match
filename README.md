Build an Alexa Pet Match Skill in ASK Java SDK
==============================================

<img src="https://m.media-amazon.com/images/G/01/mobile-apps/dex/alexa/alexa-skills-kit/tutorials/quiz-game/header._TTH_.png" />

**NOTE:** This project uses Dialog Management. If you're looking for **Pet Match** with
[Alexa Conversations](https://developer.amazon.com/en-US/docs/alexa/conversations/about-alexa-conversations.html) Please see: [skill-sample-nodejs-alexa-conversations-pet-match](https://github.com/alexa/skill-sample-nodejs-alexa-conversations-pet-match). Learn 
Alexa Conversations with [Tutorial: Build Multi-turn Skills with Alexa Conversations](https://developer.amazon.com/en-US/alexa/alexa-skills-kit/get-deeper/tutorials-code-samples/build-multi-turn-skills-with-alexa-conversations).

In this workshop you will create a skill called Pet Match that matches the user with a pet. When launched, this Alexa Skill will prompt the user for the information it needs to determine a match. Once all of the required information is collected, it will send the data to an external web service which processes the data and returns the match.

Through this workshop, you'll learn how to use advanced Alexa Skills Kit features to create and configure an Alexa Skill and AWS Lambda. The features you'll learn to use are Dialog Management and Entity Resolution. These features leverage Alexa's Automatic Speech Recognition (ASR), Natural Language Understanding (NLU), and Machine Learning (ML), which makes your life easier because you don't have to write code. You only need to provide training data to the Alexa engines via your interaction model. The skill builder makes it easy to do so.

Setup
-----

To run this example skill you need to do two things. The first is to deploy the example code in lambda, and the second is to configure the Alexa skill to use Lambda.

[![Get Started](https://camo.githubusercontent.com/db9b9ce26327ad3bac57ec4daf0961a382d75790/68747470733a2f2f6d2e6d656469612d616d617a6f6e2e636f6d2f696d616765732f472f30312f6d6f62696c652d617070732f6465782f616c6578612f616c6578612d736b696c6c732d6b69742f7475746f7269616c732f67656e6572616c2f627574746f6e732f627574746f6e5f6765745f737461727465642e5f5454485f2e706e67)](./instructions/1-voice-user-interface.md)

Additional Resources
--------------------

### Community

-  [Amazon Developer Forums](https://forums.developer.amazon.com/spaces/165/index.html) : Join the conversation!
-  [Hackster.io](https://www.hackster.io/amazon-alexa) - See what others are building with Alexa.

### Tutorials & Guides

-  [Voice Design Guide](https://developer.amazon.com/designing-for-voice/) -
   A great resource for learning conversational and voice user interface design.

### Documentation

-  [Official Maven repository](https://mvnrepository.com/artifact/com.amazon.alexa/ask-sdk)
-  [Official Alexa Skills Kit Java SDK Docs](https://alexa-skills-kit-sdk-for-java.readthedocs.io/en/latest/index.html)
-  [Official Alexa Skills Kit Docs](https://developer.amazon.com/docs/ask-overviews/build-skills-with-the-alexa-skills-kit.html)
