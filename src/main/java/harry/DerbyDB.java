package harry;
 
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.time.LocalDate;
 
public class DerbyDB{
  Connection conn;
 
  public void connectionToDerby() throws SQLException {
    // -------------------------------------------
    // URL format is
    // jdbc:derby:<local directory to save data>
    // -------------------------------------------
    String dbUrl = "jdbc:derby:data/nlpdb;create=true";
    conn = DriverManager.getConnection(dbUrl);
  }

  public void storeAnalysis(int userid, String entity, double salience, double magnitude, double score, LocalDate date, String metadata) throws SQLException {

    String dbUrl = "jdbc:derby:data/nlpdb;create=true";
    conn = DriverManager.getConnection(dbUrl);

    try {
      Statement stmt = conn.createStatement();
   
      // drop table
      // stmt.executeUpdate("Drop Table entitysentiment");
   
      // create table
      //stmt.executeUpdate("create table entitysentiment (index INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), userid int, entity varchar(100), salience double, magnitude double, score double, date date, metadata varchar(100))");
   
      String sql = "insert into entitysentiment (userid, entity, salience, magnitude, score, date, metadata) values ("+userid+", '"+entity+"', "+salience+", "+magnitude+", "+score+", '"+date+"', '"+metadata+"')";

      System.out.println(sql);

      stmt.executeUpdate(sql);

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void storeSentiment(int userid, double magnitude, double score, LocalDate date) throws SQLException {

    String dbUrl = "jdbc:derby:data/nlpdb;create=true";
    conn = DriverManager.getConnection(dbUrl);

    try {
      Statement stmt = conn.createStatement();
   
      // drop table
      // stmt.executeUpdate("Drop Table sentiment");

      //stmt.executeUpdate("create table sentiment (index INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), userid int, magnitude double, score double, date date)");

      //stmt.executeUpdate("CREATE TABLE telegramuser (userid int, firstname varchar(40), secondname varchar(40), handle varchar(40))");
   
      String sql = "insert into sentiment (userid, magnitude, score, date) values ("+userid+", "+magnitude+", "+score+", '"+date+"')";

      System.out.println(sql);

      stmt.executeUpdate(sql);

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public String getEntitySentimentData(String sort) throws SQLException {

    String output = "";
    String dbUrl = "jdbc:derby:data/nlpdb;create=true";
    conn = DriverManager.getConnection(dbUrl);

    try {
      Statement stmt = conn.createStatement();
   
      ResultSet rs = stmt.executeQuery("SELECT  entity FROM entitysentiment ORDER BY (score*10)*(magnitude*10) "+sort);
      rs.next();
      output = rs.getString("entity");
      /*while (rs.next()) { 
        output += rs.getString("entity")+"\n";
        }  */    
    } catch (Exception e) {
      e.printStackTrace();
    }
    return output;
  }

/**
 * Retrieves the username corresponding to a given userid.
 * Returns null if there is no entry.
 */
  public String getUsername(int userid) {
    String dbUrl = "jdbc:derby:data/nlpdb;create=true";
   
    try {
      conn = DriverManager.getConnection(dbUrl);
      Statement stmt = conn.createStatement();

      ResultSet rs = stmt.executeQuery("SELECT handle FROM telegramuser WHERE userid = "+userid);

      if (rs.next()) 
        return rs.getString("handle");
          
   
      //stmt.executeUpdate("CREATE TABLE telegramuser (userid int, firstname varchar(40), secondname varchar(40), handle varchar(40))");

    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

/**
 * Creates a new User
 */
  public void createUser(int userid, String username, String firstname) {
    String dbUrl = "jdbc:derby:data/nlpdb;create=true";
   
    try {
      conn = DriverManager.getConnection(dbUrl);
      Statement stmt = conn.createStatement();

      String sql = "insert into telegramuser (userid, handle, firstname) values ("+userid+", '"+username+"', '"+firstname+"')";

      System.out.println(sql);

      stmt.executeUpdate(sql);

    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}