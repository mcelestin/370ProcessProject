
public class Program 
{
	public static void main(String [] args)
	{
		Scheduler.StartSimulation(Scheduler.GetSchedulers().get(2), Process.GetProcesses("src/ProcessList.txt"));
	}
}
