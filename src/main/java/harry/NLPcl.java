package harry;

import harry.DerbyDB;
import harry.NLPBot;
import java.sql.SQLException;
import java.io.IOException;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.Scanner;

public class NLPcl {

  Logger logger = LogManager.getLogger();
  private StringBuilder mode = new StringBuilder("sentiment"); 

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
  }

  public void commands() {
    System.out.println();
    if (command.equals("none")) 
      command = "hello";

    Scanner scanner=new Scanner(System.in); 
    while (true) {
      if(command.equals(""))
        break;

      String output = new CommandHandlerCL().all( 
                command, 
                mode, 
                "harry",
                true, 
                1, 
                "it me");
      System.out.println(output);
      command = scanner.nextLine();
    }
  }  
}