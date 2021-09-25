##  Cloud Data Center Simulation

---
#### Overview
[CloudSim Plus](https://cloudsimplus.org/) is a modern, up-to-date, full-featured and fully documented simulation framework. It’s easy to use and extend, enabling modeling, simulation, and experimentation of Cloud computing infrastructures and application services. Based on the CloudSim framework, it aims to improve several engineering aspects, such as maintainability, reusability and extensibility. The aim of the assignment is to create Cloud Simulators for evaluating executions of applications in cloud datacenters with different characteristics and deployment models.

#### Instructions
##### Prerequisites
Install [Simple Build Toolkit (SBT)](https://www.scala-sbt.org/1.x/docs/index.html)
##### Run the simulations
1. Clone the repository
2. Navigate to the project
3. Run cmd prompt in the project
4. Execute below cmd
```
sbt clean compile run
```

This Simulation primarily concentrates on creating 3 Data Centers with different VM combinations. The first data center is provided with 8 Virtual machines, the second with 4 and the third with 2 Virtual Machines. 


##### VM Scheduling
Vm can be scheduled between cloudlets either by sharing time or by sharing space. The project is currently set to share Time. It can be changed to share space by changing the Config file
1.VmSchedulerSpaceShared
VmSchedulerTimeShared is a Virtual Machine Monitor (VMM), also called Hypervisor, that defines a policy to allocate one or more PEs from a PM to a VM, and allows sharing of PEs by multiple VMs. This class also implements 10% performance degradation due to VM migration. It does not support over-subscription.
2.VmSchedulerTimeShared
VmSchedulerSpaceShared is a VMM allocation policy that allocates one or more PEs from a host to a Virtual Machine Monitor (VMM), and doesn't allow sharing of PEs. The allocated PEs will be used until the VM finishes running. If there is no enough free PEs as required by a VM, or whether the available PEs doesn't have enough capacity, the allocation fails. In the case of fail, no PE is allocated to the requesting VM.

##### VM Allocation Policies
Vm Allocation Policies enable the Datacenter to select a Host to place or migrate a VM.
We have evaluated 3 different policies:
1. VmAllocationPolicyBestFit
A VmAllocationPolicy implementation that chooses, as the host for a VM, that one with the most PEs in use. It is therefore a Best Fit policy, allocating each VM into the host with the least available PEs that are enough for the VM.
2.VmAllocationPolicyRoundRobin
A Round-Robin VM allocation policy which finds the next Host having suitable resources to place a given VM in a circular way. That means when it selects a suitable Host to place a VM, it moves to the next suitable Host when a new VM has to be placed. This is a high time-efficient policy with a best-case complexity O(1) and a worst-case complexity O(N), where N is the number of Hosts.
3. VmAllocationPolicySimple
A VmAllocationPolicy implementation that chooses, as the host for a VM, that one with the fewest PEs in use. 

For this project, currently the configuration is to apply Best Fit on Data Center 1, Round Robin on Data Center 2 and Simple on Data Center 3

##### Implementation of IAAS, PAAS, SAAS and FAAS
In this simulation IAAS can be implemented by taking the specified cloudlet and adding it to a standalone Vm which is then added to a standalone Host.
This provides the user with Infrastructure access.
PAAS can be implemented by wrapping the given cloudlet with a single vm of the given specification. That is then added to the list of Vms and the execution proceeds after that.
SAAS and FAAS although are technically different, for this process we can abstract the execution similarly and can be considered as a single cloudlet.

##### Results
The reults of the experiment are logged using LOGBACK into the console so we can have an overview of the whole simulation.

##### Data Center 1



![SS1](https://user-images.githubusercontent.com/21218471/134753165-e9eff02c-f536-4261-93fd-aa618e0a2507.PNG)




##### Data Center 2


![SS2](https://user-images.githubusercontent.com/21218471/134753174-7c7d8657-6ab5-4014-ae36-7c382a854f88.PNG)



##### Data Center 3

![SS3](https://user-images.githubusercontent.com/21218471/134753179-9b59dac1-2461-47de-adb4-3ba2d7978cd2.PNG)



we can see that deacllocation of virtual machines have a huge impact on the cost of the system.
Similarly when the number of hosts were decreased, there was a high cost increase till a point and suddenly vms get failed for allocation onto a host.
The Changes accross allocation policies and Vm scheduling gave us similar results where the differences are minute. Hence we can deduce that these policies can be used to fine tune the system but not drastically change the results.
