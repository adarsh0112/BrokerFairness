package edu.bitsgoa.brokerfairness;

import java.util.Calendar;
import java.util.List;

import org.cloudbus.cloudsim.Datacenter;
import org.cloudbus.cloudsim.DatacenterBroker;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.core.CloudSim;

import edu.bitsgoa.brokerfairness.util.Common;

/**
 * created this to demo to Santonu Sir and Krishnan
 * @author ad
 *
 */
public class TestService {
	
	private static List<Vm> vmlist;
	public static void main(String[] args) 
	{
		try {
			
			int num_user = 1;   // number of cloud users
			Calendar calendar = Calendar.getInstance();
			boolean trace_flag = false;  // mean trace events

			// Initialize the GridSim library
			CloudSim.init(num_user, calendar, trace_flag);

			// Second step: Create Datacenters
			int num_dc=2; //number of datacenters to create
			int[] mips={1000, 500};
			String[] names={"Google", "AWS"}; //mips of each datacenter
			Datacenter[] dcs=Common.createDc(num_dc, mips, names);
			
			//Third step: Create Broker
			DatacenterBroker broker = Common.createBroker("fair"); //'fair' or 'default'
			int brokerId = broker.getId();
			
			int num_vms=5;//num of vms to request
			System.out.println("->Will request "+num_vms+" VM's.\n");
			vmlist=Common.createVm(brokerId, num_vms); 

			//submit vm list to the broker
			broker.submitVmList(vmlist);

			CloudSim.startSimulation();

			//print results
			Common.printXiRatio(dcs);

        	//stop simulation
			CloudSim.stopSimulation();
		}
		catch (Exception e) {
			e.printStackTrace();
			Log.printLine("The simulation has been terminated due to an unexpected error");
		}
		// TODO Auto-generated method stub

	}

}
