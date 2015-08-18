package hmm.automation.execute;

import hmm.Activator;
import hmm.automation.models.Else;
import hmm.automation.models.If;
import hmm.automation.models.Loop;
import hmm.automation.models.TreeNode;
import hmm.automation.models.WebTreeNode;
import hmm.build.console.Console;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class RunAutomationJob extends Job {

	private TreeNode root;
	private String name = "";
	private IProgressMonitor monitor;
	private String familyStr = "HMM.AUTOMATION";

	public RunAutomationJob(String name, TreeNode root) {
		super(name);
		setUser(false);
		this.root = root;
		this.name = name;
	}
	
	public void setFamilyStr(String familyStr) {
		this.familyStr = familyStr;
	}
	
	public String getFamily() {
		return familyStr;
	}
	
	@Override
	public boolean belongsTo(Object family) {
		if(family instanceof String) {
			String str = (String) family;
			if(str.equals(familyStr))
				return true;
		}
		return false;
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
		this.monitor = monitor;
		monitor.beginTask("", IProgressMonitor.UNKNOWN);
		monitor.subTask("Running Automation Script: " + name + "  ...");
		ExecuteContext context = new ExecuteContext();
		if(monitor.isCanceled())
			return cancelRun();
		try {
			executeTree(context, root);
		} catch (Exception e) {
			e.printStackTrace();
			String errorStr = "Automation Script Execution Stopped by Errors.";
			Console.getAutomationInstance().writeErrorLine(errorStr);
			monitor.done();
			return new Status(Status.ERROR, Activator.PLUGIN_ID, errorStr, e);
         }
		if(monitor.isCanceled())
			return cancelRun();
         String succStr = "Automation Script Execution Successfully Completed.";
         Console.getAutomationInstance().writeLine(succStr);
         monitor.done();
         return new Status(Status.OK, Activator.PLUGIN_ID, succStr);
	}

	private ExecuteContext executeTree(ExecuteContext context, TreeNode node) throws Exception {
		int loopTimes = 1;
		if(node instanceof WebTreeNode) {
			if(context.getDriver() == null) {
				WebDriver driver = new FirefoxDriver();
				context.setDriver(driver);
			}
		}
		if(monitor.isCanceled()) {
			return context;
		}
		context = node.exec(context);
		if(node instanceof If) {
			if(!context.isEnterIf()) {
				context.setEnterElse(true);
				return context;
			} else {
				context.setEnterElse(false);
			}
		} else if(node instanceof Else) {
			if(!context.isEnterElse())
				return context;
		} else if(node instanceof Loop) {
			loopTimes = ((Loop) node).getCount();
		}
		for(int i = 0; i < loopTimes; ++i) {
			if(monitor.isCanceled()) {
				return context;
			}
			for (TreeNode child : node.getChildren()) {
				if(monitor.isCanceled()) {
					return context;
				}
				context = executeTree(context, child);
			}
		}
		return context;
	}
	
	private IStatus cancelRun() {
		monitor.done();
		String cancelStr = "Automation Script Execution Canceled.";
		Console.getAutomationInstance().writeErrorLine(cancelStr);
		return new Status(Status.CANCEL, Activator.PLUGIN_ID, cancelStr);
	}

}
