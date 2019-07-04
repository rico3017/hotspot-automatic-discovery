package com.sev7e0.data

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions.window
import org.apache.spark.sql.streaming.{OutputMode, Trigger}

/**
  * 日志流处理
  */
object DealLogStreaming {
  val master = "local"
  var serverList = "localhost:9092"
  val kafka = "kafka"
  val group = "group-1"


  /**
    * 入口主函数
    *
    * @param args The order of parameters is serverList windowSize slideSize (windowSize slideSize can use default)
    */

  def main(args: Array[String]): Unit = {
    var windowSize = 600
    var slideSize = 300

    /**
      * check args
      */
    args match {
      case args if args.length > 3 || args.length < 1 => {
        System.err.println("main method parameters error!")
      }
      case args if args.length == 3 => {
        windowSize = args(1).toInt
        slideSize = args(2).toInt
        if (slideSize > windowSize) {
          System.err.println("Sliding interval must be less than or equal to window interval.")
        }
        serverList = args(0)
      }
      case args if args.length < 3 => {
        serverList = args(0)
      }
    }

    val windowDuration = s"$windowSize seconds"
    val slideDuration = s"$slideSize seconds"

    val spark = SparkSession.builder()
      .appName(DealLogStreaming.getClass.getName)
      .master(master)
      .getOrCreate()

    val logDF = spark.readStream
      .format(kafka)
      .option("kafka.bootstrap.servers", serverList)
      .option(ConsumerConfig.GROUP_ID_CONFIG, group)
      .option("subscribe", "hotspot-test")
      .load()


    import spark.implicits._

    val lines = logDF.selectExpr("CAST(value AS STRING)", "CAST(topic as STRING)", "CAST(partition as INTEGER)")
      .as[(String, String, Integer)]


    //    import org.apache.spark.sql.functions.to_timestamp
    val df = lines.map(line => {
      val strings = line._1.split("=")
      Topic(strings(0), strings(1), strings(2), strings(3))
    }).toDF()

    //    val dt = to_timestamp($"timestamp", "MM/dd/yyyy HH:mm:ss")


    val ds = df.select($"timestamp", $"className", $"methodName", $"topicValue")

    val query = ds.groupBy(window($"timestamp", windowDuration, slideDuration), $"topicValue")
      .count()
      .orderBy("count")
      .writeStream
      .outputMode(OutputMode.Complete())
      .trigger(Trigger.ProcessingTime("1 seconds"))
      .format("console")
      .start()


    query.awaitTermination()

  }
}
