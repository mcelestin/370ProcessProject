
public class Program 
{
	public static void main(String [] args)
	{
		Scheduler.StartSimulation(Scheduler.GetSchedulers().get(1), Process.GetProcesses("src\\ProcessList.txt"));
	}
}
