package hmm.build.handlers;
import hmm.build.execute.ScheduledBuildClientJob;
import hmm.build.execute.ScheduledBuildSeverJob;
import hmm.build.settings.Settings;
import hmm.build.util.JobUtil;
import hmm.build.util.SettingsUtil;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.handlers.HandlerUtil;


public class ScheduleHandler extends AbstractHandler {
	
	private Timer timer;

	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {
		boolean checked = HandlerUtil.toggleCommandState(event.getCommand());
		if(!checked) {
			if(!SettingsUtil.getInstance().isWorkingDirCreated()) {
				HandlerUtil.toggleCommandState(event.getCommand());
				return null;
			}
			timer = new Timer();
			Date scheduledTime = Settings.getInstance().getBuildTime();
			if(scheduledTime == null)
				scheduledTime = new Date();
			int intervalDays = Settings.getInstance().getIntervalDays();
			Calendar scheduled = Calendar.getInstance();
			scheduled.setTime(scheduledTime);
			Calendar future = Calendar.getInstance();
			int hour = scheduled.get(Calendar.HOUR_OF_DAY);
			int minute = scheduled.get(Calendar.MINUTE);
			int second = scheduled.get(Calendar.SECOND);
			future.set(Calendar.HOUR_OF_DAY, hour);
			future.set(Calendar.MINUTE, minute);
			future.set(Calendar.SECOND, second);
			Calendar now = Calendar.getInstance();
			if(future.getTimeInMillis() <= now.getTimeInMillis())
				future.add(Calendar.DATE, 1);
			TimerTask task = new TimerTask() {
				@Override
				public void run() {
					executeJob(event);
				}
			};
			timer.schedule(task, future.getTime(), intervalDays * 86400000);
			// executeJob(event);
		} else {
			if(timer != null)
				timer.cancel();
		}
		return null;
	}
	
	private void executeJob(final ExecutionEvent event) {
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				new RunCommandsHandler().disableView(event);
				if(Settings.getInstance().getServerIp().isEmpty()) {
					ScheduledBuildSeverJob job = new ScheduledBuildSeverJob("Running Scheduled Build Task (Server) ");
					new JobUtil().initScheduledBuildJob(job);
				} else {
					ScheduledBuildClientJob job = new ScheduledBuildClientJob("Running Scheduled Build Task (Client) ");
					new JobUtil().initScheduledBuildJob(job);
				}
			}
		});
	}

}
