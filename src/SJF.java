import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.LinkedList;

public class SJF extends Scheduler {

	public SJF(String sjf){
		super(sjf);
	}
	
	public void startSimulation(){
		
		LinkedList<Process> task2 = new  LinkedList<Process>(); // creates ready queue
		
	
		for(int i = 0; i < Process.GetProcessList().size(); i++ ){  //loop to retrieve all processes
			task2.add(Process.GetProcess(i));    // all processes are placed in jobList <ready queue>
			task2.get(i).setEnabled(true);    // flag to indicate processes are in the jobList 
		}
		
		
		
		LinkedList<Process> waiting = new LinkedList<Process>(); // creates waiting queue
			
		Process temp2;
		
		for( int j = 0; j < task2.size(); j++){
			for(int  k = j+1; k< task2.size(); k++ ){
				
				if(Process.GetProcess(j).getBurstTime() == Process.GetProcess(k).getBurstTime() && Process.GetProcess(j).getIdentification() > Process.GetProcess(k).getIdentification() ){
					temp2 = Process.GetProcess(j);
					Process.GetProcess(k);
				}
			}
		}
		
			
			// conditions if I/O Burst = 1/2CPU Burst then process is placed in the waiting queue
			if( task2.element().getIOTime() == ((task2.element().getBurstTime())/2)){ 
				waiting.add(task2.element());
			}
			// condition if all 
			int last = task2.size()-1;
			if(task2. )
		
		
		
	}

	@Override
	public void sortProcesses(LinkedList<Process> aProcessList) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void outputSnapShot() {
		// TODO Auto-generated method stub
		
	}
	
	
}
