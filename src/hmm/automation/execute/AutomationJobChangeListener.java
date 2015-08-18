package hmm.automation.execute;

import hmm.automation.AutomationBlock;
import hmm.build.console.Console;

import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.forms.widgets.Section;

public class AutomationJobChangeListener extends JobChangeAdapter {

	private AutomationBlock automationBlock;
	
	public AutomationJobChangeListener(AutomationBlock automationBlock) {
		this.automationBlock = automationBlock;
	}
	
	@Override
	public void aboutToRun(IJobChangeEvent event) {
		Console.getAutomationInstance().clear();
	}
	
	@Override
	public void done(IJobChangeEvent event) {
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				automationBlock.getSection().setEnabled(true);
				Section section = automationBlock.findShowingSectionForPart();
				if(section != null)
					section.setEnabled(true);
				automationBlock.getStopAction().setEnabled(false);
				automationBlock.getRunAction().setEnabled(true);
			}
		});
	}

}
