package weatherDataProducer

import org.slf4j.LoggerFactory
import org.apache.logging.log4j.core.config.Configurator

object produceWeatherData extends App {
  
   val logger = LoggerFactory.getLogger(getClass().getName())
   
   //val environment = System.getProperty("application.environment")
   Configurator.initialize(null, "src/main/resources/log4j2.properties")
   
 
  //API URL TO PULL WEATHER DATA
   val url = "https://api.openweathermap.org/data/2.5/onecall?lat=12.9716&lon=77.5946&exclude=minutely,hourly,daily,alerts&appid=7f9e75dbfba0b07fe2e4e79fc4457342&units=metrics"
  logger.info("APIurl:" + url)
  //Data which needs to extracted from the json output (API output)
  val dataList: List[String] =  List("lat","lon","timezone","dt","sunrise","sunset","temp","humidity","dew_point","visibility","wind_speed")
  logger.info("APIRequiredData:" + dataList.mkString(","))
  
  //Pull data for every 10 seconds
  val timeMS: Int = 10000
  logger.info("APIDataPollTime:" + timeMS + "ms")
  
  //Kafka confiuration details
  val server: String = "localhost"
  val port: Int = 9092
  val zookeeperPort: Int = 2181
  val topicName: String = "WeatherData"
  logger.info("KafkaServer:" + server)
  logger.info("KafkaPort:" + port)
  
  
  val produceData: weatherDataProducer = new weatherDataProducer(url, dataList)
   
  
  try{
    
      if(produceData.isTopicExists(server, zookeeperPort, topicName)){
          println("topicName:" + topicName + "Exists")
          logger.info("topicName:" + topicName + "Exists")
          val Flag: Boolean = true
          while (Flag) {
            produceData.writeToKafka(topicName, server, port)
            logger.info("API Poll Completeed and JsonData is put on KafkaTopic")
            logger.info("SleepFor:" + timeMS)
            Thread.sleep(timeMS)
          }
 
      }else{
          println("topicName:" + topicName + "Does nto Exists")
          logger.error("topicName:" + topicName + "Does not Exists")
      }
    
  }catch {
      case ex: Throwable => { logger.error(ex.getStackTrace.mkString("\n")) }
  }
   
}

