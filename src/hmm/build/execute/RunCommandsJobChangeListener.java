package hmm.build.execute;

import hmm.View;
import hmm.build.console.Console;

import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;

public class RunCommandsJobChangeListener extends JobChangeAdapter {

	@Override
	public void aboutToRun(IJobChangeEvent event) {
		aboutToRun();
	}

	@Override
	public void done(IJobChangeEvent event) {
		done();
	}
	
	public void aboutToRun() {
		Display.getDefault().syncExec(new Runnable() {
			@Override
			public void run() {
				Console.getCommandInstance().clear();
			}
		});
	}
	
	public void done() {
		Display.getDefault().syncExec(new Runnable() {
			@Override
			public void run() {
				View view = (View) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView("HMM.view");
				view.enable();
			}
		});
	}

}
