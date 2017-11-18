package harry;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import harry.ListUsers;


public class CommandHandler {


  private DerbyDB db = new DerbyDB();
  private static Logger logger = LogManager.getLogger(); 

	public String all(String command, StringBuilder mode, String user, Boolean privateChat, int id, String firstName) {
	  logger.entry(command, mode, user, privateChat);
    String output = "";

    if (command.matches("entity|sentiment|entities-sentiment")) {
      mode.delete(0, mode.length()); 
      mode.append(command);
      logger.debug("mode set to "+mode.toString());
    }

    else if (command.equals("hello"))
      output = "hello " + user;

    else if (command.startsWith("list users"))
      output = new ListUsers(command).run();

    else if (command.equals("all reports"))
      output = Report.allReports();

    else if (command.equals("help"))
      output = displayHelp();

    else if (command.startsWith("set gender")) {
      String[] splitted = command.split(" ");
      try {
        db.setGender(splitted[2], splitted[3]);
        output = "alright then";
      } catch (IllegalArgumentException e) {
        output = e.getMessage();
      }
    }

    else if (command.startsWith("report")) {
      String[] splitted = command.split(" ");
      output = Report.report(splitted[1]);
    }

    else if (command.startsWith("add twitter user")) {
      String[] splitted = command.split(" ");
      output = TwitterNLP.addTweetsToDB(splitted[3]);
    }

    // switch dependent commands that produce a reply (only in private chat)
    else if (privateChat) { 
      GoogleCloud cloud = new GoogleCloud();

      if (mode.toString().equals("sentiment")) {
        try {
          output = cloud.getSent(command);
        } catch (Exception e) {
          logger.error("A previously unencountered google cloud error: "+e.getMessage());
          logger.error(e.getStackTrace());
        } 
      }

      else if (mode.toString().equals("entity"))  {
        try {
          output = cloud.getEnt(command);
        } catch (Exception e) {
          logger.error("A previously unencountered google cloud error: "+e.getMessage());
          logger.error(e.getStackTrace());
        } 
      }

      else if (mode.toString().equals("entities-sentiment"))
        output = cloud.getEntSent(command);
    } else {
      Listener.theCloudListens(id, command);
      Listener.theCloudListensToSentiments(id, command);
      Listener.checkUser(id, user, firstName);
    }
    return logger.exit(output);
	}

  public String displayHelp()
  {
    logger.entry();
    String helpMessage = "";

    helpMessage += "This bot listens to all messages in channels that it has been added to, analyzes them using the Google Natural Language API (https://cloud.google.com/natural-language/) and stores the result in a database. You can use the following commands to interact with it:\n";
    helpMessage += "\nlist users\nDisplays all users that are available in the database.\n";
    helpMessage += "\nreport <user>\nDisplay an nlp report on a specific user.\n";
    helpMessage += "\nall reports\nDisplay all available user reports\n";
    helpMessage += "\n\nIn addition you can also use the bot as a basic interface to some functions of the Google Language API. By default the bot will provide the sentiment analysis result for messages you send to it. Use the following commands to switch to a different mode:\n";
    helpMessage += "\nentity sentiment\nUse this command to switch to entity sentiment analysis mode. The bot will display the results of the google entity sentiment analysis in reply to your text messages from now on.\n";
    helpMessage += "\nentity\nSwitch to entity analysis mode. The bot will display the results of the google entity analysis.\n";
    helpMessage += "\nsentiment\nSwitch back to sentiment analysis (the default mode)\n";

    return logger.exit(helpMessage);
  }

}