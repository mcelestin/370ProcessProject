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
	private LinkedList<Integer> mProcessingTime; 
	private boolean mIsEnabled;
	private boolean mIsProcessing;
	
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
		mProcessingTime = new LinkedList<Integer>();
		mIsEnabled = false;
		mIsProcessing = false;
	}
	
	public void setEnabled(boolean aEnabled)//Enters / Exits Ready Queue
	{
		if (mArrivalTime == -1 || mExitTime == -1)
		{
			if (!mIsEnabled && aEnabled)	//Setting Enabled to True
			{
				if (mArrivalTime == -1)
				{
					mArrivalTime = Scheduler.GetTickCount();
				}
				mIsEnabled = true;
			}
			if (mIsEnabled && !aEnabled)
			{
				if (mExitTime == -1)
				{
					mExitTime = Scheduler.GetTickCount();
				}
				mIsEnabled = false;
				mWaitingTime = calculateWaitingTime();
			}
		}
	}
	public void setProcessing(boolean aProcessing)//Has the Processor 
	{
		if (!mIsProcessing && aProcessing)
		{
			mIsProcessing = true;
			mProcessingTime.add(Scheduler.GetTickCount());
			Process.AddSequence(mIdentification);
		}
		if (mIsProcessing && !aProcessing)
		{
			mIsProcessing = false;
		}
	}
	public int getWaitingTime()
	{
		return mWaitingTime;
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
	public int getTurnaroundTime()
	{
		return (mExitTime - mArrivalTime);
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
	
	
	/*
		START STATIC FUNCTIONS:
		All aspects of the attributes are handled via Class level Functions
	*/
	
	
	
	
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
