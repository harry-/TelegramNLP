package harry;

import com.j256.ormlite.dao.Dao;

public class AllReports extends Command {

	private String help = "Shows reports for all available users.";


	public AllReports(String command) {
		if (command.matches(".*-help.*")) 
			helpFlag = true;
	}

 /**
	* Report aggregator
	* String reports for all available user together
	*
	* @return 					lots of reports all strung together
	*/
	public String run() {
		if (this.helpFlag)
			return help;
		String output = "";

	  Dao<Telegramuser, String> userdao = OrmLite.getTelegramUserDao();
	  
	  for (Telegramuser user : userdao) 
	  	output += new Report("report "+user.getHandle()).run()+"\n\n";
    
    return output;
	}
}