package harry;
 
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.time.LocalDate;

public class DerbyDB{

  private static Logger logger = LogManager.getLogger(); 

  String databaseName="data/nlpdb";
  String dbUrl = "jdbc:derby:"+databaseName;
 
  private Connection connectionToDerby() {
    Connection conn = null;
    try {      
      conn = DriverManager.getConnection(dbUrl);       
    } catch (SQLException sqle) {
      sqle.printStackTrace();
    } 
    return conn;
  }

  // this is just to catch failure to create the db
  public void checkDB()
  {
    try {
      initializeDB();
    } catch (SQLException sqle) {
      sqle.printStackTrace();
    }

  }

  
/**
 * Check if the DB is there
 * If it is not, try to create it.
 */
  public void initializeDB() throws SQLException {
    Connection conn = null;
    try {      
      conn = DriverManager.getConnection(dbUrl); 
      logger.info("Database found");      
    } catch (SQLException sqle) {
      if(sqle.getSQLState().equals("XJ004")) {
        logger.info("Database not found, trying to create...");
        createDB();
        initializeTables();
        logger.info("Done");
      }
    } 
  }

  public void createDB() throws SQLException {
    Connection conn = null;     
    conn = DriverManager.getConnection(dbUrl+";create=true");  
    conn.close();    
  }


  public void storeAnalysis(int userid, String entity, double salience, double magnitude, double score, LocalDate date, String metadata, String type) {
    try {
      Connection conn = connectionToDerby();

      Statement stmt = conn.createStatement();
   
      String sql = "insert into entitysentiment (userid, entity, salience, magnitude, score, date, metadata, type) values ("+userid+", '"+entity+"', "+salience+", "+magnitude+", "+score+", '"+date+"', '"+metadata+"', '"+type+"')";
      logger.debug(sql);

      stmt.executeUpdate(sql);

      stmt.close();
      conn.close();   

    } catch (SQLException e) {
      e.printStackTrace();
    }
  }


  public void storeSentiment(int userid, double magnitude, double score, LocalDate date) {
    try {
      Connection conn = connectionToDerby();
      Statement stmt = conn.createStatement();

      String sql = "insert into sentiment (userid, magnitude, score, date) values ("+userid+", "+magnitude+", "+score+", '"+date+"')";

      logger.debug(sql);

      stmt.executeUpdate(sql);
      stmt.close();
      conn.close(); 
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

/**
 * Retrieve top rated entries from entitysentiment
 * Returns null if there is no entry.
 *
 * @param store   either ASC or DESC
 * @param top     get the first [top] results of the query 
 * @return entity names as an array of strings
 */

  public String[] getEntitySentimentData(String sort, int top, String user, String category) throws IllegalArgumentException {

    String[] output = new String[top];

    try {
      Connection conn = connectionToDerby();
      Statement stmt = conn.createStatement();

      int userid = getUserId(user);

      String sql = "SELECT entity FROM entitysentiment WHERE userid = "+userid;

      if (!category.equals("ALL"))
        sql += " AND type = '"+category+"'";

      sql += " ORDER BY (score*10)*(magnitude*10)*(salience*100) "+sort+" FETCH NEXT "+top+" ROWS ONLY";
      
      logger.debug(sql);
      ResultSet rs = stmt.executeQuery(sql);
  
      int idx = 0;
      while (rs.next()) { 
        output[idx] = rs.getString("entity");
        idx++;
        }     
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return output;
  }

/**
 * Retrieve the entity most often mentioned by a given user
 * Returns null if there is no entry.
 *
 * @param handle   user handle
 * @return entity name as a string
 */

  public String getFavWord(String handle) throws IllegalArgumentException {

    String output = null;

    try {
      Connection conn = connectionToDerby();
      Statement stmt = conn.createStatement();

      int userid = getUserId(handle);

      String sql = "SELECT entity, COUNT(*) AS num FROM entitysentiment WHERE userid = "+userid+" GROUP BY entity ORDER BY NUM DESC FETCH FIRST ROW ONLY";
   
      ResultSet rs = stmt.executeQuery(sql);
      if(rs.next());
        output = rs.getString("entity");

    } catch (SQLException e) {
      e.printStackTrace();
    }
    return output;
  }

/**
 * Retrieve the username corresponding to a given userid.
 * Returns null if there is no entry.
 */
  public String getUsername(int userid) {

    try {
      Connection conn = connectionToDerby();

      Statement stmt = conn.createStatement();

      ResultSet rs = stmt.executeQuery("SELECT handle FROM telegramuser WHERE userid = "+userid);

      if (rs.next()) 
        return rs.getString("handle");

    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

/**
 * Retrieve the userid corresponding to a given handle.
 * Returns 0 if there is no entry.
 */
  public int getUserId(String userhandle) {

    try {
      Connection conn = connectionToDerby();

      Statement stmt = conn.createStatement();
      String sql = "SELECT userid FROM telegramuser WHERE handle = '"+userhandle+"'";
      logger.debug(sql);

      ResultSet rs = stmt.executeQuery(sql);

      if (rs.next()) 
        return rs.getInt("userid");

    } catch (SQLException e) {
      e.printStackTrace();
    }

    if (userhandle == null) {
      logger.error("No userhandle provided");
      throw new IllegalArgumentException( "No userhandle provided.");
    }

    logger.error("User "+userhandle+" does not exist in database.");
    throw new IllegalArgumentException( "User "+userhandle+" does not exist in database.");
  }

/**
 * Set the gender of a user
 * 
 */
  public void setGender(String userhandle, String gender) {

    try {
      Connection conn = connectionToDerby();

      Statement stmt = conn.createStatement();
      String sql = "UPDATE telegramuser SET gender = '"+gender+"' WHERE handle='"+userhandle+"'";

      logger.debug(sql);

      stmt.executeUpdate(sql);
      stmt.close();
      conn.close(); 
      return;

    } catch (SQLException e) {
      e.printStackTrace();
    }
    throw new IllegalArgumentException( "User "+userhandle+" does not exist in database");
  }

/**
 * Create a new User
 */
  public void createUser(int userid, String username, String firstname) {
   
    try {
      Connection conn = connectionToDerby();
      Statement stmt = conn.createStatement();

      String sql = "insert into telegramuser (userid, handle, firstname) values ("+userid+", '"+username+"', '"+firstname+"')";

      System.out.println(sql);

      stmt.executeUpdate(sql);
      stmt.close();
      conn.close(); 

    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

/**
 * Create db structure on first run
 */
  public void initializeTables() {
    createTable("CREATE TABLE Telegramuser (Userid int, Firstname varchar(40), Secondname varchar(40), Handle varchar(40), Gender varchar(10))");
    createTable("create table sentiment (index INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), userid int, magnitude double, score double, date date)");
    createTable("create table entitysentiment (index INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), userid int, entity varchar(100), salience double, magnitude double, score double, date date, metadata varchar(100), type varchar(40))");
    logger.info("Tables created");
  }

/**
 * Check if SQLException was "Table doesnt exist"
 */
  public boolean tableAlreadyExists(SQLException e) {
    boolean exists;
    if(e.getSQLState().equals("X0Y32")) {
        exists = true;
    } else {
        exists = false;
    }
    return exists;
  }

  /**
   * Create a new table, unless it already exists
   */
  public void createTable(String sql) {
   
    try {
       Connection conn = connectionToDerby();
     
      Statement stmt = conn.createStatement();
      stmt.executeUpdate(sql);
    } catch (SQLException e) {
      if(tableAlreadyExists(e)) { //check if the exception is because of pre-existing table.
        String[] words = sql.split(" ");

        System.out.println("Table "+words[2]+" already exists.  No need to recreate");
      } else {
        System.out.println(e.getMessage() + " : " + e.getStackTrace());
      }
    } finally {
        // the connection should be closed here
    }
  }

  /**
   * Retrieve average sentiment score
   *
   * @param store   either ASC or DESC
   * @param top     get the first [top] results of the query 
   * @return entity names as an array of strings
   */
  public Double getAverageSentiment(String handle) {

    Double output = 0.0;

    try {
      Connection conn = connectionToDerby();
      Statement stmt = conn.createStatement();

      int userid = getUserId(handle);

      String sql = "SELECT AVG(score) as average FROM sentiment WHERE userid = "+userid;
      logger.debug(sql);

      ResultSet rs = stmt.executeQuery(sql);
  
      rs.next();
      output = rs.getDouble("average");
   
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return output;

  }

}