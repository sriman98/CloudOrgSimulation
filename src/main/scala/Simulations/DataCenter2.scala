package Simulations

import HelperUtils.{CreateLogger, ObtainConfigReference}
import Simulations.DataCenter1.config
import org.cloudbus.cloudsim.allocationpolicies.{VmAllocationPolicyAbstract, VmAllocationPolicyBestFit, VmAllocationPolicyRandom, VmAllocationPolicyRoundRobin, VmAllocationPolicySimple}
import org.cloudbus.cloudsim.brokers.{DatacenterBroker, DatacenterBrokerSimple}
import org.cloudbus.cloudsim.cloudlets.{Cloudlet, CloudletSimple}
import org.cloudbus.cloudsim.core.CloudSim
import org.cloudbus.cloudsim.datacenters.DatacenterSimple
import org.cloudbus.cloudsim.hosts.{Host, HostSimple}
import org.cloudbus.cloudsim.resources.{Pe, PeSimple}
import org.cloudbus.cloudsim.schedulers.vm.{VmSchedulerAbstract, VmSchedulerSpaceShared, VmSchedulerTimeShared}
import org.cloudbus.cloudsim.utilizationmodels.UtilizationModelDynamic
import org.cloudbus.cloudsim.vms.{Vm, VmSimple}
import org.cloudsimplus.builders.tables.{CloudletsTableBuilder, TextTableColumn}

import java.text.DecimalFormat
import java.util
import collection.JavaConverters.*

//Second data center
class DataCenter2
object DataCenter2:

  //making sure the config file exists
  val config = ObtainConfigReference("DATA_CENTER2") match {
    case Some(value) => value
    case None => throw new RuntimeException("Cannot obtain a reference to the config data.")
  }

  //creating a logger class
  val logger = CreateLogger(classOf[DataCenter2])

  //starting the cloud simulator and creating the broker
  def Start() =
    val cloudsim = new CloudSim();
    val broker0 = new DatacenterBrokerSimple(cloudsim);


    val hostPes = List(new PeSimple(config.getLong("DATA_CENTER2.MIPS_CAPACITY")))
    logger.info(s"Created one processing element: $hostPes")



    //Configuring the VM scheduling algorithm
    def vmSchedule: VmSchedulerAbstract={
      config.getInt("VM_SCHEDULE") match {
        case 1 => new VmSchedulerTimeShared
        case 2 => new VmSchedulerSpaceShared
        case _ => new VmSchedulerTimeShared
      }
    }

    //Create Processing elements for Hosts
    def createHostPesList(HOSTS_PES: Int) = {
      val pesList = new util.ArrayList[Pe]
      (1 to HOSTS_PES).map(_ => pesList.add(new PeSimple(config.getLong("DATA_CENTER2.MIPS_CAPACITY"))))
      pesList
    }

    //Create Hosts
    def createHostList(HOSTS: Int) = {
      val hostlist = new util.ArrayList[Host]
      (1 to HOSTS).map(hostPesList => hostlist.add(new HostSimple(config.getLong("DATA_CENTER1.HOST_RAM"),
        config.getLong("DATA_CENTER2.HOST_STORAGE"),
        config.getLong("DATA_CENTER2.HOST_BW"),
        createHostPesList(config.getInt("DATA_CENTER2.HOST_PES")))
        .setVmScheduler(vmSchedule)
      ))
      hostlist
    }

    val hostList = createHostList(config.getInt("DATA_CENTER2.HOSTS"))


    logger.info(s"Created hosts: $hostList")

    //Give the VM Allocation Policy
    def vmAllocate: VmAllocationPolicyAbstract={
      config.getInt("DATA_CENTER2.VM_ALLOCATION") match {
        case 1 => new VmAllocationPolicyBestFit
        case 2 => new VmAllocationPolicyRoundRobin
        case 3 => new VmAllocationPolicySimple
        case _ => new VmAllocationPolicyBestFit
      }
    }

    val dc0 = new DatacenterSimple(cloudsim, hostList,vmAllocate);

    // Creating the VMS for the hosts
    val vmList = List(
      new VmSimple(config.getLong("VM1.MIPS_CAPACITY"),config.getLong("VM1.PES"))
        .setRam(config.getLong("VM1.RAM"))
        .setBw(config.getLong("VM1.BW"))
        .setSize(config.getLong("VM1.STORAGE")),
      new VmSimple(config.getLong("VM2.MIPS_CAPACITY"),config.getLong("VM2.PES"))
        .setRam(config.getLong("VM2.RAM"))
        .setBw(config.getLong("VM2.BW"))
        .setSize(config.getLong("VM2.STORAGE")),
      new VmSimple(config.getLong("VM3.MIPS_CAPACITY"),config.getLong("VM3.PES"))
        .setRam(config.getLong("VM3.RAM"))
        .setBw(config.getLong("VM3.BW"))
        .setSize(config.getLong("VM3.STORAGE")),
      new VmSimple(config.getLong("VM4.MIPS_CAPACITY"),config.getLong("VM4.PES"))
        .setRam(config.getLong("VM4.RAM"))
        .setBw(config.getLong("VM4.BW"))
        .setSize(config.getLong("VM4.STORAGE"))
    )


    logger.info(s"Created virtual machines: $vmList")

    val utilizationModel = new UtilizationModelDynamic(config.getDouble("UTILIZATION_RATIO"));
    //Create the cloudlets
    val cloudletList = new CloudletSimple(config.getLong("SAAS1.SIZE"), config.getInt("SAAS1.PES"), utilizationModel)::
      new CloudletSimple(config.getLong("SAAS2.SIZE"), config.getInt("SAAS2.PES"), utilizationModel)::
      new CloudletSimple(config.getLong("SAAS3.SIZE"), config.getInt("SAAS3.PES"), utilizationModel)::
      new CloudletSimple(config.getLong("SAAS4.SIZE"), config.getInt("SAAS4.PES"), utilizationModel)::
      new CloudletSimple(config.getLong("FAAS1.SIZE"), config.getInt("FAAS1.PES"), utilizationModel)::
      new CloudletSimple(config.getLong("FAAS2.SIZE"), config.getInt("FAAS2.PES"), utilizationModel)::
      new CloudletSimple(config.getLong("FAAS3.SIZE"), config.getInt("FAAS3.PES"), utilizationModel)::
      new CloudletSimple(config.getLong("FAAS4.SIZE"), config.getInt("FAAS4.PES"), utilizationModel)::
      Nil

    logger.info(s"Created a list of cloudlets: $cloudletList")

    //Give the list of VMs and Cloudlets to the broker and start the simulation
    broker0.submitVmList(vmList.asJava);
    broker0.submitCloudletList(cloudletList.asJava);

    logger.info("Starting cloud simulation...")
    cloudsim.start();

    new CloudletsTableBuilder(broker0.getCloudletFinishedList()).build();

    //Get the results of the simulation and add cost heuristics to compare
    def printResults(broker:DatacenterBroker,COST_PER_SECOND:Double,COST_PER_MEMORY:Double,COST_PER_STORAGE:Double,COST_PER_BW:Double): Unit = {
      logger.info("Printing the results")
      val df = new DecimalFormat("0.00")
      new CloudletsTableBuilder(broker.getCloudletFinishedList)
        .addColumn(new TextTableColumn("   Cost   ", "Execution"), (cloudlet: Cloudlet) =>  df.format(cloudlet.getActualCpuTime*COST_PER_SECOND*100))
        .addColumn(new TextTableColumn("   Cost   ", "BW"), (cloudlet: Cloudlet) =>  df.format(cloudlet.getFileSize*COST_PER_BW*100))
        .addColumn(new TextTableColumn("   Cost   ", "Storage"), (cloudlet: Cloudlet) =>  df.format(cloudlet.getActualCpuTime*cloudlet.getFileSize*COST_PER_STORAGE*100))
        .addColumn(new TextTableColumn("   Cost   ", "Memory"), (cloudlet: Cloudlet) =>  df.format(cloudlet.getFileSize*cloudlet.getActualCpuTime*COST_PER_MEMORY*100))
        .addColumn(new TextTableColumn("   Cost   ", "Total"), (cloudlet: Cloudlet) =>  df.format(cloudlet.getActualCpuTime*COST_PER_SECOND*100
          + cloudlet.getFileSize*COST_PER_BW*100
          + cloudlet.getActualCpuTime*cloudlet.getFileSize*COST_PER_STORAGE*100
          + cloudlet.getFileSize*cloudlet.getActualCpuTime*COST_PER_MEMORY*100)).setTitle(broker.getName).build()
    }

    printResults(broker0,config.getDouble("COST_PER_SECOND"),config.getDouble("COST_PER_MEMORY"),config.getDouble("COST_PER_STORAGE"),config.getDouble("COST_PER_BW"))

