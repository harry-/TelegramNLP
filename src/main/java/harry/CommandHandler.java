package harry;

public class CommandHandler {
	public static String all(String command, StringBuilder mode) {
	  
    if (command.equals("entity")) 
      mode="entity";
    
    else if (command.equals("sentiment")) 
      mode="sentiment";

    else if (command.equals("entity sentiment")) 
      mode="entities-sentiment";

    else if (command.equals("hello"))
      return("hello " + update.getMessage().getFrom().getUserName());

    else if (command.equals("list users"))
      return(Report.userList());

    else if (command.equals("all reports"))
      return(Report.allReports());

    else if (command.equals("help"))
      return(displayHelp());

    else if (command.startsWith("set gender")) {
      String[] splitted = command.split(" ");
      try {
        db.setGender(splitted[2], splitted[3]);
        ("alright then");
      } catch (IllegalArgumentException e) {
        return(e.getMessage());
      }
    }

    else if (command.startsWith("report")) {

      String[] splitted = command.split(" ");
        return(Report.report(splitted[1]));
    }

    else if (command.startsWith("add twitter user")) {

      String[] splitted = command.split(" ");
        return(TwitterNLP.addTweetsToDB(splitted[3]));
    }


    // switch dependent commands that produce a reply (only in private chat)
    else if (update.getMessage().getChat().isUserChat()){ 
      GoogleCloud cloud = new GoogleCloud();

      if (mode.equals("sentiment"))
         return(cloud.getSent(command));

      else if (mode.equals("entity"))
        return(cloud.getEnt(command));

      else if (mode.equals("entities-sentiment"))
        return(cloud.getEntSent(command));
    } else {
      Listener.theCloudListens(update.getMessage().getFrom().getId(), 
        command);
      Listener.theCloudListensToSentiments(update.getMessage().getFrom().getId(), 
        command);
      Listener.checkUser(update.getMessage().getFrom().getId(), 
        update.getMessage().getFrom().getUserName(), 
        update.getMessage().getFrom().getFirstName());

    }

	}
}