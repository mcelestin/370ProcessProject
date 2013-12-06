
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
		mProcessingTime = new LinkedList<Integer>();
		mIsProcessing = false;
		mIsActive = false;
		mCurrentBurstTime = 0;
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
			}
			if (mIsActive && !aIsActive)
			{
				if (mExitTime == -1)
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
			if (mCurrentBurstTime == 0)
			{
				mCurrentBurstTime = mBurstTime;
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
			mCurrentIOTime = mIOTime;
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
				if (mCurrentBurstTime != (mBurstTime / 2))
				{
					System.out.println("Process Number " +getIdentification() +" Processing  CPU Burst: "+mCurrentBurstTime);
					mCurrentBurstTime = mCurrentBurstTime - 1;
				}	
				else if (!mIsIO) 
				{
					setProcessing(false);
					setIO(true);
					return;
				}
			}
			else
			{
				if (mCurrentBurstTime > 0)
				{
					System.out.println("Process Number " +getIdentification() +" Processing  CPU Burst: "+mCurrentBurstTime);
					mCurrentBurstTime = mCurrentBurstTime - 1;
				}
				if (mCurrentBurstTime <= 0)
				{
					System.out.println("Process Number " +getIdentification() +" Processing Done");
					setProcessing(false);
					return;
				}
			}
		}
		if (mIsIO)
		{
			if (mCurrentIOTime > 0)
			{
				System.out.println("Process Number " +getIdentification() +" In IO  IO Burst: " +mCurrentIOTime);
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
		int waitingTime = 0;
		for (int i = 0; i < mProcessingTime.size(); i++)
		{
			if (i == 0)
			{
				waitingTime += (mProcessingTime.get(i) - mArrivalTime); 
			}
			if (i > 0)
			{
				waitingTime += (mProcessingTime.get(i) - mProcessingTime.get(i - i)); 
			}
		}
		return (mExitTime - waitingTime - mArrivalTime);
	}
	public String outputCurrentState()
	{
		mStatusBuilder = new StringBuilder();
		
		if (mProcessingTime.get(mProcessingTime.size() - 1) == Scheduler.GetTickCount())
		{
			mStatusBuilder.append("CPU loading job " +getIdentification() +": CPU burst (" +getBurstTime() +") IO burst (" +getIOTime() +")\n");
		}
		else if (mIsProcessing)
		{
			mStatusBuilder.append("Servicing " +Scheduler.GetSchedulerName() +"job " +getIdentification() +": CPU burst (" +getBurstTime() +") IO burst (" +getIOTime() +")\n");
		}
		
		if ((mBurstTime == 0 && mIOTime == 0) && ((mProcessingTime.get(mProcessingTime.size() - 1) == Scheduler.GetTickCount()) || mIOExitTime == Scheduler.GetTickCount()))
		{
			mStatusBuilder.append("Process " +getIdentification() +" has Finished\n");
		}
		else if (!mIsProcessing && (mProcessingTime.get(mProcessingTime.size() - 1)) == Scheduler.GetTickCount())
		{
			mStatusBuilder.append("Process " +getIdentification() +" Finished CPU Burst\n");
		}
		else if (!mIsIO && (mIOExitTime == Scheduler.GetTickCount()))
		{
			mStatusBuilder.append("Process " +getIdentification() +" Finished IO Burst\n");
		}
		return new String(mStatusBuilder);
	}

	
	
	
	public static LinkedList<Process> GetProcessList()
	{
		return mProcessList;
	}
	public static float GetAverageTurnaroundTime()
	{
		float averageTurnaroundTime = 0;
		for (int i = 0; i < mProcessList.size(); i++)
		{
			averageTurnaroundTime += mProcessList.get(i).getTurnaroundTime();
		}
		return (averageTurnaroundTime / mProcessList.size());
	}
	public static void AddSequence(int aIdentification)		
	{
		if (mProcessSequence == null)
		{
			mProcessSequence = new LinkedList<Integer>();
		}
		mProcessSequence.add(aIdentification);
	}
	public static float GetAverageWaitingTime()
	{
		float averageWaitingTime = 0;
		for(int i = 0; i < mProcessList.size(); i++)
		{
			averageWaitingTime += mProcessList.get(i).getWaitingTime();
		}
		return (averageWaitingTime / mProcessList.size());
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
	public static void OutputResults()
	{
		System.out.println("Final Report for " +Scheduler.GetSchedulerName() +" algorithm");
		System.out.println("Throughput = ");
		System.out.println("PROCESS ID   WAIT TIME");
		for(int i = 0; i < mProcessList.size(); i++)
		{
			System.out.println(mProcessList.get(i).mIdentification + "  " +mProcessList.get(i).getWaitingTime());
		}
		System.out.println("AVERAGE WAITING TIME = " +Process.GetAverageWaitingTime());
		System.out.println("CPU UTILIZATION = ");
		System.out.println("SEQUENCE OF PROCESSES IN CPU: " +GetProcessSequence());
		System.out.println("PROCESS ID 	TURN AROUND TIME");
		for(int i = 0; i < mProcessList.size(); i++)
		{
			System.out.println(mProcessList.get(i).mIdentification + "  " +mProcessList.get(i).getTurnaroundTime());
		}
		System.out.println("AVERAGE TURN AROUND = " +GetAverageTurnaroundTime());
	}
}
