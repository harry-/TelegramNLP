package harry;

import com.j256.ormlite.dao.Dao;
import harry.DerbyDB;

public class SetGender extends Command {

	private String help = "Sets the gender for a user. The corresponding pronouns will be used in reports if a user's gender is set to either male of female. In all other cases, the pronouns they/their will be used.\nExample: set gender harry male";


	public SetGender(String command) {
		this.commandString = command;
		if (command.matches(".*-help.*")) 
			helpFlag = true;
	}

 /**
	* Set the gender field in the user table
	*
	* @return 					a useless confirmation (or the help text)
	*/
	public String run() {
		if (this.helpFlag)
			return help;
		DerbyDB db = new DerbyDB();
		String output = "";
		String[] splitted = this.commandString.split(" ");
    try {
      db.setGender(splitted[2], splitted[3]);
      output = "alright then";
    } catch (IllegalArgumentException e) {
      output = e.getMessage();
    }
    return output;
	}
}
