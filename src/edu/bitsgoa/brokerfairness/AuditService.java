package edu.bitsgoa.brokerfairness;

import java.util.List;

import org.cloudbus.cloudsim.Datacenter;
import org.cloudbus.cloudsim.DatacenterBroker;
import org.cloudbus.cloudsim.Vm;

import edu.bitsgoa.brokerfairness.util.Config;

/**
 * This is the AuditService Class that runs various verification algorithms on the existing cloud structure
 * @author Adarsh Srivastava
 *
 */
public class AuditService {
	
	@SuppressWarnings("unused")
	private static List<Vm> vmlist;
	/**
	 * The method used to call the verification algos
	 * @param dcs the array of datacenter objects
	 * @param broker	the DatacenterBroker object
	 */
	public static void audit(Datacenter[] dcs, DatacenterBroker broker) 
	{
			int brokerId = broker.getId();
			
			int num_vms=Integer.parseInt(Config.getValue("num_vms")); //num of vms to request		
			int xi_ratio[]=new int[num_vms];
			
			boolean isFair=SimpleFairness.simpleFairnessAlgo(num_vms, xi_ratio, dcs, brokerId, broker );
			System.out.println("->'Broker is fair'? = "+isFair+".");
	}

}
