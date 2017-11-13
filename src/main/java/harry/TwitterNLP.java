package harry;

import twitter4j.*;

import java.util.List;
import static java.lang.Math.toIntExact;


public class TwitterNLP {

  public static String addTweetsToDB(String user) {
    
    Twitter twitter = TwitterFactory.getSingleton();
    String output = "";

    try {
      User tuser = twitter.showUser(user);
      String check = Listener.checkUser(toIntExact(tuser.getId()),tuser.getScreenName(), tuser.getName());

      if (check.equals("exists"))
        return "User has been added previously.";

      Paging p = new Paging();
      p.setCount(800);
      List<Status> statuses = twitter.getUserTimeline(tuser.getId(), p);
      System.out.println("Showing home timeline.");
      for (Status status : statuses) {
        System.out.println(status.getUser().getName() + ":" +
                             status.getText());
        Listener.theCloudListensToSentiments(toIntExact(tuser.getId()), status.getText());
        Listener.theCloudListens(toIntExact(tuser.getId()), status.getText());     
      }

      output += statuses.size() + " of "+tuser.getScreenName()+"'s tweets have been analyzed.";

      System.out.println(tuser.getOriginalProfileImageURL());
      System.out.println(tuser.getName());
      System.out.println(tuser.getScreenName());
    } catch (twitter4j.TwitterException e) {
      System.out.println(e.getMessage());
    } catch (ArithmeticException e) {
      System.out.println("The twitter id is too LONG for the db int field");
      System.out.println(e.getMessage());
    }
    return output;
  }
}