package harry;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import harry.DerbyDB;



public class Report {

	/**
	 * Create a report
	 *
	 * @param		handle	userhandle
	 * @return 	a verbose report
	 */
	public String report(String handle) {
		String report = "This will be the report.";

		report = 	entitySentiment(handle) +
							favWord(handle);	

		return report;
	}

	/**
	 * The three most and least favourite things
	 *
	 * @param handle	userhandle
	 * @return a verbose report
	 */
	public String entitySentiment(String handle) {
		String report = "This will be the report.";
		String[] fav = new String[3];
		String[] worst  = new String[3];
		DerbyDB db = new DerbyDB();

		String gender = OrmLite.getGenderByHandle(handle);	

		try {
			worst = db.getEntitySentimentData("ASC", 3, handle);
			fav = db.getEntitySentimentData("DESC", 3, handle);
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

    report = handle +" likes "+fav[0]+", "+fav[1]+" and "+fav[2]+". "+xdislike+" "+worst[0]+", "+worst[1]+" and "+worst[2]+". ";

		return report;
	}

	/**
	 * A given user's favourite word
	 *
	 * @param handle	userhandle
	 * @return a verbose report
	 */
	public String favWord (String handle) {
		String report = "This will be the report.";
		String fav = "";

		DerbyDB db = new DerbyDB();

		try {
			fav = db.getFavWord(handle);
	
		} catch (IllegalArgumentException e)
		{
			return e.getMessage();
		}

		String gender = OrmLite.getGenderByHandle(handle);

		String xdislike = "They talk";
		if (gender != null ) {
			if (gender.equals("male"))
				xdislike = "He talks";
			else if (gender.equals("female"))
				xdislike = "She talks";
		}

    report = xdislike+" about "+fav+" a lot. ";

		return report;
	}
}

