package harry;

import com.j256.ormlite.dao.Dao;
import harry.DerbyDB;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import java.util.Collections;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CategoryReport extends Command {

	Logger logger = LogManager.getLogger();

	private String help = "Displays the favourite and lest favourite entities in each category for a given user.\nExample: category report harry";

	public CategoryReport(String command) {
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
    String handle = splitted[2];

		String report = "";

		List<String> types = new DerbyDB().getTypes(handle);
		types.remove("OTHER");
		logger.debug(types.toString());
		Collections.shuffle(types);
		logger.debug(types.toString());
		String[] arrayTypes = types.toArray( new String[]{} );

		DerbyDB db = new DerbyDB();

		String[] fav = new String[1];

		for( int i = 0; i < arrayTypes.length; i++)
		{      
			try {
				 fav = db.getEntitySentimentData("DESC", 1, handle, arrayTypes[i]);
			} catch (IllegalArgumentException e)
			{
				return e.getMessage();
			}
			report += (handle+"'s favourite "+arrayTypes[i].toLowerCase().replace('_', ' ')+ " is "+fav[0]+". ");
		}
		

		return report;
	}
}

