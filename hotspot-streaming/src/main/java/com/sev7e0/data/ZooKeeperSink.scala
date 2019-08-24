package com.sev7e0.data

import java.util.StringJoiner

import org.apache.curator.framework.{CuratorFramework, CuratorFrameworkFactory}
import org.apache.curator.retry.ExponentialBackoffRetry
import org.apache.spark.sql.{ForeachWriter, Row}
import org.apache.zookeeper.CreateMode
import org.slf4j.LoggerFactory

class ZooKeeperSink(url: String, timeout: Int, path: String) extends ForeachWriter[Row] {

  protected lazy val logger = LoggerFactory.getLogger(getClass.getName)

  val PARENTNODE = "/rootNode/"

  private var client: CuratorFramework = _

  /**
    * get client
    *
    * @return
    */
  override def open(partitionId: Long, epochId: Long): Boolean = {
    val exponentialBackoffRetry = new ExponentialBackoffRetry(timeout, 3)
    client = CuratorFrameworkFactory.newClient(url, exponentialBackoffRetry)
    client.start()
    true
  }

  /**
    * create zookeeper path
    *
    * @param value
    */
  override def process(value: Row): Unit = {
    val joiner = new StringJoiner("/", PARENTNODE, "")
    joiner.add(String.valueOf(value.get(0)))
    logger.info("zk path：{}", joiner.toString)
    logger.info("value：{}", value.toString)
    client.create()
      .withMode(CreateMode.EPHEMERAL_SEQUENTIAL)
      .forPath(joiner.toString, value.toString().getBytes)
  }

  override def close(errorOrNull: Throwable): Unit = {
    client.close()
  }


}

object ZooKeeperSink{
  var a = new ZooKeeperSink("url", 4, "1")
  def main(args: Array[String]): Unit = {
    val value = ("a", "b", "d")
    val joiner = new StringJoiner("/", a.PARENTNODE, "")
    joiner.add(value._1)
    joiner.add(value._2)
    joiner.add(value._3)
    println(joiner.toString)

  }
}