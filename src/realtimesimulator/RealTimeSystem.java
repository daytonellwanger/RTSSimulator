package realtimesimulator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

import realtimesimulator.log.JobAssignedToProcessorLogEvent;
import realtimesimulator.log.JobFinishedLogEvent;
import realtimesimulator.log.JobReleasedLogEvent;
import realtimesimulator.log.JobRemovedFromProcessorLogEvent;
import realtimesimulator.log.Log;
import realtimesimulator.log.LogEvent;


public class RealTimeSystem {

	private PriorityQueue<RealTimeEvent> eventQueue;
	private List<Processor> processors;
	private List<RealTimeJob> runningJobs;
	private PriorityQueue<RealTimeJob> readyQueue;
	private List<Queue<RealTimeJob>> taskQueues;
	private double[] taskTardiness;
	private double currentTime;
	private Log log;
	private boolean logEnabled;

	
	public RealTimeSystem(int numTasks) {
		eventQueue = new PriorityQueue<RealTimeEvent>();
		processors = new ArrayList<Processor>();
		runningJobs = new ArrayList<RealTimeJob>();
		readyQueue = new PriorityQueue<RealTimeJob>();
		taskQueues = new ArrayList<Queue<RealTimeJob>>();
		taskTardiness = new double[numTasks];
		for(int i = 0; i < numTasks; i++) {
			taskQueues.add(new LinkedList<RealTimeJob>());
			taskTardiness[i] = 0;
		}
	}

	public void simulate(double simulationTime, boolean logEnabled) {
		this.logEnabled = logEnabled;
		while(!eventQueue.isEmpty()) {
			RealTimeEvent event = eventQueue.remove();
			if(event.time > simulationTime) {
				break;
			} else {
				currentTime = event.time;
				event.doAction(this);
			}
		}
		if(logEnabled) {
			log.close();
		}
		System.out.println("Tardiness:");
		for(int i = 0; i < taskTardiness.length; i++) {
			System.out.println("Task " + i + ": " + taskTardiness[i]);
		}
	}
	
	private void log(LogEvent logEvent) {
		if(logEnabled) {
			if(log == null) {
				try {
					log = new Log();
				} catch (Exception ex) {ex.printStackTrace(); System.exit(1);}
			}
			log.log(logEvent);
		}
	}

	public void setProcessors(List<Processor> processors) {
		this.processors = processors;
	}
	
	public void addEvent(RealTimeEvent event) {
		eventQueue.add(event);
	}

	public void releaseJob(RealTimeJob job) {
		log(new JobReleasedLogEvent(currentTime, job));
		Queue<RealTimeJob> taskQueue = taskQueues.get(job.task.taskId);
		taskQueue.add(job);
		if(taskQueue.size() == 1) {
			queueJob(job);
		}
	}
	
	private void queueJob(RealTimeJob job) {
		if(job != null) {
			readyQueue.add(job);
			assignJobsToProcessors();
		}
	}

	public void jobFinishedEvent(RealTimeJob finishedJob) {
		List<RealTimeJob> finishedJobs = new ArrayList<RealTimeJob>();
		finishedJobs.add(finishedJob);
		RealTimeEvent nextEvent;
		while(((nextEvent = eventQueue.peek()) != null) 
				&& ((nextEvent.time == currentTime) && (nextEvent instanceof JobFinishedEvent))) {
			JobFinishedEvent jobFinishedEvent = (JobFinishedEvent) eventQueue.poll();
			finishedJobs.add(jobFinishedEvent.job);
		}
		for(RealTimeJob j : finishedJobs) {
			jobFinished(j);
		}
		assignJobsToProcessors();
	}
	
	private void jobFinished(RealTimeJob job) {
		log(new JobFinishedLogEvent(currentTime, job));
		double tardiness = Math.max(0, currentTime - job.deadline);
		if(tardiness > taskTardiness[job.task.taskId]) {
			taskTardiness[job.task.taskId] = tardiness;
		}
		job.processor.activeJob = null;
		runningJobs.remove(job);
		Queue<RealTimeJob> taskQueue = taskQueues.get(job.task.taskId);
		taskQueue.poll();
		queueJob(taskQueue.peek());
	}

	//Ensure the list of running jobs contains the highest priority jobs
	//in the system.
	private void updateRunningJobs() {
		RealTimeJob topReadyJob;
		while((topReadyJob = readyQueue.peek()) != null) {
			boolean openProcessor = (processors.size() - runningJobs.size()) > 0;
			if(openProcessor) {
				addSorted(runningJobs, readyQueue.poll());
			} else {
				RealTimeJob lowestPriorityRunningJob = runningJobs.get(runningJobs.size() - 1);
				boolean higherPriority = topReadyJob.compareTo(lowestPriorityRunningJob) < 0;
				if(higherPriority) {
					removeJobFromProcessor(lowestPriorityRunningJob);
					runningJobs.remove(runningJobs.size() - 1);
					addSorted(runningJobs, readyQueue.poll());
				} else {
					break;
				}
			}
		}
	}

	private void assignJobsToProcessors() {
		updateRunningJobs();
		for(int i = 0; i < runningJobs.size(); i++) {
			RealTimeJob job = runningJobs.get(i);
			Processor proc = processors.get(i);
			if(job.processor != proc) {
				if((job.processor != null) && (job.processor.speed == proc.speed)) {
					swapProcessors(proc, job.processor);
				} else {
					assignJobToProcessor(job, proc);
				}
			}
		}
	}
	
	private void swapProcessors(Processor proc1, Processor proc2) {
		int idx1 = processors.indexOf(proc1);
		int idx2 = processors.indexOf(proc2);
		Collections.swap(processors, idx1, idx2);
	}

	private void assignJobToProcessor(RealTimeJob job, Processor proc) {
		if(job.processor != null) {
			removeJobFromProcessor(job);
		}
		if((proc.activeJob != null) && (proc.activeJob != job)) {
			removeJobFromProcessor(proc.activeJob);
		}
		log(new JobAssignedToProcessorLogEvent(currentTime, job, proc));
		proc.activeJob = job;
		proc.activeJobStartTime = currentTime;
		job.processor = proc;
		readyQueue.remove(job);

		double remainingTime = job.task.executionTime - (job.percentComplete * job.task.executionTime);
		double finishTime = currentTime + (remainingTime / proc.speed);
		JobFinishedEvent finishEvent = new JobFinishedEvent(finishTime, job);
		job.finishEvent = finishEvent;
		addEvent(finishEvent);
	}

	private void removeJobFromProcessor(RealTimeJob job) {
		log(new JobRemovedFromProcessorLogEvent(currentTime, job, job.processor));
		job.percentComplete += job.processor.speed * ((currentTime - job.processor.activeJobStartTime) / job.task.executionTime);
		job.processor.activeJob = null;
		job.processor = null;
		eventQueue.remove(job.finishEvent);
		readyQueue.add(job);
	}
	
	public static <T extends Comparable<T>> void addSorted(List<T> list, T item) {
		for(int i = 0; i < list.size(); i++) {
			if(item.compareTo(list.get(i)) < 0) {
				list.add(i, item);
				return;
			}
		}
		list.add(item);
	}

}