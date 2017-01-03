package realtimesimulator.log;

import realtimesimulator.RealTimeJob;

public abstract class LogEvent {
	
	double time;
	RealTimeJob job;
	
	public LogEvent(double time, RealTimeJob job) {
		this.time = time;
		this.job = job;
	}
	
	@Override
	public String toString() {
		return "Time " + time + ": ";
	}
	
}