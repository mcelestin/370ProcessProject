import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.LinkedList;

public class FCFS extends Scheduler {
	
	public FCFS(String fcfs){
		super(fcfs);
		
	}
	
	public void startSimulation(){  // begin simulation
		
		LinkedList<Process> task = new LinkedList<Process>(); // creates new jobList<ready queue>
		LinkedList<Process> waiting = new LinkedList<Process>(); // creates waiting queue
		mTickCount = 0;

		 
		for(int i = 0; i < Process.GetProcessList().size(); i++ ){  // loop to retrieve all processes
			task.add(Process.GetProcess(i));     // all processes are placed in jobList 
			task.get(i).setEnabled(true);       // flag to indicate processes are in the jobList 
			task.get(i).setIdentification(i);  // numbers each process added to the list
		}
		
		for( int j = 0; j < task.size(); j++){
			
			task.get(j).setProcessing(true); // sets true id process is runnign through Cpu simulation
			task.get(j).setBurstTime(mTickCount);// sets the burst time of that process
			
			if (task.get(j).getBurstTime() == ( (task.get(j).getBurstTime()) %2 ) ){ // sets the I/0 time
				task.get(j).setIOTime(mTickCount++);
				waiting.add(task.get(j));
				
			}
		}
		
	
		for(; ;){
			
			
			// condition where snapshot is taking between specified interval
			if(mTickCount == mTickCount%10){
				
				// Output results of the scheduler
				Process.OutputResults();
				// update tick Count
				mTickCount++;
			}
		}
		
	}

	
	// sort the incoming processes
	public void sortProcesses(LinkedList<Process> aProcessList) {
		
		
	}
	

}
