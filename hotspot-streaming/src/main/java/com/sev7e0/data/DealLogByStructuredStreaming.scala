package com.sev7e0.data

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._
import org.apache.spark.sql.streaming.{OutputMode, Trigger}
import org.apache.spark.sql.types.{StringType, StructField, StructType}

/**
  * 日志流处理
  */
object DealLogByStructuredStreaming {
  val MASTER = "local"
  val SERVERLIST = "localhost:9092"
  val KAFKA = "kafka"
  val GROUP = "group-1"

  var url = "localhost:2181"
  var timeout = 2000
  var root = "hotspot"


  def initProp(): Unit = {
    url = Option.apply(System.getProperty("url")).getOrElse(url)
    //    timeout = Int(Option.apply(System.getProperty("timeout")).getOrElse(timeout))
    root = Option.apply(System.getProperty("root")).getOrElse(root)
  }

  def main(args: Array[String]): Unit = {

    //    initProp()

    val windowSize = 60
    val slideSize = 60
    if (slideSize > windowSize) {
      System.err.println("<滑动间隔> 必须要小于或等于 <窗口间隔>")
    }

    val windowDuration = s"$windowSize seconds"
    val slideDuration = s"$slideSize seconds"


    val spark = SparkSession.builder()
      .appName(DealLogByStructuredStreaming.getClass.getName)
      .master("local")
      .getOrCreate()

    val logDF = spark.readStream
      .format(KAFKA)
      .option("kafka.bootstrap.servers", SERVERLIST)
      .option(ConsumerConfig.GROUP_ID_CONFIG, GROUP)
      .option("subscribe", "Topic")
      .load()

    import spark.implicits._
    val schema = StructType(Seq(
      StructField("className", StringType, true),
      StructField("methodName", StringType, true),
      StructField("value", StringType, true)
    ))
    val lines = logDF.selectExpr("cast (value as string) as json", "cast (timestamp as timestamp)")
      .select(from_json($"json", schema = schema).as("hot"), $"timestamp".as("timestamp"))
      .toDF()

    val query = lines.withWatermark("timestamp", "1 minutes")
      .groupBy(window($"timestamp", windowDuration, slideDuration), $"hot.className", $"hot.methodName", $"hot.value")
      .count()
      .orderBy("count")
      .select($"timestamp",$"className", $"methodName", $"value", $"count")
      .where($"count" > 50)
      .writeStream
      // 不截断日志
      .option("truncate", "false")
      .outputMode(OutputMode.Complete())
      .trigger(Trigger.ProcessingTime("5 seconds"))
      .foreach(ZooKeeperSink )
      .format("console")
      .start()

    println(query.lastProgress)
    query.awaitTermination()

  }
}
