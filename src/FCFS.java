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
		mTickCount = 0;
		Process temp;
		 
		for(int i = 0; i < Process.GetProcessList().size(); i++ ){  // loop to retrieve all processes
			task.add(Process.GetProcess(i));    // all processes are placed in jobList 
			task.get(i).setEnabled(true);    // flag to indicate processes are in the jobList 
		}
		
	
		for( int j = 0; j < task.size(); j++){
			for(int  k = j+1; k < task.size(); k++ ){
				
				int burst1 = Process.GetProcess(j).getBurstTime();   // Burst time of process j
				int burst2 = Process.GetProcess(k).getBurstTime();	// Burst time of process k
				int P_ID1 = Process.GetProcess(j).getIdentification(); // ID of process j	
				int P_ID2 = Process.GetProcess(k).getIdentification(); // ID of process k
				
				
				// condition if process j and k have the same burst time  then the lower process ID get processed first
				if(task.indexOf(burst1) ==  task.indexOf(burst2) && task.indexOf(P_ID1)  > task.indexOf(P_ID2) ){
					temp = task.get(j);
					
					
				}
			}
		}
		
	
		for(; ;){
			
			// Output Resluts of the scheduler
			Process.OutputResults();
			// update tick Count
			mTickCount++;
			
		}
		
	}

	
	// sort the incoming processes
	public void sortProcesses(LinkedList<Process> aProcessList) {
			
		
	}
	

}
