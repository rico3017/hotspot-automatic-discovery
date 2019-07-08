package com.sev7e0.data

import java.util.concurrent.CountDownLatch

import org.apache.spark.sql.{ForeachWriter, Row}
import org.apache.zookeeper.ZooKeeper
import org.slf4j.LoggerFactory

class ZooKeeperSink(url: String, timeout: Int, path: String) extends ForeachWriter[Row] {

  protected lazy val logger = LoggerFactory.getLogger(getClass.getName)


  var zk: ZooKeeper = _

  val countDownLatch: CountDownLatch = new CountDownLatch(1);

  override def open(partitionId: Long, epochId: Long): Boolean = {

    zk = new ZooKeeper(url, timeout, event => {
      logger.info("链接状态更改----{}", event.getState())
      countDownLatch.countDown()
    })
    countDownLatch.await()
    true
  }

  /**
    * create zookeeper path
    *
    * @param value
    */
  override def process(value: Row): Unit = {
    logger.info(value.toString())
    //    zk.create(path, Array.apply(value.getByte(0)), OPEN_ACL_UNSAFE, EPHEMERAL)
  }

  override def close(errorOrNull: Throwable): Unit = {
    zk.close()
  }
}
