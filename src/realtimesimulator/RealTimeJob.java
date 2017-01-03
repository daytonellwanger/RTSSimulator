package realtimesimulator;

public class RealTimeJob implements Comparable<RealTimeJob> {
	
	public RealTimeTask task;
	public double deadline;
	Processor processor;
	RealTimeEvent finishEvent;
	double percentComplete;
	public int jobNumber;
	
	
	public RealTimeJob(RealTimeTask task, double deadline, int jobNumber) {
		this.task = task;
		this.deadline = deadline;
		this.jobNumber = jobNumber;
		percentComplete = 0;
	}
	
	@Override
	public int compareTo(RealTimeJob o) {
		int result0 = (int) Math.signum(this.deadline - o.deadline);
		if(result0 == 0) {
			return (int) Math.signum(task.taskId - o.task.taskId);
		} else {
			return result0;
		}
	}
	
}