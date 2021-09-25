package Simulations

import HelperUtils.ObtainConfigReference
import com.typesafe.config.ConfigFactory
import org.cloudbus.cloudsim.core.CloudSim
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import org.junit.{Assert, Test}
import org.junit.Assert.{assertEquals, assertNotNull, assertTrue}
import Simulations.{DataCenter1,DataCenter2,DataCenter3}


class SimulationTest {

  val config = ConfigFactory.load("application.conf")
  val Simulator = new DataCenter1()
  val Simulator2 = new DataCenter2()
  val Simulator3 = new DataCenter3()

  @Test
  def testCheckConfig = {
    assertNotNull(config)
  }

  @Test
  def testHostCount = {
    //assertEquals(Simulator.hostList.size(), config.getInt("DATA_CENTER1.HOSTS"))
    //assertEquals(Simulator2.hostList.size(), config.getInt("DATA_CENTER2.HOSTS"))
   // assertEquals(Simulator3.hostList.size(), config.getInt("DATA_CENTER3.HOSTS"))
  }

  @Test
  def testVmCount = {
    //assertEquals(Simulator.vmList.size(), config.getInt("VMS"))
  }

  @Test
  def testCloudletsCount = {
    //assertEquals(Simulator.cloudletList.size(), config.getInt("CURRENT_CLOUDLETS"))
  }

}