package harry;

abstract class Command {

	String help = "generic help";
	String commandString;

	public Boolean helpFlag = false;

	public String displayHelp() {
		return help;
	}

	abstract String run();
}