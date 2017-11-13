package harry;

import harry.DerbyDB;
import java.sql.SQLException;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import java.util.List;
import twitter4j.*;

public class NLPTwitter {

  @Parameter(names = "-user", description = "telegram @ handle (without the @)")
  private String user;

  public static void main(String... args) throws Exception {

  	NLPcl nlpcl = new NLPcl();
    JCommander.newBuilder()
    	.addObject(nlpcl)
        .build()
        .parse(args);
  
    //Initialize the database
    DerbyDB db = new DerbyDB();
    db.checkDB();
    
	  //nlpcl.report();

    // The factory instance is re-useable and thread safe.
    Twitter twitter = TwitterFactory.getSingleton();
    Paging p = new Paging();
    p.setCount(800);
    List<Status> statuses = twitter.getUserTimeline(42112455, p);
    System.out.println("Showing home timeline.");
    for (Status status : statuses) {
      System.out.println(status.getUser().getName() + ":" +
                           status.getText());
    }

  }

  public void report() {
  	Report report = new Report();
    System.out.println(report.report(user));
    System.out.println(report.userList());
    System.out.println(report.allReports());
  }
}