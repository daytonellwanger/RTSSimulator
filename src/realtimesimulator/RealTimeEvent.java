package realtimesimulator;

public abstract class RealTimeEvent implements Comparable<RealTimeEvent> {

	double time;
	
	public RealTimeEvent(double time) {
		this.time = time;
	}
	
	public abstract void doAction(RealTimeSystem system);
	
	public int compareTo(RealTimeEvent o) {
		int result0 = (int) Math.signum(this.time - o.time);
		//JobFinishedEvents always precede JobReleasedEvents if occurring at the same time
		if(result0 == 0) {
			if((o instanceof JobFinishedEvent) && (this instanceof JobReleasedEvent)) {
				return 1;
			} else if((o instanceof JobReleasedEvent) && (this instanceof JobFinishedEvent)) {
				return -1;
			}
		}
		return result0;
	}
	
}