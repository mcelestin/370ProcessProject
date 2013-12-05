import java.util.LinkedList;

public abstract class Scheduler 
{
	//Member Variables
	protected String mSchedulerName;															//Name of the Scheduler
	protected LinkedList<Process> mProcessList;													//List of Processes
	
	protected static int mTickCount;															//Tick Count
	protected static Scheduler mScheduler;														//Instance of Scheduler
	private static LinkedList<Scheduler> mSchedulerList;										//List of Schedulers
	
	public Scheduler(String aSchedulerName)
	{
		mSchedulerName 	= aSchedulerName;
		mProcessList 	= new LinkedList<Process>();
		mScheduler 		= null;
	}
	public abstract void startSimulation();														//Override Function for Simulation Run State
	public abstract void sortProcesses(LinkedList<Process> aProcessList);						//Override Function To Sort Incoming Processes
	
	public static void StartSimulation(Scheduler aScheduler, LinkedList<Process> aProcessList)	//Initialize and then Start the selected Simulation
	{
		mScheduler = aScheduler;
		mScheduler.startSimulation();
		mScheduler.sortProcesses(aProcessList);
		mScheduler.startSimulation();
	}
	public static LinkedList<Scheduler> GetSchedulers()											//Returns the List of Scheduler Objects which Extend Scheduler
	{
		if (mSchedulerList == null)
		{
			mSchedulerList = new LinkedList<Scheduler>();
		}
		mSchedulerList.add(new PriorityScheduler("PriorityScheduler"));
		return mSchedulerList;
	}
	public static int GetTickCount()															//Returns the Tick Count
	{
		return mTickCount;
	}
	public static String GetSchedulerName()
	{
		return mScheduler.mSchedulerName;
	}
}
