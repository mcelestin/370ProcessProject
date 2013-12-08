import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.LinkedList;

public class SJF extends Scheduler {

	public SJF(String sjf){
		super(sjf);
	}

	public void startSimulation(){
		

		while(true){

			sortProcesses(mProcessList);
			
			for( int j = 0;  j < mProcessList.size(); j++ ){

				mProcessList.get(j).computeBurstTime();


				if(mProcessList.get(j).isActive() && mProcessList.get(j).isProcessing() && !mProcessList.get(j).isIO()){

					mProcessList.get(j).setProcessing(true);
				}
				else 
					mProcessList.get(j).computeBurstTime();

			}

			

			if(mTickCount % mStateInterval == 0){


				outputSnapShot();

			}


			Process.OutputResults(mProcessList);

		}



	}


	public void sortProcesses(LinkedList<Process> aProcessList) {

		Process temp;

		for(int i = 0; i < aProcessList.size(); i++){

			if(aProcessList.get(i).isActive() && !aProcessList.get(i).isProcessing() && !aProcessList.get(i).isIO()){

				if(aProcessList.get(i).getBurstTime() < aProcessList.get(i-1).getBurstTime()){

					temp = aProcessList.get(i);
					aProcessList.set(i, aProcessList.get(i-1));
					aProcessList.set(i-1, temp);

				}
			}

		}
		
		mProcessList = aProcessList; 
		mProcessList.get(0).setProcessing(true);
	}


	public void outputSnapShot(){


		StringBuilder readyString = new StringBuilder();
		StringBuilder deviceString =  new StringBuilder();


		System.out.println("t = " + FCFS.GetTickCount());
		for(int i = 0; i < mProcessList.size(); i++)
		{

			System.out.println(mProcessList.get(i).outputCurrentState());
			if(mProcessList.get(i).isActive()){
				if(!mProcessList.get(i).isProcessing() && !mProcessList.get(i).isIO()){
					if( i < mProcessList.size() && readyString.length() > 0 ){

						readyString.append("-");

					}
					readyString.append(mProcessList.get(i).getIdentification());
				}

				if(!mProcessList.get(i).isProcessing() && mProcessList.get(i).isIO()){
					if( i < mProcessList.size() && deviceString.length() > 0){

						deviceString.append("-");
					}
					readyString.append(mProcessList.get(i).getIdentification());
				}
			}
		}

		System.out.println("Current State of Ready Queue:"+ new String(readyString));
		System.out.println("Current State of IO Queue:"+ new String(deviceString));


	}


}
