package harry;

import com.j256.ormlite.dao.Dao;
import harry.DerbyDB;
import java.util.List;
import java.util.Random;
import java.util.Collections;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Report extends Command {

	Logger logger = LogManager.getLogger();

	private String help = "Displays the nlp report of a given user.\nExample: report harry";

	public Report(String command) {
		this.commandString = command;
		if (command.matches(".*-help.*")) 
			helpFlag = true;
	}

 /**
	* Create a nice report
	* Aggregates all the available reports for a given user
	*
	* @return 				a verbose report
	*/
	public String run() {
		if (this.helpFlag)
			return help;

    String[] splitted = commandString.split(" ");
    String handle = splitted[1];

		String report = "This will be the report.";

		List<String> types = new DerbyDB().getTypes(handle);
		types.remove("OTHER");
		logger.debug(types.toString());
		Collections.shuffle(types);
		logger.debug(types.toString());
		String[] arrayTypes = types.toArray( new String[]{} );



		report = 	capitalize(entitySentiment(handle)) +
				favWord(handle);

		if (arrayTypes.length>1) 
			report +=	favInCategory (handle, arrayTypes[0]) +	" and "+uncapitalize(favInCategory(handle, arrayTypes[1]))+". ";
		
		report+=mood(handle);	

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



    report = xdislike+" favourite "+category.toLowerCase().replace('_', ' ')+" is "+fav[0];

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
    