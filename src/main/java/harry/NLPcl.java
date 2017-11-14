package harry;

import harry.DerbyDB;
import harry.NLPBot;
import java.sql.SQLException;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NLPcl {

  Logger logger = LogManager.getLogger();

  @Parameter(names = "-user", description = "telegram @ handle (without the @)")
  private String user;
  @Parameter(names = "-command", description = "same as the botcommands maybe at some point")
  private String command;

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

  }  
}
/*
          if (update.getMessage().getText().equals("hello"))
            message.setText("hello " + update.getMessage().getFrom().getUserName());

          else if (update.getMessage().getText().equals("list users"))
            message.setText(Report.userList());

          else if (update.getMessage().getText().equals("all reports"))
            message.setText(Report.allReports());

          else if (update.getMessage().getText().equals("help"))
            message.setText(displayHelp());

          else if (update.getMessage().getText().startsWith("set gender")) {
            String[] splitted = update.getMessage().getText().split(" ");
            try {
              db.setGender(splitted[2], splitted[3]);
              message.setText("alright then");
            } catch (IllegalArgumentException e) {
              message.setText(e.getMessage());
            }
            */