import java.util.LinkedList;
//import java.util.Locale.Category;

import org.omg.CosNaming._BindingIteratorImplBase;


public class PriorityScheduler extends Scheduler
{
	private Process mPriorityProcess;
	public PriorityScheduler(String aSchedulerName)
	{
		super(aSchedulerName);
	}
	public void startSimulation()
	{
		for(; ;)
		{
			setPriorityProcess();
			for (int i = 0; i < mProcessList.size(); i++)
			{
				mProcessList.get(i).computeBurstTime();
			}
			if (mTickCount % Scheduler.mStateInterval == 0)
			{
				outputSnapShot();
			}
			mTickCount++;
			if (mTickCount == 100)
			{
				break;
			}
		}
	}
	private void setPriorityProcess()
	{
		for (int i = 0; i < mProcessList.size(); i++)
		{
			if (!mProcessList.get(i).isIO() && mProcessList.get(i).isActive())
			{
				if ((mProcessList.get(i).getPriority() <= mPriorityProcess.getPriority()) || (mPriorityProcess.isIO() || !mPriorityProcess.isActive()))
				{
					mPriorityProcess = mProcessList.get(i);
					mPriorityProcess.setProcessing(true);
					for (int x = i + 1; x < mProcessList.size(); x++)
					{
						mProcessList.get(x).setProcessing(false);
					}
					break;
				}		
			}
		}
	}

	public void sortProcesses(LinkedList<Process> aProcessList)
	{
		for (int i = 0; i < aProcessList.size(); i++)
		{
			if ((i + 1) < aProcessList.size())
			{
				for (int x = i + 1; x < aProcessList.size(); x++)
				{
					if (aProcessList.get(i).getPriority() > aProcessList.get(x).getPriority())
					{
						Process tempProcess = aProcessList.get(i);
						aProcessList.set(i, aProcessList.get(x));
						aProcessList.set(x, tempProcess);
					}
				}
			}
		}
		mProcessList.add(aProcessList.get(0));
		mProcessList.add(aProcessList.get(1));
		mProcessList.add(aProcessList.get(2));
		mProcessList.add(aProcessList.get(3));
		mProcessList.add(aProcessList.get(4));
		mProcessList.add(aProcessList.get(5));
		mProcessList.add(aProcessList.get(6));
		mProcessList.add(aProcessList.get(7));
		mProcessList.get(0).setProcessing(true);
		mPriorityProcess = mProcessList.get(0);
	}
	public void outputSnapShot()
	{
		System.out.println("Tick Count = " +Scheduler.GetTickCount());
		for(int i = 0; i < Process.GetProcessList().size(); i++)
		{
			Process.GetProcessList().get(i).outputCurrentState();
		}
	}
}
