package harry;

import harry.DerbyDB;
import java.sql.SQLException;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.misc.TransactionManager;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.SelectArg;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

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

     // this uses h2 by default but change to match your database
    String databaseUrl = "jdbc:derby:data/nlpdb";
    // create a connection source to our database
    ConnectionSource connectionSource =
        new JdbcConnectionSource(databaseUrl);

    // instantiate the dao
    Dao<Telegramuser, String> TelegramuserDao = OrmLite.getTelegramUserDao();

    //Dao<Telegramuser, String> TelegramuserDao =
    //    DaoManager.createDao(connectionSource, Telegramuser.class);

    // if you need to create the 'Telegramusers' table make this call
    //TableUtils.createTable(connectionSource, Telegramuser.class);

  
    // create an instance of Telegramuser
    //Telegramuser telegramuser = new Telegramuser(333,"harry", "ichselbst");
    //telegramuser.setUserid(123);


    // persist the Telegramuser object to the database
    //TelegramuserDao.create(telegramuser);

    // retrieve the Telegramuser from the database by its id field (name)
    Telegramuser telegramuser2 = TelegramuserDao.queryForId("333");
    System.out.println("Telegramuser: " + telegramuser2.getHandle());

    // close the connection source
    connectionSource.close();
  }

  public void report() {

  	Report report = new Report();
	System.out.println(report.entitySentiment(user));
  }
}