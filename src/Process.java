import java.util.*;
import java.io.*;
public class Process 
{
	private int mIdentification;
	private int mBurstTime;
	private int mIOTime;
	private int mPriority;
	private int mArrivalTime;
	private int mExitTime;
	private int mWaitingTime;
	private int mIOExitTime;
	private LinkedList<Integer> mProcessingTime;
	private boolean mIsActive;
	private boolean mIsProcessing;
	private boolean mIsIO;
	private int mCPUAccessTime;
	private int mIOAccessTime;
	private StringBuilder mStatusBuilder;
	
	private int mCurrentIOTime;
	private int mCurrentBurstTime;
	
	private static FileReader mFileReader;
	private static BufferedReader mBufferedReader;
	private static LinkedList<Process> mProcessList; 		//List of Processes
	private static LinkedList<Integer> mProcessSequence; 	//Sequence of Processes that accessed the CPU
	
	
	public Process()
	{
		mIdentification = 0;
		mBurstTime = 0;
		mIOTime = 0;
		mPriority = 0;
		mArrivalTime = 0;
		mExitTime = 0;
		mWaitingTime = 0;
		mIOExitTime = 0;
		mCPUAccessTime = 0;
		mIOAccessTime = 0;
		mProcessingTime = null;
		mIsProcessing = false;
		mIsActive = false;
		mCurrentBurstTime = 0;
		mCurrentIOTime = 0;
	}
	public void setIdentification(int aIdentification)
	{
		mIdentification = aIdentification;
	}
	public void setBurstTime(int aBurstTime)
	{
		mBurstTime = aBurstTime;
	}
	public void setIOTime(int aIOTime)
	{
		mIOTime = aIOTime;
	}
	public void setPriority(int aPriority)
	{
		mPriority = aPriority;
	}
	public int getIdentification()
	{
		return mIdentification;
	}
	public int getBurstTime()
	{
		return mBurstTime;
	}
	public int getIOTime()
	{
		return mIOTime;
	}
	public int getPriority()
	{
		return mPriority;
	}
	public int getWaitingTime()
	{
		return mWaitingTime;
	}
	public int getTurnaroundTime()
	{
		return (mExitTime - mArrivalTime);
	}
	public void SetActiveProcess(boolean aIsActive)
	{
		if (mArrivalTime == 0 || mExitTime == 0)
		{
			if (!mIsActive && aIsActive)	//Setting Enabled to True
			{
				if (mArrivalTime == -1)
				{
					mArrivalTime = Scheduler.GetTickCount();
				}
				mIsActive = true;
				if (mCurrentBurstTime != mBurstTime)
				{
					mCurrentBurstTime = mBurstTime;
				}
				if (mCurrentIOTime != mIOTime)
				{
					mCurrentIOTime = mIOTime;
				}
			}
			if (mIsActive && !aIsActive)
			{
				if (mExitTime == 0)
				{
					mExitTime = Scheduler.GetTickCount();
				}
				mIsActive = false;
				mWaitingTime = calculateWaitingTime();
			}
		}
	}
	public void setProcessing(boolean aIsProcessing)
	{
		if (!mIsProcessing && aIsProcessing)
		{
			mIsProcessing = true;
			if (mProcessingTime == null)
			{
				mProcessingTime = new LinkedList<Integer>();
			}
			mProcessingTime.add(Scheduler.GetTickCount());
			Process.AddSequence(mIdentification);
		}
		if (mIsProcessing && !aIsProcessing)
		{
			mIsProcessing = false;
			if (mCurrentBurstTime == 0 && mCurrentIOTime == 0 && (mIOExitTime != 0))
			{
				SetActiveProcess(false);
			}
		}
	}
	public void setIO(boolean aIsIO)
	{
		if (!mIsIO && aIsIO)
		{
			mIsIO = true;
		}
		if (mIsIO && !aIsIO)
		{
			mIsIO = false;
			mIOExitTime = Scheduler.GetTickCount();
		}
	}
	public void computeBurstTime()
	{
		if (mIsProcessing)
		{
			if (mIOExitTime == 0)
			{
				if (mCurrentBurstTime <= (mBurstTime / 2) && !mIsIO)
				{
					setProcessing(false);
					setIO(true);
					return;
				}
				else
				{
					mCPUAccessTime = mCPUAccessTime + 1;
					mCurrentBurstTime = mCurrentBurstTime - 1;
				}	
			}
			else
			{
				if (mCurrentBurstTime > 0)
				{
					mCPUAccessTime = mCPUAccessTime + 1;
					mCurrentBurstTime = mCurrentBurstTime - 1;
				}
				if (mCurrentBurstTime <= 0)
				{
					setProcessing(false);
					return;
				}
			}
		}
		if (mIsIO)
		{
			if (mCurrentIOTime > 0)
			{
				mIOAccessTime = mIOAccessTime + 1;
				mCurrentIOTime = mCurrentIOTime - 1;
			}
			if (mCurrentIOTime <= 0)
			{
				setIO(false);
				return;
			}
		}
	}
	public boolean isActive()
	{
		return mIsActive;
	}
	public boolean isProcessing()
	{
		return mIsProcessing;
	}
	public boolean isIO()
	{
		return mIsIO;
	}
	private int calculateWaitingTime()
	{
		return (mExitTime - (mCPUAccessTime + mIOAccessTime) - mArrivalTime);
	}

	public static LinkedList<Process> GetProcessList()
	{
		return mProcessList;
	}
	public static float GetAverageTurnaroundTime(LinkedList<Process> aProcessList)
	{
		float averageTurnaroundTime = 0;
		for (int i = 0; i < aProcessList.size(); i++)
		{
			averageTurnaroundTime += aProcessList.get(i).getTurnaroundTime();
		}
		return (averageTurnaroundTime / aProcessList.size());
	}
	public static void AddSequence(int aIdentification)		
	{
		if (mProcessSequence == null)
		{
			mProcessSequence = new LinkedList<Integer>();
		}
		if (mProcessSequence.size() < 1)
		{
			mProcessSequence.add(aIdentification);
		}
		else if (aIdentification != mProcessSequence.getLast())
		{
			mProcessSequence.add(aIdentification);
		}
	}
	public static float GetAverageWaitingTime(LinkedList<Process> aProcessList)
	{
		float averageWaitingTime = 0;
		for(int i = 0; i < aProcessList.size(); i++)
		{
			averageWaitingTime += aProcessList.get(i).getWaitingTime();
		}
		return (averageWaitingTime / aProcessList.size());
	}
	public static String GetProcessSequence()
	{
		StringBuilder sequenceBuilder = new StringBuilder();
		for(int i = 0; i < mProcessSequence.size(); i++)
		{
			sequenceBuilder.append(mProcessSequence.get(i));
			if ((i + 1) < mProcessSequence.size())
			{
				sequenceBuilder.append('-');
			}
		}
		return sequenceBuilder.toString();
	}
	public static Process GetProcess(int aIndex)
	{
		return mProcessList.get(aIndex);
	}
	public static LinkedList<Process> GetProcesses(String aFileLocation)
	{
		File inputFile = new File(aFileLocation);
		try
		{
			if (inputFile.canRead())
			{
				mProcessList = new LinkedList<Process>();
				mFileReader = new FileReader(inputFile);
				mBufferedReader = new BufferedReader(mFileReader);
				String readLine;
				Process readProcess;
				LinkedList<Process> processList = new LinkedList<Process>();
				while ((readLine = mBufferedReader.readLine()) != null)
				{
					if ((readProcess = CheckProcessIntegrity(readLine)) != null)
					{
						processList.add(readProcess);
					}	
				}
				return processList;
			}
		}
		catch(IOException ex)
		{
			
		}
		return null;
	}
	private static Process CheckProcessIntegrity(String aReadLine)
	{
		for(int i = 0; i < aReadLine.length(); i++)
		{
			if ((aReadLine.charAt(i) != '0') && (aReadLine.charAt(i) != '1') && (aReadLine.charAt(i) != '2') && (aReadLine.charAt(i) != '3')
					&& (aReadLine.charAt(i) != '4') && (aReadLine.charAt(i) != '5') && (aReadLine.charAt(i) != '6') && (aReadLine.charAt(i) != '7')
					&& (aReadLine.charAt(i) != '8') && (aReadLine.charAt(i) != '9') && (aReadLine.charAt(i) != '\t') && (aReadLine.charAt(i) != ' '))   
			{
				return null;
			}
		}
		Process returnProcess;
		String processFields[] = new String[4];
		
		for (int i = 0; i < processFields.length; i++)
		{
			String processValue = "";
			for(int x = 0; x < aReadLine.length(); x++)
			{				
				if (aReadLine.charAt(x) >= '0' && aReadLine.charAt(x) <= '9')
				{
					processValue += aReadLine.charAt(x);
				}
				if (((aReadLine.charAt(x) == ' ') || (aReadLine.charAt(x) == '\t')) || (x + 1 == aReadLine.length()))
				{
					if (processValue != "")
					{
						processFields[i] = processValue;
						aReadLine = aReadLine.substring(x + 1);
						break;
					}
				}
			}
		}
		returnProcess = new Process();
		returnProcess.setIdentification(Integer.parseInt(processFields[0]));
		returnProcess.setBurstTime(Integer.parseInt(processFields[1]));
		returnProcess.setIOTime(Integer.parseInt(processFields[2]));
		returnProcess.setPriority(Integer.parseInt(processFields[3]));
		return returnProcess;
	}
	public String outputCurrentState()
	{
		mStatusBuilder = new StringBuilder();
		if (mIsActive)
		{
			if ((mCurrentBurstTime != mBurstTime || mCurrentIOTime != mIOTime))
			{
				if (mIsProcessing)
				{
					if (mProcessingTime.get(mProcessingTime.size() - 1) == Scheduler.GetTickCount())
					{
						mStatusBuilder.append("CPU loading job " +getIdentification() +": CPU burst (" +mCurrentBurstTime +") IO burst (" +mCurrentIOTime +")\n");
					}
					else 
					{
						mStatusBuilder.append("Servicing job " +getIdentification() +": CPU burst (" +mCurrentBurstTime +") IO burst (" +mCurrentIOTime +")\n");
					}
				}
				if ((!mIsIO && (mIOExitTime == Scheduler.GetTickCount()) && mIOExitTime != 0))
				{
					mStatusBuilder.append("Process " +getIdentification() +" Finished IO Burst.\n");
				}				
			}
		}
		if(!isActive() && mExitTime == Scheduler.GetTickCount())
		{
			mStatusBuilder.append("Process " +getIdentification() + " Has Completed.\n");
		}
		return new String(mStatusBuilder);
	}
	public static void OutputResults(LinkedList<Process> aProcessList)
	{
		System.out.println("\nFinal Report for " +Scheduler.GetSchedulerName() +" algorithm");
		System.out.println("\nThroughput = "+GetThroughPut(aProcessList.size(), Scheduler.GetTickCount()));
		System.out.println(String.format("%5s%15s", "\nPROCESS ID", "WAIT TIME"));
		for(int i = 0; i < aProcessList.size(); i++)
		{
			System.out.println(String.format("%5s%16s", aProcessList.get(i).mIdentification, +aProcessList.get(i).getWaitingTime()));
		}
		System.out.println("AVERAGE WAITING TIME = " +GetAverageWaitingTime(aProcessList));
		System.out.println("\nCPU UTILIZATION = " +GetUtilization(aProcessList, Scheduler.GetTickCount()));
		System.out.println("\nSEQUENCE OF PROCESSES IN CPU: " +GetProcessSequence());
		System.out.println(String.format("%5s%20s", "\nPROCESS ID", "TURN AROUND TIME"));
		for(int i = 0; i < aProcessList.size(); i++)
		{
			System.out.println(String.format("%5s%18s", aProcessList.get(i).mIdentification, aProcessList.get(i).getTurnaroundTime()));
		}
		System.out.println("AVERAGE TURN AROUND = " +GetAverageTurnaroundTime(aProcessList));
	}
	private static float GetThroughPut(int aCount, int aTime)
	{
		return ((float)aCount / (float)aTime);
	}
	private static float GetUtilization(LinkedList<Process> aProcessList, int aTime)
	{
		float utilizationTime = 0;
		for(int i = 0; i < aProcessList.size(); i++)
		{
			utilizationTime = utilizationTime + aProcessList.get(i).mCPUAccessTime;
		}
		return ((utilizationTime / (float)aTime) * 100);
	}
}
