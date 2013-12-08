import java.util.LinkedList;

public abstract class Scheduler 
{
	//Member Variables
	protected String mSchedulerName;															//Name of the Scheduler
	protected LinkedList<Process> mProcessList;													//List of Processes
	
	protected static int mStateInterval;
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
	public abstract void outputSnapShot();
	
	public static void StartSimulation(Scheduler aScheduler, LinkedList<Process> aProcessList)	//Initialize and then Start the selected Simulation
	{
		mStateInterval = 10;
		mTickCount = 0;
		mScheduler = aScheduler;
		for (int i = 0; i < aProcessList.size(); i++)
		{
			aProcessList.get(i).SetActiveProcess(true);
		}
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
		mSchedulerList.add(new FCFS("FCFS-Scheduler"));
		mSchedulerList.add(new SJF("SJF-Scheduler"));

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
