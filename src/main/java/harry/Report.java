package harry;

import java.sql.SQLException;
import harry.DerbyDB;

public class Report {
	public String entitySentiment() {
		String report = "This will be the report.";
		String[] fav = new String[3];
		String[] worst  = new String[3];
		DerbyDB db = new DerbyDB();
		try {
			worst = db.getEntitySentimentData("ASC", 3);
			fav = db.getEntitySentimentData("DESC", 3);
		} catch (SQLException sqle) {
      System.out.println(sqle.toString());
    }

    report = "You like "+fav[0]+", "+fav[1]+" and "+fav[2]+". You dislike "+worst[0]+", "+worst[1]+" and "+worst[2]+".";

		return report;
	}
}