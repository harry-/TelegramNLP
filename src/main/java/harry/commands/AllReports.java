package harry;

import com.j256.ormlite.dao.Dao;

public class AllReports extends Command {

	private String help = "Shows reports for all available users.";


	public AllReports(String command) {
		if (command.matches(".*help.*")) 
			helpFlag = true;
	}

 /**
	* Report aggregator
	* String reports for all available user together
	*
	* @return 					lots of reports all strung together
	*/
	public String run() {
		String output = "";

	  Dao<Telegramuser, String> userdao = OrmLite.getTelegramUserDao();
	  for (Telegramuser user : userdao) {
	  	output += Report.report(user.getHandle()) +"\n\n";
    }

    if (this.helpFlag == false)
    	return output;
    return help;
	}
}