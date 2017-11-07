package harry;

import harry.DerbyDB;

public class NLPcl {
  public static void main(String... args) throws Exception {
  
    //Initialize the database
  	//DerbyDB db = new DerbyDB();
  	DerbyDB db = new DerbyDB();
  	db.initializeTables();

	Report report = new Report();
    System.out.println(report.entitySentiment());
  }
}