package harry;

import com.google.cloud.language.v1.Entity;
import com.google.cloud.language.v1.AnalyzeEntitySentimentResponse;

import harry.DerbyDB;
import harry.GoogleCloud;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class Listener {

  public void theCloudListens(int userID, String text) {

    GoogleCloud cloud = new GoogleCloud();
    DerbyDB db = new DerbyDB();

    LocalDateTime currentTime = LocalDateTime.now();
    LocalDate date = currentTime.toLocalDate();

    try {
      List<Entity> entities = cloud.getEntSent2(text);

      for (Entity entity : entities) {
        String answer = "Entity: " + entity.getName();
        answer += ", Salience: " + entity.getSalience();
        answer += ", Score: " + entity.getSentiment().getScore();
        answer += ", Magnitude: " + entity.getSentiment().getMagnitude();
 
        System.out.println(answer);

        db.storeAnalysis(userID,
          entity.getName(),
          entity.getSalience(), 
          entity.getSentiment().getMagnitude(), 
          entity.getSentiment().getScore(), 
          date, "");
      }
    } catch (SQLException sqle) {
      System.out.println(sqle.toString());
    }  catch (Exception e) {
      System.out.println(e.toString());
    }
  }
}