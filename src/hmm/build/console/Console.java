package hmm.build.console;
  
public class Console extends AbstractConsole {
    
	private static Console csl = new Console("Command Message Output");
	
	private static Console autoCsl = new Console("Automation Message Output");
	
    private Console(String consoleName) {
		super(consoleName);
	}

	public static Console getCommandInstance() {
    	if(csl.findConsole() == null)
    		csl.createConsole();
		return csl;
    }
	
	public static Console getAutomationInstance() {
		if(autoCsl.findConsole() == null)
			autoCsl.createConsole();
		return autoCsl;
	}

}
