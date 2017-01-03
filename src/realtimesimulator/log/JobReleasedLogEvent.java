package realtimesimulator.log;

import realtimesimulator.RealTimeJob;

public class JobReleasedLogEvent extends LogEvent {

	public JobReleasedLogEvent(double time, RealTimeJob job) {
		super(time, job);
	}

	@Override
	public String toString() {
		return super.toString() + "Job " + job.jobNumber + " of task " + job.task.taskId +
				" released.";
	}
	
}
