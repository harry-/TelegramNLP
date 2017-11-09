package harry;

import harry.DerbyDB;
import java.sql.SQLException;

public class NLPcl {
  public static void main(String... args) {
  
    //Initialize the database
  	//DerbyDB db = new DerbyDB();
  	DerbyDB db = new DerbyDB();
  	try {
  		db.checkDB();
  		db.initializeTables();
  	} catch (SQLException sqle) {
  		sqle.printStackTrace();
  	}

	Report report = new Report();
    System.out.println(report.entitySentiment("jolieblonde"));
  }
}