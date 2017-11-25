package harry;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import harry.Date;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MoodChart extends Command {

	Logger logger = LogManager.getLogger();

	String help = "Displays a chart of a user's average message sentiment over time.\nExample: chart realDonaldTrump";
	String commandString;

	public Boolean helpFlag = false;

	public MoodChart(String command) {
		logger.entry(command);
		this.commandString = command;
		if (command.matches(".*-help.*")) 
			helpFlag = true;
		logger.exit();
	}

 /**
	* Draw a chart
	*
	* @return 					
	*/
	public String run() {
		logger.entry();

		if (this.helpFlag)
			return help;
		String output = "";

    String[] splitted = commandString.split(" ");
    String handle = splitted[1];

    DerbyDB db = new DerbyDB();

    Date.date();
    //Chart.chart();

    List<Double> x = new ArrayList<Double>();
    List<Double> y = new ArrayList<Double>();

    ResultSet rs = db.getAverageSentiment(handle, "date"); 

    double idx = 0;
    try {
	    while(rs.next()) {
	      x.add(idx);
	      y.add(rs.getDouble("average"));
	      logger.debug(rs.getDouble("average") + "/"+ rs.getDate("date"));
	      idx++;

	    }
	  } catch (SQLException e) {
	  	logger.error(e.getMessage());
	  	logger.error(e.getStackTrace());
	  }

    double[] xArray = x.stream().mapToDouble(d -> d).toArray(); 
    double[] yArray = y.stream().mapToDouble(d -> d).toArray(); 

    try {
    	new Chart().chart(xArray, yArray, handle+"'s mood over time");
    } catch (Exception e) {
    	logger.error(e.getMessage());
    	logger.error(e.getStackTrace());
    }

    return logger.exit("photo");
	}
}