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

		for(int i = 0; i < Process.GetProcessList().size(); i++ ){  // loop to retrieve all processes
			task.add(Process.GetProcess(i));     // all processes are placed in jobList 
		}
		
		while(true){
		 
			for(int i = 0; i < mProcessList.size(); i++ ){  
				
					task.get(i).computeBurstTime();       // flag to indicate processes are in the jobList
					
					if(task.get(i).isActive() && task.get(i).isProcessing() && !task.get(i).isIO()){
							task.get(i).setProcessing(true);
					}
					else
							task.get(i).computeBurstTime();
					
			}
		
			
			if( mTickCount == mTickCount % mStateInterval ){
				outputSnapShot();
			}
			mTickCount++;
			
			if(mTickCount == 100)
				
				break;
		}
		
		Process.OutputResults(mProcessList);

		
	}

	
	// sort the incoming processes
	public void sortProcesses(LinkedList<Process> job) {
	
		return;
	}
	

	
	
	public void outputSnapShot()
	{
		
		
		System.out.println("t = " + FCFS.GetTickCount());
		for(int i = 0; i < mProcessList.size(); i++)
		{
			
		System.out.println(mProcessList.get(i).outputCurrentState());
			
		}
		

	}
}
