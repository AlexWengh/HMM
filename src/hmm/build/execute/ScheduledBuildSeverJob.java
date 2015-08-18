package hmm.build.execute;

import hmm.Activator;
import hmm.build.console.Console;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

public class ScheduledBuildSeverJob extends Job {
	
	private IProgressMonitor monitor;

	public ScheduledBuildSeverJob(String name) {
		super(name);
		setUser(false);
	}

	@Override
	public boolean belongsTo(Object family) {
		if(family instanceof String) {
			String str = (String) family;
			if(str.equals("HMM.SCHEDULE"))
				return true;
		}
		return false;
	}
	
	@Override
	protected IStatus run(IProgressMonitor monitor) {
		this.monitor = monitor;
		monitor.beginTask("", IProgressMonitor.UNKNOWN);
		try {
			runCommands();
		} catch (InterruptedException e) {
			return error(e);
		}
		if(monitor.isCanceled()) {
			monitor.done();
			String cancelStr = "Scheduled Job Execution is Canceled.";
			Console.getCommandInstance().writeErrorLine(cancelStr);
			return new Status(Status.CANCEL, Activator.PLUGIN_ID, cancelStr);
		}
		monitor.done();
		String succStr = "Scheduled Build Execution Successfully Completed.";
		Console.getCommandInstance().writeLine(succStr);
		return new Status(Status.OK, Activator.PLUGIN_ID, succStr);
	}
	
	private IStatus error(InterruptedException e) {
		e.printStackTrace();
		String errorStr = "Scheduled Job Execution Stopped by Errors.";
		Console.getCommandInstance().writeErrorLine(errorStr);
		monitor.done();
		return new Status(Status.ERROR, Activator.PLUGIN_ID, errorStr, e);
	}
	
	private void runCommands() throws InterruptedException {
		monitor.subTask("Running Configured Commands ...");
		RunCommandsJob job = new RunCommandsJob("Running Configured Commands");
		job.schedule(100);
		try {
			getJobManager().join(job.getFamily(), monitor);
		} catch (OperationCanceledException e) {
			if(!job.cancel())
				job.join();
		}
		if(!job.getResult().isOK())
			monitor.setCanceled(true);
	}

}
