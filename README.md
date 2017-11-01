Prerequisites

Sign up to the Google Cloud Platform (https://cloud.google.com)

Follow the steps on https://cloud.google.com/natural-language/docs/quickstart-client-libraries to create a project with nlp api support, create a service account and download the json authentication file for it. That's all we need from google for now, but it doesnt hurt to follow the rest of the quickstart steps and see if you can successfully run the example code.

The path to your google cloud json authentication file needs to be stored in the environment variable GOOGLE_APPLICATION_CREDENTIALS.

Now go talk to the telegram bot @BotFather to setup your bot and get the bot token. The token needs to be stored in the environment variable TELEGRAM_TOKEN and the bot name in TELEGRAM_BOT.

Here's an example of how to set the environment variables in bash:

export GOOGLE_APPLICATION_CREDENTIALS=~/<your jason file>
export TELEGRAM_TOKEN=<your token>
export TELEGRAM_BOT=<your bot name>

Next install apache derby (http://db.apache.org/derby/papers/DerbyTut/ - complete at least topic 1 - install software and topic 3 - embedded derby). The apache derby database resides in the data folder. The following table has been created:

create table entitysentiment (userid int, entity varchar(100), salience double, magnitude double, score double, data date);

Finally you'll also need maven (https://maven.apache.org/) available on your system.


Compilation

To compile and package in the root directory of the repository (where the pom.xml file resides): mvn package

If all goes well, this will create a .jar file in a subfolder named "target". 


Execution

Execute the jar package: java -jar ./target/<your jar file>.jar


Usage

Send any message to the bot and it will tell you what google thinks about its "sentiment" and "magnitude" (sentiment analysis).

Send the keyword "entity" to the bot to switch to entity analysis mode. All non keyword messages after that will be answered with the results of the google entity analysis.

Send the keyword "entities-sentiment" to the bot to switch to entities sentiment analysis mode.

Switch back to sentiment analysis mode by sending the keyword "sentiment".
