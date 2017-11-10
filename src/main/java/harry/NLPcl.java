package harry;

import harry.DerbyDB;
import java.sql.SQLException;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

public class NLPcl {

  @Parameter(names = "-user", description = "telegram @ handle (without the @)")
  private String user;

  public static void main(String... args) {

  	NLPcl nlpcl = new NLPcl();
    JCommander.newBuilder()
    	.addObject(nlpcl)
        .build()
        .parse(args);
  
    //Initialize the database
  	DerbyDB db = new DerbyDB();
  	try {
  		db.checkDB();
  		db.initializeTables();
  	} catch (SQLException sqle) {
  		sqle.printStackTrace();
  	}

	nlpcl.report();
  }

  public void report() {

  	Report report = new Report();
	System.out.println(report.entitySentiment(user));
  }
}