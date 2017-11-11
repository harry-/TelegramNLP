package harry;

import harry.DerbyDB;
import java.sql.SQLException;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

public class NLPcl {

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
    
	  nlpcl.report();

  }

  public void report() {
  	Report report = new Report();
    System.out.println(report.report(user));
    System.out.println(report.userList());
    System.out.println(report.allReports());
  }
}