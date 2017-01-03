package realtimesimulator;


public class RealTimeTask implements Comparable<RealTimeTask> {
	
	double executionTime;
	double period;
	double utilization;
	public int taskId;
	private int jobNumber;
	
	
	public RealTimeTask(double executionTime, double period, int taskId) {
		this.executionTime = executionTime;
		this.period = period;
		utilization = executionTime / period;
		this.taskId = taskId;
		this.jobNumber = 0;
	}
	
	public RealTimeJob getNextJob(double currentTime) {
		jobNumber++;
		double jobDeadline = currentTime + period;
		return new RealTimeJob(this, jobDeadline, jobNumber);
	}
	
	@Override
	public int compareTo(RealTimeTask o) {
		return (int) Math.signum(o.utilization - utilization);
	}
	
}