package com.sev7e0.data

case class Topic(timestamp: String, className: String, methodName: String, topicValue: String)

object Topic {
  def apply(timestamp: String, className: String, methodName: String, topicValue: String): Topic = new Topic(timestamp, className, methodName, topicValue)
}