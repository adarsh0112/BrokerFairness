package edu.bitsgoa.brokerfairness;
import java.util.List;

import org.cloudbus.cloudsim.Datacenter;
import org.cloudbus.cloudsim.DatacenterBroker;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.core.CloudSim;

import edu.bitsgoa.brokerfairness.util.Common;
/**
 * Class to call Simple Fairness Algorithm.
 * @author adarsh
 *
 */
public class SimpleFairness {
	
	/**
	 * This function takes in the number of VMs, and starts a simulation with that. Further, it checks whether 
	 * the broker id fair and accordingly returns true or false, along with passing the xi_ratio in an array
	 * @param num_vms num of VMs to create
	 * @param xi_ratio	return array for xi_ratio
	 * @param dcs	array of Datacenter objects
	 * @param brokerId	Broker's ID
	 * @param broker DatacenterBroker object whose fairness we want to test
	 * @return	boolean value whether the broker is fair or not
	 */
	public static boolean simpleFairnessAlgo( int num_vms, int[] xi_ratio, Datacenter[] dcs,int brokerId,  DatacenterBroker broker)
	{
		boolean result=true; //assume strongly fair
		List<Vm> vmlist=Common.createVm(brokerId, num_vms);
		broker.submitVmList(vmlist);
		CloudSim.startSimulation();
		
		xi_ratio=Common.getXiRatio(dcs);
		Common.printXiRatio(dcs);
		
		float fairShare=(float)num_vms/dcs.length;
		for(int vi: xi_ratio)
			if(vi>Math.ceil(fairShare) || vi<Math.floor(fairShare))
				result=false; //not strongly fair
		
		if(!result){
			/*
			 * for each of the ratio terms that are less than floor(fairShare), 
			 * try to allot a vm of the required size 
			 * and see if the datacenter has bottlenecked. 
			 * If it has bottlenecked for each of those terms, broker is still fair, so  return true
			 * else return false
			 */
			result=true; //assume weakly fair
			for(int i=0; i<dcs.length; i++){
				int vi=xi_ratio[i]; //number of VMs allotted to ith DC
				int cap=dcs[i].getHostList().get(0).getTotalMips();	//total capacity for VMs (each VM takes 1 mips)
				//System.out.println(cap);
				if(vi<Math.floor(fairShare) && vi<cap) //if allotted less than fair share, should've bottlenecked
					result=false;	//if it didn't bottlneck, not weakly fair either
			}

		}			
		return result;			
	}

}
