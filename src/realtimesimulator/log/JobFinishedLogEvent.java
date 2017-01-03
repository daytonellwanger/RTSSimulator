package realtimesimulator.log;

import realtimesimulator.RealTimeJob;

public class JobFinishedLogEvent extends LogEvent {

	public JobFinishedLogEvent(double time, RealTimeJob job) {
		super(time, job);
	}

	@Override
	public String toString() {
		return super.toString() + "Job " + job.jobNumber + " of task " + job.task.taskId +
				" finished. Deadline: " + job.deadline + ".";
	}
	
}