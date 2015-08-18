package hmm.build.console;

import org.eclipse.ui.console.IConsoleFactory;

public class ConsoleFactory implements IConsoleFactory {
	
	public void openConsole() {
		Console.getCommandInstance();
		Console.getAutomationInstance();
		Console.getCommandInstance().showConsole();
	}

}
