/**
 * 
 */
package edu.bitsgoa.brokerfairness;

import java.util.Calendar;
import java.util.List;

import org.cloudbus.cloudsim.Datacenter;
import org.cloudbus.cloudsim.DatacenterBroker;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.core.CloudSim;

import edu.bitsgoa.brokerfairness.util.Common;
import edu.bitsgoa.brokerfairness.util.Config;
/**
 * The Simulator class that sets uup the cloud env
 * @author Adarsh Srivastava
 *
 */
public class Simulator {

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
			
			int num_user = Integer.parseInt(Config.getValue("num_user"));   // number of cloud users
			Calendar calendar = Calendar.getInstance();
			boolean trace_flag = false;  // mean trace events

			// Initialize the GridSim library
			CloudSim.init(num_user, calendar, trace_flag);

			// Second step: Create Datacenters
			int num_dc=Integer.parseInt(Config.getValue("num_dc")); //number of datacenters to create
			int[] mips= Config.getMips();	//mips of respective datacenters
			String[] names=Config.getValue("names").split(","); //names of each datacenter
			Datacenter[] dcs=Common.createDc(num_dc, mips, names);
			
			//Third step: Create Broker
			String broker_type=Config.getValue("broker_type");
			DatacenterBroker broker = Common.createBroker(broker_type); //'fair' or 'default'
			
			AuditService.audit(dcs, broker); //call the auditing Service
			
			
			//stop simulation
			CloudSim.stopSimulation();
		}
		catch (Exception e) {
			e.printStackTrace();
			Log.printLine("The simulation has been terminated due to an unexpected error");
		}
	}
}
