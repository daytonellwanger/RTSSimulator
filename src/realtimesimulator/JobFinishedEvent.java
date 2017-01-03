package realtimesimulator;

public class JobFinishedEvent extends RealTimeEvent {
	
	RealTimeJob job;
	
	public JobFinishedEvent(double time, RealTimeJob job) {
		super(time);
		this.job = job;
	}

	@Override
	public void doAction(RealTimeSystem system) {
		system.jobFinishedEvent(job);
	}
	
	//If two jobs finish at the same time, the one executing on the
	//slower processor is handled first. This 
	@Override
	public int compareTo(RealTimeEvent o) {
		if(o instanceof JobFinishedEvent) {
			JobFinishedEvent o2 = (JobFinishedEvent) o;
			int superResult = super.compareTo(o);
			if(superResult != 0) {
				return superResult;
			} else {
				return (int) Math.signum(job.processor.speed - o2.job.processor.speed);
			}
		} else {
			return super.compareTo(o);
		}
	}

}