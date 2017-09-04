package com.advdsp.monitor.kafka.offset

import java.text.SimpleDateFormat
import java.util.Properties

import kafka.admin.AdminClient
import kafka.common.{TopicAndPartition}
import org.apache.kafka.clients.CommonClientConfigs
import org.apache.kafka.clients.consumer.{ConsumerConfig, KafkaConsumer}
import org.apache.kafka.common.TopicPartition
import org.apache.kafka.common.serialization.StringDeserializer

import scala.collection.JavaConverters._

/**
  */
class ConsumerGroupMonitor(val group_name: String, val bootstrapServers: String) {

  private val adminClient = createAdminClient();
  private var consumer: KafkaConsumer[String, String] = null

  var groupInformationCollect = new GroupInformationCollect

  private def createAdminClient(): AdminClient = {
    val props = new Properties()
    props.put(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers)
    AdminClient.create(props)
  }

  protected def describeGroup(group: String) {
    val consumerSummaries = adminClient.describeConsumerGroup(group)
    if (consumerSummaries.isEmpty)
      println(s"Consumer group `${group}` does not exist or is rebalancing.")
    else {
      val consumer = getConsumer()
      printDescribeHeader()
      consumerSummaries.foreach { consumerSummary =>
        val topicPartitions = consumerSummary.assignment.map(tp => TopicAndPartition(tp.topic, tp.partition))
        val partitionOffsets = topicPartitions.flatMap { topicPartition =>
          Option(consumer.committed(new TopicPartition(topicPartition.topic, topicPartition.partition))).map { offsetAndMetadata =>
            topicPartition -> offsetAndMetadata.offset
          }
        }.toMap
        describeTopicPartition(group, topicPartitions, partitionOffsets.get,
          _ => Some(s"${consumerSummary.clientId}_${consumerSummary.clientHost}"))
      }
    }
  }

  protected def describeTopicPartition(group: String,
                                       topicPartitions: Seq[TopicAndPartition],
                                       getPartitionOffset: TopicAndPartition => Option[Long],
                                       getOwner: TopicAndPartition => Option[String]): Unit = {
    topicPartitions
      .sortBy { case topicPartition => topicPartition.partition }
      .foreach { topicPartition =>
        describePartition(group, topicPartition.topic, topicPartition.partition, getPartitionOffset(topicPartition),
          getOwner(topicPartition))
      }
  }


  /**
    * 替换原来的
    *
    * @param group
    * @param topic
    * @param partition
    * @param offsetOpt
    * @param ownerOpt
    */
  private def describePartition(group: String,
                                topic: String,
                                partition: Int,
                                offsetOpt: Option[Long],
                                ownerOpt: Option[String]) {
    def collect(logEndOffset: Option[Long]): Unit = {
      val lag = offsetOpt.filter(_ != -1).flatMap(offset => logEndOffset.map(_ - offset))
      // println("%-30s %-30s %-10s %-15s %-15s %-15s %s".format(group, topic, partition, offsetOpt.getOrElse("unknown"), logEndOffset.getOrElse("unknown"), lag.getOrElse("unknown"), ownerOpt.getOrElse("none")))
      if (null == logEndOffset) {
        // miss
      }
      else {
        var groupInformation = new GroupInformation
        groupInformation.setGroup_name(group)
        groupInformation.setTopic_name(topic)
        groupInformation.setPartition_num(partition)
        //groupInformation.setCurrent_offset(offsetOpt.get)
        //groupInformation.setLog_end_offset(logEndOffset.get)
        groupInformation.setLag(lag.get)
        groupInformationCollect.collectPartitionInfo(groupInformation)
      }
    }

    getLogEndOffset(topic, partition) match {
      case LogEndOffsetResult.LogEndOffset(logEndOffset) => collect(Some(logEndOffset))
      case LogEndOffsetResult.Unknown => collect(None)
      case LogEndOffsetResult.Ignore =>
    }
  }

  protected def printDescribeHeader() {
    println("%-30s %-30s %-10s %-15s %-15s %-15s %s".format("GROUP", "TOPIC", "PARTITION", "CURRENT-OFFSET", "LOG-END-OFFSET", "LAG", "OWNER"))
  }


  sealed trait LogEndOffsetResult

  object LogEndOffsetResult {

    case class LogEndOffset(value: Long) extends LogEndOffsetResult

    case object Unknown extends LogEndOffsetResult

    case object Ignore extends LogEndOffsetResult

  }


  /**
    * kafka group
    *
    * @param topic
    * @param partition
    * @return
    */
  protected def getLogEndOffset(topic: String, partition: Int): LogEndOffsetResult = {
    val consumer = getConsumer()
    val topicPartition = new TopicPartition(topic, partition)
    consumer.assign(List(topicPartition).asJava)
    consumer.seekToEnd(List(topicPartition).asJava)
    val logEndOffset = consumer.position(topicPartition)
    LogEndOffsetResult.LogEndOffset(logEndOffset)
  }


  private def getConsumer() = {
    if (consumer == null)
      consumer = createNewConsumer()
    consumer
  }

  def close(): Unit = {
    if (null != consumer) {
      consumer.close();
    }
  }

  def init(): Unit = {
    groupInformationCollect = new GroupInformationCollect();
  }

  private def createNewConsumer(): KafkaConsumer[String, String] = {
    val properties = new Properties()
    val deserializer = (new StringDeserializer).getClass.getName
    properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers)
    properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false")
    properties.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "30000")
    properties.put(ConsumerConfig.GROUP_ID_CONFIG, group_name)
    properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, deserializer)
    properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, deserializer)
    new KafkaConsumer(properties)
  }

}

object ConsumerGroupMonitor {
  def main(args: Array[String]): Unit = {
    val consumerGroupMonitor = new ConsumerGroupMonitor("data-normal", "localhost:9092")
    consumerGroupMonitor.describeGroup("data-normal")

    var groupInformationCollect = consumerGroupMonitor.groupInformationCollect;
    println(groupInformationCollect)


    //SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
  }
}
