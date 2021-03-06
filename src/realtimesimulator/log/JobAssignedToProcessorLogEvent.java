package realtimesimulator.log;

import realtimesimulator.Processor;
import realtimesimulator.RealTimeJob;

public class JobAssignedToProcessorLogEvent extends LogEvent {

	Processor processor;
	
	public JobAssignedToProcessorLogEvent(double time, RealTimeJob job, Processor processor) {
		super(time, job);
		this.processor = processor;
	}

	@Override
	public String toString() {
		return super.toString() + "Job " + job.jobNumber + " of task " + job.task.taskId +
				" assigned to processor " + processor.processorId + ".";
	}
	
}