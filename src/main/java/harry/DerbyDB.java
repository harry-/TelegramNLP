package harry;
 
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.time.LocalDate;

public class DerbyDB{

  String databaseName="data/nlpdb";
  String dbUrl = "jdbc:derby:"+databaseName;
 
  public Connection connectionToDerby() {
    Connection conn = null;
    try {      
      conn = DriverManager.getConnection(dbUrl);       
    } catch (SQLException sqle) {
      sqle.printStackTrace();
    } 
    return conn;
  }

  public void checkDB() throws SQLException {
    Connection conn = null;
    System.out.println("Check database");
    try {      
      conn = DriverManager.getConnection(dbUrl);       
    } catch (SQLException sqle) {
      if(sqle.getSQLState().equals("XJ004")) {
        System.out.println("Database not found, try to create...");
        createDB();
        System.out.println("Done");
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

      System.out.println(sql);

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

      System.out.println(sql);

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
  public String[] getEntitySentimentData(String sort, int top) {

    String[] output = new String[top];

    try {
      Connection conn = connectionToDerby();
      Statement stmt = conn.createStatement();
   
      ResultSet rs = stmt.executeQuery("SELECT entity FROM entitysentiment ORDER BY (score*10)*(magnitude*10)*(salience*100) "+sort+" FETCH NEXT "+top+" ROWS ONLY");
      //SELECT  entity, (score*100)*(magnitude*100), (score*100)*(magnitude*100)*(salience*100) as SxMxS FROM entitysentiment ORDER BY (score*10)*(magnitude*10) ASC FETCH NEXT 3 ROWS ONLY
     
   
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
    createTable("CREATE TABLE telegramuser (userid int, firstname varchar(40), secondname varchar(40), handle varchar(40))");
    createTable("create table sentiment (index INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), userid int, magnitude double, score double, date date)");
    createTable("create table entitysentiment (index INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), userid int, entity varchar(100), salience double, magnitude double, score double, date date, metadata varchar(100), type varchar(40))");
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
}