./kafka-topics.sh --zookeeper localhost:2181  --describe --topic click-data

./kafka-topics.sh --zookeeper localhost:2181  --alter --topic dsp-data  --partitions 10

./kafka-consumer-groups.sh --new-consumer --bootstrap-server localhost:9092 --group data-normal --describe

./kafka-topics.sh --zookeeper localhost:2181 --topic  dsp-data-error --create  --partitions 10  --replication-factor 1

./kafka-topics.sh --zookeeper localhost:2181  --alter --topic dsp-data  --partitions 20

./kafka-topics.sh --zookeeper localhost:2181 --topic  dsp-data-new --create  --partitions 9  --replication-factor 2

./kafka-topics.sh --zookeeper localhost:2181  --alter --topic dsp-data-new  --partitions 30

./kafka-topics.sh --zookeeper localhost:2181 --topic __consumer_offsets --describe


bin/kafka-reassign-partitions.sh --zookeeper localhost:2181 --reassignment-json-file increase-replication-factor-2.json --execute

./kafka-topics.sh --zookeeper localhost:2181  --alter --topic dsp-data-error  --partitions 16

./kafka-topics.sh --zookeeper localhost:2181 --topic  dsp-data-copy-error --create  --partitions 16  --replication-factor 2
