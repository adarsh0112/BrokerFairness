package edu.bitsgoa.brokerfairness.util;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.cloudbus.cloudsim.CloudletSchedulerTimeShared;
import org.cloudbus.cloudsim.Datacenter;
import org.cloudbus.cloudsim.DatacenterBroker;
import org.cloudbus.cloudsim.DatacenterBrokerFair;
import org.cloudbus.cloudsim.DatacenterCharacteristics;
import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Pe;
import org.cloudbus.cloudsim.Storage;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.VmAllocationPolicySimple;
import org.cloudbus.cloudsim.VmSchedulerTimeShared;
import org.cloudbus.cloudsim.provisioners.BwProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.PeProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.RamProvisionerSimple;

/**
 * Worker class to provide the service of some commonly used functions
 * @author adarsh
 *
 */
public class Common{
	
	/**
	 * Creates list of VMs with given parameters and returns the list
	 * @param brokerId	brokerId of broker to who will handle these VMs
	 * @param vms	number of VMs to create
	 * @return	a list of created VM objects
	 */
	public static List<Vm> createVm(int brokerId, int vms) {
		//Creates a container to store VMs. This list is passed to the broker later
		LinkedList<Vm> list = new LinkedList<Vm>();

		//VM Parameters
    	int mips = 1;
    	long size = 100; //image size (MB)
    	int ram = 0; //vm memory (MB)
    	long bw = 0;
    	int pesNumber = 1; //number of cpus
    	String vmm = "Xen"; //VMM name
		//create VMs
		Vm[] vm = new Vm[vms];

		for(int i=0;i < vms;i++){
			vm[i] = new Vm(1+ i, brokerId, mips, pesNumber, ram, bw, size, vmm, new CloudletSchedulerTimeShared());
			list.add(vm[i]);
		}

		return list;
	}
	/**
	 * creates Datacenters with the given mips and names
	 * @param num	number of DCs to create
	 * @param mips	mips for the Pes in the host of the datacenter
	 * @param names names for each DC
	 * @return	an array of Datacenter objects
	 */
	public static Datacenter[] createDc(int num, int[] mips, String[] names)
	{
		Datacenter[] datacenter = new Datacenter[num];
		for(int i=0; i<num; i++)
		{
			datacenter[i] = createDatacenter(names[i], mips[i]);
			System.out.println("->"+names[i]+" capacity="+mips[i]+"mips.");
		}
		return datacenter;

	}
	
	/**
	 * private function used in createDC, creates a single DC with given mips and name
	 * @param name nme of DC
	 * @param mips	mips for host
	 * @return the created Datacenter object
	 */
	private static Datacenter createDatacenter(String name, int mips){

        
    	List<Host> hostList = new ArrayList<Host>();
    	List<Pe> peList = new ArrayList<Pe>();
    	peList.add(new Pe(0, new PeProvisionerSimple(mips))); // need to store Pe id and MIPS Rating

        
        int hostId=0;
        int ram = 2048; //host memory (MB)
        long storage = 1000000; //host storage
        int bw = 10000;

        hostList.add(
    			new Host(
    				hostId,
    				new RamProvisionerSimple(ram),
    				new BwProvisionerSimple(bw),
    				storage,
    				peList,
    				new VmSchedulerTimeShared(peList)
    			)
    		); // This is our machine


        
        String arch = "x86";      // system architecture
        String os = "Linux";          // operating system
        String vmm = "Xen";
        double time_zone = 10.0;         // time zone this resource located
        double cost = 3.0;              // the cost of using processing in this resource
        double costPerMem = 0.05;		// the cost of using memory in this resource
        double costPerStorage = 0.001;	// the cost of using storage in this resource
        double costPerBw = 0.0;			// the cost of using bw in this resource
        LinkedList<Storage> storageList = new LinkedList<Storage>();	//we are not adding SAN devices by now

        DatacenterCharacteristics characteristics = new DatacenterCharacteristics(
                arch, os, vmm, hostList, time_zone, cost, costPerMem, costPerStorage, costPerBw);

        Datacenter datacenter = null;
        try {
            datacenter = new Datacenter(name, characteristics, new VmAllocationPolicySimple(hostList), storageList, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return datacenter;
    }
	
	/**
	 * prints xi_ratio of allocation done
	 * @param dcs array of DC objects in current cloud
	 */
	public static void printXiRatio(Datacenter[] dcs ){
		System.out.println("\n================================Xi_Ratio===========================");
		int xi_ratio[]=getXiRatio(dcs);
    	for(int i=0; i<dcs.length; i++)
    		System.out.println(dcs[i].getName()+"\t= "+xi_ratio[i]+" vms allotted");
    	System.out.println("================================+++++++===========================");
	}
	
	/**
	 * returns the xi_ratio for the current cloud
	 * @param dcs array of DC objects in current cloud
	 * @return	xi_ratio
	 */
	public static int[] getXiRatio( Datacenter[] dcs ){
		int xi_ratio[]=new int[dcs.length];
    	for(int i=0; i<dcs.length; i++)
    		xi_ratio[i]=dcs[i].getVmList().size();
    	return xi_ratio;
	
	}
	
	/**
	 * Creates a broker of given type
	 * @param type this an be either 'fair' or 'default' (Unfair)
	 * @return the created broker (DatacenterBroker object)
	 */
	public static DatacenterBroker createBroker(String type)
	{

		DatacenterBroker broker = null;
		try {
			if(type.equals("fair"))
				broker = new DatacenterBrokerFair("Fair_Broker");
			else if(type.equals("default"))
				broker = new DatacenterBroker("Default_Broker");
	
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return broker;
	}
}

