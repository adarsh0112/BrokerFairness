# Next steps

### Major  Chunks
* create a Simulator class- this is the guy that sets up the cloud env
* create an AuditService Class- this is the service that calls verification algos on broker
* Make a Provisioning request depend on more factors (like ram, space, etc.) instead of just mips
** Make the datacenter have additional characteristics as above

* ~~Implement simple fairness algorithm~~
* ~~Fix datacentrebrokerfair breaking down with many VMs~~

### Minor tweaks
* Include a config file, and make all programs read from that
* ~~create a todo~~
* ~~Break the functions of test2.java into a utility class to be imported and reused~~
* push project to git
* ~~figure out a way to find if a DC bottlenecked~~



# TODO
* implement a way to figure out total cost of allocation (basically total number of VMs taken to allocate
* Implement Probabilistic fairness algorithm