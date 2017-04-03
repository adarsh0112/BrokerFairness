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
 * This is the  verification service that sets up a meta cloud environment
 * and calls various fairness verification algorithms
 * @author adarsh
 *
 */
public class Service {
	
	@SuppressWarnings("unused")
	private static List<Vm> vmlist;
	
	/**
	 * The main method. This will set up  the broker and datacenters, and then call one of 
	 * the verification algorithms
	 * @param args
	 */
	public static void main(String[] args) 
	{
		try {
			
			int num_user = 1;   // number of cloud users
			Calendar calendar = Calendar.getInstance();
			boolean trace_flag = false;  // mean trace events

			// Initialize the GridSim library
			CloudSim.init(num_user, calendar, trace_flag);

			// Second step: Create Datacenters
			int num_dc=5; //number of datacenters to create
			int[] mips={86, 11, 2, 27, 1};
			String[] names={"Google", "AWS", "Azure", "Box", "Dropbox"}; //mips of each datacenter
			Datacenter[] dcs=Common.createDc(num_dc, mips, names);
			
			//Third step: Create Broker
			DatacenterBroker broker = Common.createBroker("fair"); //'fair' or 'default'
			int brokerId = broker.getId();
			
			int num_vms=100; //num of vms to request		
			int xi_ratio[]=new int[num_vms];
			
			boolean isFair=SimpleFairness.simpleFairnessAlgo(num_vms, xi_ratio, dcs, brokerId, broker );
			System.out.println("->'Broker is fair'? = "+isFair+".");

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
