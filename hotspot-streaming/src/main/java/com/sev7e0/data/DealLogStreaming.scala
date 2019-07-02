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
  val serverList = "localhost:9092"
  val kafka = "kafka"
  val group = "group-1"



  def main(args: Array[String]): Unit = {

    val windowSize = 60
    val slideSize = 60
    if (slideSize > windowSize) {
      System.err.println("<滑动间隔> 必须要小于或等于 <窗口间隔>")
    }

    val windowDuration = s"$windowSize seconds"
    val slideDuration = s"$slideSize seconds"


    val spark = SparkSession.builder()
      .appName(DealLogStreaming.getClass.getName)
      .master("local")
      .getOrCreate()

    val logDF = spark.readStream
      .format(kafka)
      .option("kafka.bootstrap.servers", serverList)
      .option(ConsumerConfig.GROUP_ID_CONFIG, group)
      .option("subscribe", "hotspot-test")
      .load()


    import spark.implicits._

    val lines = logDF.selectExpr("CAST(value AS STRING)",
      "CAST(topic as STRING)",
      "CAST(partition as INTEGER)")
      .as[(String, String, Integer)]


    val df = lines.map(line => {
      val strings = line._1.split("=")
      Topic(strings(0), strings(1), strings(2), strings(3))
    })
      .toDF()


    val ds = df.select($"timestamp", $"className", $"methodName", $"topicValue").as[Topic]

    val query = ds.groupBy(window($"timestamp", windowDuration, slideDuration),$"topicValue")
      .count()
      .orderBy("window","count")
      .writeStream
      .outputMode(OutputMode.Complete())
      .trigger(Trigger.Continuous("1 seconds"))
      .format("console")
      .start()


    query.awaitTermination()

  }
}
