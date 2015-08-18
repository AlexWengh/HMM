package hmm.build.settings;

import java.util.LinkedList;
import java.util.List;

public class CommandLines {
	
	private static CommandLines commandLines = new CommandLines();
	
	private List<String> commands = new LinkedList<String>();
	
	private CommandLines() {}
	
	public static CommandLines getInstance() {
		return commandLines;
	}
	
	public void addCommand(String command) {
		commands.add(command);
	}
	
	public void removeCommand(int index) {
		commands.remove(index);
	}
	
	public void modifyCommand(int index, String command) {
		commands.set(index, command);
	}
	
	public void exchangeCommand(int index1, int index2) {
		String temp = commands.get(index1);
		commands.set(index1, commands.get(index2));
		commands.set(index2, temp);
	}
	
	public List<String> getCommands() {
		return commands;
	}
}
