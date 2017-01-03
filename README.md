# RTSSimulator
This code simulates the scheduling and execution of a periodic task system on a uniform multiprocessor platform (where processors may have different speeds). The tasks are scheduled using global preemptive earliest-deadline-first (GEDF). Each processor is specified  by its speed. Each task is specified by an ordered pair (C, T), where C is its worst-case execution requirement (its worst-case execution time on a unit-speed processor) and T is its period.

## Usage
Invoke the code with the command "java -jar RTSS.jar input.txt -l", where "RTSS.jar" is the location of the executable jar, "input.txt" is the file containing the inputs, and "-l" is an optional flag for logging purposes.

## Input file
The program requires a 3 line input file (see input.txt for an example). The first line contains a list of space separate real numbers which give the processor speeds. The second line contains space separated ordered pairs of real numbers which define the tasks. E.g. to specify a task with execution time 1 and period 2 and a second task with execution time 2 and period 4, the second line would contain "1,2 2,4". The third line contains a double specifying the length of the simulation.

## Output
The program outputs whether or not the task system is feasible with the given processors and the tardiness of each task. If the flag "-l" is passed in, the program also creates an output file "log.txt" which contains a listing of all events which happened during the simulation.