# 基于 Structured Streaming 的缓存热点数据自动发现

## 介绍

在实时的系统访问次数统计中，单位时间内某条数据超过一定访问量，那么将这条数据判定为热点数据，在分布式集群中的每台机器上进行热点数据的本地缓存。

## 架构

| backend-service  |  hotspot-streaming | internal-cache  |
|---|---|---|
|  Spring Boot | Structured Streaming  |  Spring Boot |
|  Redis | Kafka  | Kafka  |
|  Kafka |  ZooKeeper |   |
|  Mysql |   |   |

![流程图](https://github.com/sev7e0/hotspot-automatic-discovery/blob/spring/flow%20chart.png "流程图")


## 环境

```shell
git clone https://github.com/sev7e0/hotspot-automatic-discovery.git

mvn install -DskipTests
```
0. 导入`weibo.sql`。

1. `backend-service`中配置 Kafka 相关配置，ZooKeeper 相关配置，Mysql 相关配置
启动`InternalCacheApplication`。

2. `hotspot-streaming`中配置 Kafka 相关配置，ZooKeeper 相关配置，启动
`DealLogByStructuredStreaming$`。

3. 通过`backend-service`中`TopicController`路径进行访问（Postman、或其他 Http 客户端工具）。