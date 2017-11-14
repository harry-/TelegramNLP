package harry;

import com.j256.ormlite.dao.Dao;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.sql.SQLException;
import harry.DerbyDB;

public class Report {
	
	private static Logger logger = LogManager.getLogger(); 

 /**
	* Create a nice report
	* Aggregates all the available reports for a given user
	*
	* @param	handle 	userhandle
	* @return 				a verbose report
	*/
	public static String report(String handle) {
		String report = "This will be the report.";

		report = 	capitalize(entitySentiment(handle)) +
							favWord(handle) +
							favInCategory (handle, "LOCATION") +
							" and "+uncapitalize(favInCategory(handle, "PERSON"))+". " +
							mood(handle);	

		return report;
	}

 /**
	* The three most and least favourite things
	*
	* @param		handle	userhandle
	* @return 					a verbose report
	*/
	public static String entitySentiment(String handle) {

		String report = "This will be the report.";
		String[] fav = new String[3];
		String[] worst  = new String[3];
		DerbyDB db = new DerbyDB();

		String gender = OrmLite.getGenderByHandle(handle);	


		try {
			worst = db.getEntitySentimentData("ASC", 3, handle, "ALL");
			fav = db.getEntitySentimentData("DESC", 3, handle, "ALL");
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
	* A given user's favourite thing in a category
	*
	* @param	category	an entity category (e.g. "Work_Of_Art")
	* @param 	handle  	user handle
	* @return 					a verbose report
	*/
	public static String favInCategory (String handle, String category) {
		String report = "This will be the report.";
		String[] fav = new String[1];

		DerbyDB db = new DerbyDB();

		try {
			fav = db.getEntitySentimentData("DESC", 1, handle, category);
		} catch (IllegalArgumentException e)
		{
			return e.getMessage();
		}

		String gender = OrmLite.getGenderByHandle(handle);

		String xdislike = "Their";
		if (gender != null ) {
			if (gender.equals("male"))
				xdislike = "His";
			else if (gender.equals("female"))
				xdislike = "Her";
		}

    report = xdislike+" favourite "+category.toLowerCase()+" is "+fav[0];

		return report;
	}

 /**
	* A given user's favourite word
	*
	* @param	handle	userhandle
	* @return 				a verbose report
	*/
	public static String favWord (String handle) {
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

 /**
	* Mood
	*
	* @param 	handle  	user handle
	* @return 					a verbose report
	*/
	public static String mood (String handle) {
		String report = "This will be the report.";
		Double mood = 0.0;

		DerbyDB db = new DerbyDB();

		try {
		  mood = db.getAverageSentiment(handle);
		} catch (IllegalArgumentException e)
		{
			return e.getMessage();
		}

		String gender = OrmLite.getGenderByHandle(handle);

		String xdislike = "They seem";
		if (gender != null ) {
			if (gender.equals("male"))
				xdislike = "He seems";
			else if (gender.equals("female"))
				xdislike = "She seems";
		}

		if (mood<0)
			report="in a bad mood.";
		else if (mood>0)
			report="in a good mood.";
		else
			return "";

    report = xdislike+" to generally be "+report;

		return report;
	}

 /**
	* Stuff info about all user records in a string
	*
	* @return 					a verbose list
	*/
	public static String userList() {
		String output = "";

	  Dao<Telegramuser, String> userdao = OrmLite.getTelegramUserDao();
	  for (Telegramuser user : userdao) {
      output+=user.getHandle()+", "+user.getFirstname()+" ("+user.getUserid()+")\n";
    }
    return output;
	}

 /**
	* Report aggregator
	* String reports for all available user together
	*
	* @return 					lots of reports all strung together
	*/
	public static String allReports() {
		String output = "";

	  Dao<Telegramuser, String> userdao = OrmLite.getTelegramUserDao();
	  for (Telegramuser user : userdao) {
	  	output += report(user.getHandle()) +"\n\n";
    }
    return output;
	}

 /**
	* Capitalize the first character of a string
	*
	* @param	input 		some string
	* @return 					same string with the first letter capitalized
	*/
	public static String capitalize(String input) {
		return input.substring(0, 1).toUpperCase() + input.substring(1);
	}

 /**
	* Uncapitalize the first character of a string
	*
	* @param	input 		some string
	* @return 					same string with the first letter uncapitalized
	*/
	public static String uncapitalize(String input) {
		return input.substring(0, 1).toLowerCase() + input.substring(1);
	}
}

