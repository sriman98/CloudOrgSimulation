import HelperUtils.{CreateLogger, ObtainConfigReference}
import Simulations.{DataCenter1, DataCenter2, DataCenter3}
import com.typesafe.config.ConfigFactory
import org.slf4j.LoggerFactory

object Simulation:
  val logger = CreateLogger(classOf[Simulation])

  @main def runSimulation =
    logger.info("Constructing a cloud model...")
    DataCenter1.Start()
    DataCenter2.Start()
    DataCenter3.Start()
    logger.info("Finished cloud simulation...")

class Simulation