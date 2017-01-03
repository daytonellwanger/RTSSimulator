package realtimesimulator;

public class JobReleasedEvent extends RealTimeEvent {
	
	RealTimeTask task;
	
	
	public JobReleasedEvent(double time, RealTimeTask task) {
		super(time);
		this.task = task;
	}

	@Override
	public void doAction(RealTimeSystem system) {
		system.releaseJob(task.getNextJob(time));
		JobReleasedEvent nextJobReleasedEvent = new JobReleasedEvent(time + task.period, task);
		system.addEvent(nextJobReleasedEvent);
	}
	
	//If two jobs are released at the same time, the one with the earlier deadline
	//is handled first. This ensures that the one with the later deadline isn't
	//scheduled to run on a processor and then immediately replaced.
	@Override
	public int compareTo(RealTimeEvent o) {
		int superResult = super.compareTo(o);
		if((superResult != 0) || !(o instanceof JobReleasedEvent)) {
			return superResult;
		} else {
			JobReleasedEvent o2 = (JobReleasedEvent) o;
			int result0 = (int) Math.signum(task.period - o2.task.period);
			if(result0 == 0) {
				return (int) Math.signum(task.taskId - o2.task.taskId);
			} else {
				return result0;
			}
		}
	}

}