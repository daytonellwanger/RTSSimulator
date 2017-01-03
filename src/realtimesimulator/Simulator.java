package realtimesimulator;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;


public class Simulator {
	
	public static void main(String[] args) {
		if(args.length < 1) {
			System.out.println("Please provide input filename as argument");
		} else {
			boolean log = false;
			if(args.length >= 2) {
				log = args[1].equals("-l");
			}
			(new Simulator()).simulate(args[0], log);
		}
	}
	
	private void simulate(String inputFileName, boolean log) {
		File inputFile = new File(inputFileName);
		Scanner scanner;
		try {
			scanner = new Scanner(inputFile);
		} catch (Exception ex) {
			System.out.println("Could not open file " + inputFileName);
			return;
		}
		
		String[] processors = scanner.nextLine().split(" ");
		String[] tasks = scanner.nextLine().split(" ");
		double simulationTime = scanner.nextDouble();
		scanner.close();
		
		RealTimeSystem system = new RealTimeSystem(tasks.length);
		
		List<Processor> processorList = new ArrayList<Processor>();
		for(int i = 0; i < processors.length; i++) {
			processorList.add(new Processor(Double.parseDouble(processors[i]), i));
		}
		Collections.sort(processorList);
		system.setProcessors(processorList);
		
		List<RealTimeTask> taskList = new ArrayList<RealTimeTask>();
		for(int i = 0; i < tasks.length; i++) {
			String[] taskParameters = tasks[i].split(",");
			RealTimeTask task = new RealTimeTask(Double.parseDouble(taskParameters[0]), 
					Double.parseDouble(taskParameters[1]), i);
			taskList.add(task);
			system.addEvent(new JobReleasedEvent(0, task));
		}
		Collections.sort(taskList);
		
		if(FeasibilityTest.isFeasible(taskList, processorList)) {
			System.out.println("System is feasible.");
		} else {
			System.out.println("System is not feasible.");
		}
		system.simulate(simulationTime, log);
	}
	
}