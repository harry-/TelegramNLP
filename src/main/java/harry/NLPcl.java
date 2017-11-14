package harry;

import harry.DerbyDB;
import harry.NLPBot;
import java.sql.SQLException;
import java.io.IOException;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class NLPcl {

  Logger logger = LogManager.getLogger();

  @Parameter(names = "-user", description = "telegram @ handle (without the @)")
  private String user;
  @Parameter(names = "-command", description = "same as the botcommands maybe at some point")
  private String command = "none";

  public static void main(String... args) throws Exception {

  	NLPcl nlpcl = new NLPcl();
    JCommander.newBuilder()
    	.addObject(nlpcl)
        .build()
        .parse(args);
  
    //Initialize the database
    DerbyDB db = new DerbyDB();
    db.checkDB();

    nlpcl.commands();
    
   // nlpcl.report();

  }

  public void report() {
  	Report report = new Report();
    System.out.println(report.report(user));
    System.out.println(report.userList());
    System.out.println(report.allReports());
  }

  public void commands() {
    switch (command) {
      case "hello":
        System.out.println("hello");
        break;
      case "list users":
        System.out.println(Report.userList());
        break;
      case  "all reports":
        System.out.println(Report.allReports());
        break;
      case  "help":
        System.out.println(NLPBot.displayHelp());
    }
    if (command.startsWith("set gender")) {
      String[] splitted = command.split(" ");
      try {
        DerbyDB db = new DerbyDB();
        db.setGender(splitted[2], splitted[3]);
        System.out.println("alright then");
      } catch (IllegalArgumentException e) {
        logger.error(e.getMessage());
      }
    }
    else if (command.startsWith("report")) {
      String[] splitted = command.split(" ");
      System.out.println(Report.report(splitted[1]));
    }
    else if (command.startsWith("add twitter user")) {
      String[] splitted = command.split(" ");
      System.out.println(TwitterNLP.addTweetsToDB(splitted[3]));
    }
    System.out.println();
    try {
      System.in.read();
    } catch (IOException e) {
      logger.error(e.getMessage());
      logger.error(e.getStackTrace());
    }
  }  
}