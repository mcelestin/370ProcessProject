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
		}
		
		for( int j = 0; j < task.size(); j++){
			
			task.get(j).setProcessing(true); // sets true id process is running through CPU simulation
			
			// set the IO time and places process into waiting list
			if (task.get(j).isProcessing() == true ){ 
				waiting.add(task.get(j));
	
			}
		
			if (  (  task.get(j).getIdentification() == (task.size() - 1)   )   &&   (task.get(j).getBurstTime() != 0)   ) {
				task.add(waiting.get(j));
				waiting.get(j).getBurstTime();				
			}
			
			
			
		}
	
		}
		
	}

	
	// sort the incoming processes
	public void sortProcesses(LinkedList<Process> aProcessList) {
	
		return;
		
	}
	

	public void outputSnapShot
	
}
