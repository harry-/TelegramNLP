package harry;

import twitter4j.*;

import java.util.List;
import static java.lang.Math.toIntExact;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class AddTwitterUser extends Command {

	private static Logger logger = LogManager.getLogger();

	private String help = "Adds a twitter user to the database and analyzes the first 200 tweets in that user's timeline.\nExample: add twitter user realDonaldTrump";


	public AddTwitterUser(String command) {
		this.commandString = command;
		if (command.matches(".*help.*")) 
			helpFlag = true;
	}

 /**
	* Add twitter user to database
	* adds a new twitter user to the databe user table 
	* and analyses the 200 last tweets in that user's timeline
	*
	* @return 					success or failure message
	*/
	public String run() {
		if (this.helpFlag)
			return help;
		String output = "";

    String[] splitted = commandString.split(" ");
    String user = splitted[3];

  	Twitter twitter = TwitterFactory.getSingleton();

    try {
      User tuser = twitter.showUser(user);
      String check = Listener.checkUser(toIntExact(tuser.getId()),tuser.getScreenName(), tuser.getName());

      if (check.equals("exists"))
        return "User has been added previously.";

      logger.info("Analyzing "+tuser.getScreenName()+"'s tweets. This will take awhile");

      Paging p = new Paging();
      p.setCount(800);
      List<Status> statuses = twitter.getUserTimeline(tuser.getId(), p);
      logger.info("Showing home timeline.");
      for (Status status : statuses) {
        logger.debug(status.getUser().getName() + ":" +
                             status.getText());
        Listener.theCloudListensToSentiments(toIntExact(tuser.getId()), status.getText());
        Listener.theCloudListens(toIntExact(tuser.getId()), status.getText());     
      }

      output += statuses.size() + " of "+tuser.getScreenName()+"'s tweets have been analyzed.";

      //System.out.println(tuser.getOriginalProfileImageURL());
      logger.info(tuser.getName());
      logger.info(tuser.getScreenName());
    } catch (twitter4j.TwitterException e) {
      logger.error(e.getMessage());
    } catch (ArithmeticException e) {
      logger.error("The twitter id is too LONG for the db int field");
      logger.error(e.getMessage());
    }
    return output;
  }
}