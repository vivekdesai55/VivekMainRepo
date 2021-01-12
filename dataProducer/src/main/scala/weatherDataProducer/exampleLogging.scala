package weatherDataProducer

import org.slf4j.LoggerFactory

object exampleLogging extends App {

  val logger = LoggerFactory.getLogger(getClass.getName)
  println(logger.isInfoEnabled())
  logger.info("I am a log message")
  logger.debug("I am a log message")
  logger.error("I am a log message")
  
  println("Hello, World!")
}