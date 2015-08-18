package hmm.automation.actions;

import hmm.Activator;
import hmm.automation.AutomationBlock;
import hmm.automation.AutomationEditorInput;
import hmm.automation.execute.AutomationJobChangeListener;
import hmm.automation.execute.RunAutomationJob;
import hmm.automation.models.TreeNode;
import hmm.automation.util.TreeModelXmlUtil;
import hmm.build.util.JobUtil;
import hmm.build.util.SettingsUtil;
import hmm.build.util.ShowMessageUtil;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.plugin.AbstractUIPlugin;

public class RunAction extends Action {
	
	private AutomationBlock automationBlock;
	private RunAutomationJob job;
	
	public RunAction(AutomationBlock automationBlock) {
		super("Run", AS_PUSH_BUTTON);
		setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "images/run.gif"));
		this.automationBlock = automationBlock;
	}
	
	public void run() {
		if(!SettingsUtil.getInstance().isWorkingDirCreated())
			return;
		TreeViewer viewer = automationBlock.getTreeViewer();
		AutomationEditorInput input = (AutomationEditorInput) viewer.getInput();
		TreeNode root = input.getRootModel();
		if(!root.hasChildren()) {
			new ShowMessageUtil().showInformation("There is nothing to run.");
			return;
		} else {
			boolean isTreeValid = new TreeModelXmlUtil().isTreeValid(root);
			if(!isTreeValid) {
				new ShowMessageUtil().showInformation("Errors detected, please fix errors first.");
				return;
			}
		}

		disableEditor();
		
		job = new RunAutomationJob("Running: " + input.getName(), root);
		job.addJobChangeListener(new AutomationJobChangeListener(automationBlock));
		job.setRule(JobUtil.HMM_JOB_RULE);
		job.schedule(100);
		StopAction stopAction = automationBlock.getStopAction();
		stopAction.setEnabled(true);
	}
	
	public RunAutomationJob getJob() {
		return job;
	}
	
	public void disableEditor() {
		StopAction stopAction = automationBlock.getStopAction();
		stopAction.setRunAction(this);
		setEnabled(false);
		automationBlock.getSection().setEnabled(false);
		Section section = automationBlock.findShowingSectionForPart();
		if(section != null)
			section.setEnabled(false);
	}
	
}
