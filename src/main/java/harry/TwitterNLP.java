package harry;

import twitter4j.*;

import java.util.List;
import static java.lang.Math.toIntExact;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;


public class TwitterNLP {

  private static Logger logger = LogManager.getLogger();

  public static String addTweetsToDB(String user) {
    
    Twitter twitter = TwitterFactory.getSingleton();
    String output = "";

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