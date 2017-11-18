package harry;

import com.j256.ormlite.dao.Dao;

public class ListUsers extends Command {

	private String help = "Displays a list of all users stored in the database.";


	public ListUsers(String command) {
		if (command.matches(".*help.*")) 
			helpFlag = true;
	}

 /**
	* Stuff info about all user records in a string
	*
	* @return 					a verbose list
	*/
	public String run() {
		String output = "";

	  Dao<Telegramuser, String> userdao = OrmLite.getTelegramUserDao();
	  for (Telegramuser user : userdao) {
      output+=user.getHandle()+", "+user.getFirstname()+" ("+user.getUserid()+")\n";
    }

    if (this.helpFlag == false)
    	return output;
    return help;
	}
}