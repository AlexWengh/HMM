package hmm.build.util;

import hmm.build.execute.ScheduledBuildJobChangeListener;

import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.core.runtime.jobs.Job;

public class JobUtil {
	
	public static ISchedulingRule HMM_JOB_RULE = new ISchedulingRule() {
        public boolean contains(ISchedulingRule rule) {
        	return this.equals(rule);
        }
        
        public boolean isConflicting(ISchedulingRule rule) {
        	return this.equals(rule);
        }
    };

	public void initScheduledBuildJob(Job job) {
		job.addJobChangeListener(new ScheduledBuildJobChangeListener());
		job.setRule(JobUtil.HMM_JOB_RULE);
		job.schedule(100);
	}
	
}
