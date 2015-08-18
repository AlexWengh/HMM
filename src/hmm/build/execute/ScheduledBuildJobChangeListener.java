package hmm.build.execute;

import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;

public class ScheduledBuildJobChangeListener extends JobChangeAdapter {

	@Override
	public void aboutToRun(IJobChangeEvent event) {
		new RunCommandsJobChangeListener().aboutToRun();
	}

	@Override
	public void done(IJobChangeEvent event) {
		new RunCommandsJobChangeListener().done();
	}

}
