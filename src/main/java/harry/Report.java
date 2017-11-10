package harry;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import harry.DerbyDB;

public class Report {

	/**
	 * Create a report
	 *
	 * @param user	userhandle
	 * @return a verbose report
	 */
	public String entitySentiment(String user) {
		String report = "This will be the report.";
		String[] fav = new String[3];
		String[] worst  = new String[3];
		DerbyDB db = new DerbyDB();

		String gender = "";
		Dao<Telegramuser, String> userdao = OrmLite.getTelegramUserDao();
		for (Telegramuser tuser : userdao) {
    	if (tuser.getHandle().equals(user))
 				gender = tuser.getGender();
		}

		try {
			worst = db.getEntitySentimentData("ASC", 3, user);
			fav = db.getEntitySentimentData("DESC", 3, user);
		} catch (IllegalArgumentException e)
		{
			return e.getMessage();
		}

		String xdislike = "They dislike";
		if (gender != null ) {
			if (gender.equals("male"))
				xdislike = "He dislikes";
			else if (gender.equals("female"))
				xdislike = "She dislikes";
		}

    report = user +" likes "+fav[0]+", "+fav[1]+" and "+fav[2]+". "+xdislike+" "+worst[0]+", "+worst[1]+" and "+worst[2]+".";

		return report;
	}
}