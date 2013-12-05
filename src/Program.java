

public class Program 
{
	public static void main(String [] args)
	{
		Process.GetProcesses("src\\ProcessList.txt");
		//Populate Shceduler Objects in Collection
		//Choose the Scheduler from Collection and Call Scheduler.StartSimulation();
		PriorityScheduler peter = new PriorityScheduler("Peter");
		Scheduler.StartSimulation(peter);
	}
}
