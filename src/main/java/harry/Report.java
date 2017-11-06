package harry;

import java.sql.SQLException;
import harry.DerbyDB;

public class Report {
	public String entitySentiment() {
		String report = "This will be the report.";
		String fav = "";
		String worst = "";
		DerbyDB db = new DerbyDB();
		try {
			worst = db.getEntitySentimentData("ASC");
			fav = db.getEntitySentimentData("DESC");
		} catch (SQLException sqle) {
      System.out.println(sqle.toString());
    }

    report = "You like "+fav+". You dislike "+worst+".";

		return report;
	}
}