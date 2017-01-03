package realtimesimulator;

import java.util.List;

public class FeasibilityTest {

	//Tasks and processors assumed to be sorted. Processors by speed and tasks by utilization.
	public static boolean isFeasible(List<RealTimeTask> tasks, List<Processor> processors) {
		double[] speedSums = new double[processors.size()];
		speedSums[0] = processors.get(0).speed;
		for(int i = 1; i < speedSums.length; i++) {
			speedSums[i] = speedSums[i - 1] + processors.get(i).speed;
		}
		
		double[] utilizationSums = new double[tasks.size()];
		utilizationSums[0] = tasks.get(0).utilization;
		for(int i = 1; i < utilizationSums.length; i++) {
			utilizationSums[i] = utilizationSums[i - 1] + tasks.get(i).utilization;
		}
		
		boolean constraint1 = (utilizationSums[utilizationSums.length - 1] <= speedSums[speedSums.length - 1]);
		boolean constraint2 = true;
		int limit = (speedSums.length <= utilizationSums.length) ? speedSums.length : utilizationSums.length;
		for(int i = 0; i < limit; i++) {
			if(utilizationSums[i] > speedSums[i]) {
				constraint2 = false;
				break;
			}
		}
		
		return (constraint1 && constraint2);
	}
	
}