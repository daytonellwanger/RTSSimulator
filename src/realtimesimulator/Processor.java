package realtimesimulator;

public class Processor implements Comparable<Processor> {

	double speed;
	public int processorId;
	RealTimeJob activeJob;
	double activeJobStartTime;
	
	public Processor(double speed, int processorId) {
		this.speed = speed;
		this.processorId = processorId;
	}

	@Override
	public int compareTo(Processor o) {
		return (int) Math.signum(o.speed - speed);
	}
	
}