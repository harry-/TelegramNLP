package harry;

import harry.DerbyDB;
import harry.Listener;

import java.sql.SQLException;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import twitter4j.*;

import static java.lang.Math.toIntExact;
import java.util.List;

public class NLPTwitter {

  @Parameter(names = "-user", description = "twitter @ handle (without the @)")
  private String user;

  public static void main(String... args) throws Exception {

  	NLPTwitter nlpTwitter = new NLPTwitter();
    JCommander.newBuilder()
    	.addObject(nlpTwitter)
        .build()
        .parse(args);
  
    //Initialize the database
    DerbyDB db = new DerbyDB();
    db.checkDB();
   
	  nlpTwitter.report();
  }

  public void report() {
    // The factory instance is re-useable and thread safe.
    Twitter twitter = TwitterFactory.getSingleton();

    System.out.println(user);

    try {
      User tuser = twitter.showUser(user);
      Listener.checkUser(toIntExact(tuser.getId()),tuser.getScreenName(), tuser.getName());

      Paging p = new Paging();
      p.setCount(10);
      List<Status> statuses = twitter.getUserTimeline(tuser.getId(), p);
      System.out.println("Showing home timeline.");
      for (Status status : statuses) {
        System.out.println(status.getUser().getName() + ":" +
                             status.getText());
        Listener.theCloudListensToSentiments(toIntExact(tuser.getId()), status.getText());
        Listener.theCloudListens(toIntExact(tuser.getId()), status.getText());
        
      }

      System.out.println(tuser.getOriginalProfileImageURL());
      System.out.println(tuser.getName());
      System.out.println(tuser.getScreenName());
    } catch (twitter4j.TwitterException e) {
      System.out.println(e.getMessage());
    } catch (ArithmeticException e) {
      System.out.println("The twitter id is too LONG for the db int field");
      System.out.println(e.getMessage());
    }
  }
}