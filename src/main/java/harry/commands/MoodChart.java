package harry;

abstract class MoodChart extends Command {

	String help = "Displays a chart of a user's average message sentiment over time.";
	String commandString;

	public Boolean helpFlag = false;

	public MoodChart(String command) {
		this.commandString = command;
		if (command.matches(".*-help.*")) 
			helpFlag = true;
	}

 /**
	* Draw a chart
	*
	* @return 					
	*/
	public String run() {
		if (this.helpFlag)
			return help;
		String output = "";

    String[] splitted = commandString.split(" ");
    String handle = splitted[1];

    return output;
	}
}