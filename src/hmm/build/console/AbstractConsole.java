package hmm.build.console;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;

public abstract class AbstractConsole {
	
	private MessageConsole console;
    
    private MessageConsoleStream mcs;
    
    private Color black, red;
    
    private String consoleName = "";
    
    public AbstractConsole(String consoleName) {
		this.consoleName = consoleName;
	}
    
    public void write(String msg) {
    	setColor(black);
        mcs.print(msg);
    }
    
    public void writeLine(String msg) {
    	setColor(black);
        mcs.println(msg);
    }
    
    public void writeError(String msg) {
    	setColor(red);
    	mcs.print(msg);
    }
    
    public void writeErrorLine(String msg) {
    	setColor(red);
    	mcs.println(msg);
    }
    
    private void setColor(final Color color) {
    	if(mcs.getColor() == null || mcs.getColor() != color) {
    		mcs = console.newMessageStream();
	    	Display.getDefault().syncExec(new Runnable() {
				public void run() {
					mcs.setColor(color);
				}
			});
    	}
    }
    
    public void clear() {
    	console.clearConsole();
    }

    protected MessageConsole findConsole() {
    	Display.getDefault().syncExec(new Runnable() {
			public void run() {
		        IConsoleManager conMan = ConsolePlugin.getDefault().getConsoleManager();
		        IConsole[] existing = conMan.getConsoles();
		        for (int i = 0; i < existing.length; i++) {
		            if (consoleName.equals(existing[i].getName())) {
		            	console = (MessageConsole) existing[i];
		            	return;
		            }
		        }
			}
		});
        return console;
    }
    
    protected void createConsole() {
    	Display.getDefault().syncExec(new Runnable() {
			public void run() {
		    	IConsoleManager manager = ConsolePlugin.getDefault().getConsoleManager();
		    	console = new MessageConsole(consoleName, null);
		    	manager.addConsoles(new IConsole[] { console });
		        try {
		        	Font font = new Font(Display.getCurrent(), "simsun", 9, SWT.NORMAL);
		        	console.setFont(font);
		        } catch (SWTError error) {
					error.printStackTrace();
				}
		        mcs = console.newMessageStream();
		        black = Display.getCurrent().getSystemColor(SWT.COLOR_BLACK);
		        red = Display.getCurrent().getSystemColor(SWT.COLOR_RED);
		        mcs.setColor(black);
				manager.showConsoleView(console);
			}
		});
    }
    
    public void showConsole() {
    	IConsoleManager manager = ConsolePlugin.getDefault().getConsoleManager();
    	manager.showConsoleView(console);
    }
}
