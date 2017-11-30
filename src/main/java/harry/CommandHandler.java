package harry;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.time.LocalDate;
import java.time.LocalDateTime;


import harry.commands.*;


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

    else if (command.startsWith("all reports"))
      output = new AllReports(command).run();

    else if (command.equals("help"))
      output = displayHelp();

    else if (command.startsWith("set gender")) 
      output = new SetGender(command).run();

    else if (command.startsWith("report")) 
      output = new Report(command).run();

    else if (command.startsWith("add twitter user")) 
      output = new AddTwitterUser(command).run();

    else if (command.startsWith("chart")) 
      output = new MoodChart(command).run();    

    else if (command.startsWith("category report")) 
      output = new CategoryReport(command).run();    

    else if (command.equals("get types"))
      output = db.getTypes().toString();
  

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

      LocalDateTime currentTime = LocalDateTime.now();
      LocalDate date = currentTime.toLocalDate();


      Listener.theCloudListens(id, command, 0);
      Listener.theCloudListensToSentiments(id, command, date, 0);
      Listener.checkUser(id, user, firstName);
    }
    return logger.exit(output);
	}

  public String displayHelp()
  {
    logger.entry();
    String helpMessage = "";

    helpMessage += "\n\nThis bot listens to all messages in channels that it has been added to, analyzes them using the Google Natural Language API (https://cloud.google.com/natural-language/) and stores the result in a database. You can use the following commands to interact with it:\n";
    helpMessage += "\nlist users";
    helpMessage += "\nreport [user]";
    helpMessage += "\nall reports";
    helpMessage += "\nadd twitter user [twitter handle]";
    helpMessage += "\nchart [user]";
    helpMessage += "\n\nType [command] -help to display more information about a specific command.\nExamples:\tadd twitter user -help\n\t\tall reports -help";
    helpMessage += "\n\nIn addition you can also use the bot as a basic interface to some functions of the Google Language API. By default the bot will provide the sentiment analysis result for messages you send to it. Use the following commands to switch to a different mode:\n";
    helpMessage += "\nentity sentiment\nUse this command to switch to entity sentiment analysis mode. The bot will display the results of the google entity sentiment analysis in reply to your text messages from now on.\n";
    helpMessage += "\nentity\nSwitch to entity analysis mode. The bot will display the results of the google entity analysis.\n";
    helpMessage += "\nsentiment\nSwitch back to sentiment analysis (the default mode)\n";

    return logger.exit(helpMessage);
  }

}