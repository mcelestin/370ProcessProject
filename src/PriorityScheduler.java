import java.util.LinkedList;



public class PriorityScheduler extends Scheduler
{
	
	public PriorityScheduler(String aSchedulerName) {
		super(aSchedulerName);
		// TODO Auto-generated constructor stub
	}

	public void startSimulation()
	{
		LinkedList<Process> cat = new LinkedList<Process>();
		//populate list
		for(int i = 0; i < Process.GetProcessList().size(); i++)
		{
			cat.add(Process.GetProcess(i));
			cat.get(i).setEnabled(true);
		}
		

		//Sort
		//Initialize a Queue/Collection
		//Do Simulation
		for(; ;)
		{
			//Output Snapshots
			mTickCount++;
		}
	}
}
