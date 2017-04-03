/*
 * Title:        CloudSim Toolkit
 * Description:  CloudSim (Cloud Simulation) Toolkit for Modeling and Simulation of Clouds
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2009-2012, The University of Melbourne, Australia
 */

package org.cloudbus.cloudsim;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.text.DecimalFormat;

import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.core.CloudSimTags;
import org.cloudbus.cloudsim.core.SimEntity;
import org.cloudbus.cloudsim.core.SimEvent;
import org.cloudbus.cloudsim.lists.CloudletList;
import org.cloudbus.cloudsim.lists.VmList;

/**
 * DatacentreBroker represents a broker acting on behalf of a user. It hides VM management, as vm
 * creation, submission of cloudlets to VMs and destruction of VMs.
 * 
 * @author Rodrigo N. Calheiros
 * @author Anton Beloglazov
 * @since CloudSim Toolkit 1.0
 */
public class DatacenterBrokerFair extends DatacenterBroker {

	static DecimalFormat numberFormat = new DecimalFormat("#.00");
	
	protected List<Integer> datacenterTriedIdsList=new ArrayList<Integer>();

	public DatacenterBrokerFair(String name) throws Exception {
		super(name);
	}
	/**
	 * Process the ack received due to a request for VM creation.
	 * 
	 * @param ev a SimEvent object
	 * @pre ev != null
	 * @post $none
	 */
	protected void processVmCreate(SimEvent ev) 
	{
		int[] data = (int[]) ev.getData();
		int datacenterId = data[0];
		int vmId = data[1];
		int result = data[2];

		if (result == CloudSimTags.TRUE) {
			getVmsToDatacentersMap().put(vmId, datacenterId);
			getVmsCreatedList().add(VmList.getById(getVmList(), vmId));
			Log.printConcatLine(numberFormat.format(CloudSim.clock()), ": ", getName(), ": VM #", vmId,
					" has been created in ", CloudSim.getEntityName(datacenterId), ", Host #",
					VmList.getById(getVmsCreatedList(), vmId).getHost().getId());
			
		} else {
			Log.printConcatLine(numberFormat.format(CloudSim.clock()), ": ", getName(), ": Creation of VM #", vmId,
					" failed in ", CloudSim.getEntityName(datacenterId));
			getDatacenterRequestedIdsList().add(datacenterId);
			if(getDatacenterTriedIdsList().contains(datacenterId))
				getDatacenterTriedIdsList().remove((Integer)datacenterId);
		}

		incrementVmsAcks();

		// all the requested VMs have been created
		if (getVmsCreatedList().size() == getVmList().size() - getVmsDestroyed()) {
			submitCloudlets();
		} 
		else {
			// all the acks received, but some VMs were not created
			if (getVmsRequested() == getVmsAcks()) {
				// find id of the next datacenter that has not been tried
				/*
				System.out.println("getDatacenterIdsList :=");
				for(int id: getDatacenterIdsList())
						System.out.println(CloudSim.getEntityName(id));
				*/
				for (int nextDatacenterId : getDatacenterIdsList()) {
					//print both lists
					/*
					System.out.println("*****Checking for "+CloudSim.getEntityName(nextDatacenterId));
					
					System.out.println("##getDatacenterRequestedIdsList :=");
					for(int id: getDatacenterRequestedIdsList())
						System.out.println(CloudSim.getEntityName(id));
					
					System.out.println("##getDatacenterTriedIdsList :=");
					for(int id: getDatacenterTriedIdsList())
						System.out.println(CloudSim.getEntityName(id));
					*/

					if (!getDatacenterRequestedIdsList().contains(nextDatacenterId) && !getDatacenterTriedIdsList().contains(nextDatacenterId)) 
					{
					createVmsInDatacenter(nextDatacenterId);
					return;
					}
				}

				// all datacenters already queried
				if (getVmsCreatedList().size() > 0) { // if some vm were created
					submitCloudlets();
				} else { // no vms created. abort
					Log.printLine(numberFormat.format(CloudSim.clock()) + ": " + getName()
							+ ": none of the required VMs could be created. Aborting");
					finishExecution();
				}
			}
		}
	}


	/**
	 * Create the submitted virtual machines in a datacenter.
	 * 
	 * @param datacenterId Id of the chosen Datacenter
	 * @pre $none
	 * @post $none
         * @see #submitVmList(java.util.List) 
	 */
	protected void createVmsInDatacenter(int datacenterId) 
	{
		// send as much vms as possible for this datacenter before trying the next one
		int requestedVms = 0;
		String datacenterName = CloudSim.getEntityName(datacenterId);
		for (Vm vm : getVmList()) {
			if (!getVmsToDatacentersMap().containsKey(vm.getId())) {
				Log.printLine(numberFormat.format(CloudSim.clock()) + ": " + getName() + ": Trying to Create VM #" + vm.getId()
						+ " in " + datacenterName);
				sendNow(datacenterId, CloudSimTags.VM_CREATE_ACK, vm);
				requestedVms++;
				break;
			}
		}
		getDatacenterTriedIdsList().add(datacenterId);
		if (getDatacenterTriedIdsList().size()==(getDatacenterIdsList().size()-getDatacenterRequestedIdsList().size()))
			getDatacenterTriedIdsList().clear();
		//getDatacenterRequestedIdsList().add(datacenterId);

		setVmsRequested(requestedVms);
		setVmsAcks(0);
	}
	protected List<Integer> getDatacenterTriedIdsList() 
	{
		return datacenterTriedIdsList;
	}
	

}
