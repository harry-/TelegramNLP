package harry;

abstract class Command {

	private String help = "generic help";
	public Boolean helpFlag = false;

	public String displayHelp() {
		return help;
	}

	abstract String run();
}