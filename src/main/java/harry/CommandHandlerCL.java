package harry;

public class CommandHandlerCL extends CommandHandler {

   public String displayHelp()
  {
    String helpMessage = "";

    helpMessage += "This program reads twitter feeds of specified users, analyzes them using the Google Natural Language API (https://cloud.google.com/natural-language/) and stores the result in a database. You can use the following commands to interact with it:\n";
    helpMessage += "\nlist users\nDisplays all users that are available in the database.\n";
    helpMessage += "\nreport <user>\nDisplay an nlp report on a specific user.\n";
    helpMessage += "\nall reports\nDisplay all available user reports.\n";
    helpMessage += "\n\nIn addition you can test some functions of the Google Language API directly. By default the program will provide the sentiment analysis result for messages you send to it. Use the following commands to switch to a different mode:\n";
    helpMessage += "\nentity sentiment\nUse this command to switch to entity sentiment analysis mode. The program will display the results of the google entity sentiment analysis in reply to your text messages from now on.\n";
    helpMessage += "\nentity\nSwitch to entity analysis mode. The bot will display the results of the google entity analysis.\n";
    helpMessage += "\nsentiment\nSwitch back to sentiment analysis (the default mode).\n";

    return helpMessage;
  }
}