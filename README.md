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

![流程图](https://github.com/sev7e0/hotspot-automatic-discovery/blob/spring/%E6%B5%81%E7%A8%8B%E5%9B%BE.png "流程图")


## 环境配置

## 简单总结