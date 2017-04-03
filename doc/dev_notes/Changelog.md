# Changes to Cloudsim library
* In DatacenterBroker.class
	* Modified print statements to display clock to just two decimal places
	* Made two methods public- getDatacenterIdsList() and getVmsToDatacentersMap()
	* Print clock to just 2 decimal places
* Extend DatacenterBroker to create DatacenterBrokerFair
	* Allots Vms in a round robin fashion

# Changes to project source code
* Got rid of test, test2.  Added Service, Utility, TestService and SimpleFairness classes
* Added SimpleFairness Algo
* Added a new util package
* created AuditService and Simulator
* created config.properties file and a Config class to read from that
