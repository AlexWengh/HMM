package hmm.build.handlers;
import hmm.View;
import hmm.build.execute.RunCommandsJob;
import hmm.build.execute.RunCommandsJobChangeListener;
import hmm.build.util.JobUtil;
import hmm.build.util.SettingsUtil;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.handlers.HandlerUtil;


public class RunCommandsHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		if(!SettingsUtil.getInstance().isWorkingDirCreated())
			return null;
		disableView(event);
		RunCommandsJob job = new RunCommandsJob("Running Configured Commands");
		job.addJobChangeListener(new RunCommandsJobChangeListener());
		job.setRule(JobUtil.HMM_JOB_RULE);
		job.schedule(100);
		return null;
	}
	
	public void disableView(ExecutionEvent event) {
		View view = (View) HandlerUtil.getActivePart(event).getSite().getPage().findView("HMM.view");
		view.disable();
	}

}
