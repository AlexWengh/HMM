package hmm.automation.actions;

import hmm.Activator;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.plugin.AbstractUIPlugin;

public class StopAction extends Action {
	
	private RunAction runAction;
	
	public StopAction() {
		super("Stop", AS_PUSH_BUTTON);
		setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "images/stop.gif"));
		setEnabled(false);
	}
	
	public void setRunAction(RunAction runAction) {
		this.runAction = runAction;
	}
	
	public void run() {
		if(!runAction.getJob().cancel()) {
			try {
				runAction.getJob().join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
