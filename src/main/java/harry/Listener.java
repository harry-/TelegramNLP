package harry;

import com.google.cloud.language.v1.Entity;
import com.google.cloud.language.v1.AnalyzeEntitySentimentResponse;
import com.google.cloud.language.v1.Sentiment;
import com.google.cloud.language.v1.AnalyzeSentimentResponse;

import harry.DerbyDB;
import harry.GoogleCloud;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class Listener {

  private static Logger logger = LogManager.getLogger(); 

  public static void theCloudListens(long userID, String text, long messageID) {

    logger.entry(userID, text);

    GoogleCloud cloud = new GoogleCloud();
    DerbyDB db = new DerbyDB();

    LocalDateTime currentTime = LocalDateTime.now();
    LocalDate date = currentTime.toLocalDate();
    logger.info(date+": "+text);

    List<Entity> entities = cloud.getEntSent2(text);

    for (Entity entity : entities) {
      String answer = "Entity: " + entity.getName();
      answer += ", Salience: " + entity.getSalience();
      answer += ", Score: " + entity.getSentiment().getScore();
      answer += ", Magnitude: " + entity.getSentiment().getMagnitude();
      answer += ", Type: " + entity.getType().name();

      db.storeAnalysis(userID,
        entity.getName(),
        entity.getSalience(), 
        entity.getSentiment().getMagnitude(), 
        entity.getSentiment().getScore(), 
        date, "",
        entity.getType().name(),
        messageID
        );
      logger.exit(answer);
    }
  }
  public static void theCloudListensToSentiments(long userID, String text, LocalDate date, long messageID) {

    GoogleCloud cloud = new GoogleCloud();
    DerbyDB db = new DerbyDB();

    Sentiment sentiment = cloud.getSentimentObject(text);

    String answer = "Score: " + sentiment.getScore();
    answer += ", Magnitude: " + sentiment.getMagnitude();

    logger.debug(answer);

    db.storeSentiment(userID,
      sentiment.getMagnitude(), 
      sentiment.getScore(), 
      date,
      messageID
      );
  }

  public static String checkUser(long userid, String username, String firstname) {
    DerbyDB db = new DerbyDB();
    if (db.getUsername(userid) == null) {
      db.createUser(userid, username, firstname);
      return "created";
    }
    return "exists";
  }
}