#!/usr/bin/env bash

KAFKA_HOST=${KAFKA_HOST:-kafka.middleware}
ZK_HOST=${ZK_HOST:-zookeeper.middleware}


function listKefTopics {
    bin/kafka-topics.sh --list --zookeeper ${ZK_HOST}:2181 | grep "kef*"
}

function setKefRetentionTime {

    firstYearEiTopic=$(bin/kafka-topics.sh --list --zookeeper ${ZK_HOST}:2181 | grep -m1 "kefcharts-ei-*")
    firstYearEiIdx=${firstYearEiTopic: -1}

    lastYearEiTopic=$(bin/kafka-topics.sh --list --zookeeper ${ZK_HOST}:2181 | grep "kefcharts-ei-*" | tail -1)
    lastYearEiIdx=${lastYearEiTopic: -1}

    for i in $(eval echo "{$firstYearEiIdx..$lastYearEiIdx}")
    do
      topicbeeren=kefcharts-ei-beeren-201$i
      topicfunde=kefcharts-ei-funde-201$i

      bin/kafka-configs.sh --zookeeper ${ZK_HOST}:2181 --alter --entity-type topics --entity-name $topicbeeren --add-config retention.ms=1000
      bin/kafka-configs.sh --zookeeper ${ZK_HOST}:2181 --alter --entity-type topics --entity-name $topicfunde --add-config retention.ms=1000
    done

    firstYearFFTopic=$(bin/kafka-topics.sh --list --zookeeper ${ZK_HOST}:2181 | grep -m1 "kefcharts-ff-*")
    firstYearFFIdx=${firstYearFFTopic: -1}

    lastYearFFTopic=$(bin/kafka-topics.sh --list --zookeeper ${ZK_HOST}:2181 | grep "kefcharts-ff-*" | tail -1)
    lastYearFFIdx=${lastYearFFTopic: -1}

    for i in $(eval echo "{$firstYearFFIdx..$lastYearFFIdx}")
    do
      topicff=kefcharts-ff-201$i

      bin/kafka-configs.sh --zookeeper ${ZK_HOST}:2181 --alter --entity-type topics --entity-name $topicff --add-config retention.ms=1000
    done

    bin/kafka-configs.sh --zookeeper ${ZK_HOST}:2181 --alter --entity-type topics --entity-name kefmaps --add-config retention.ms=1000
}

function resetKefRetentionTime {

    firstYearEiTopic=$(bin/kafka-topics.sh --list --zookeeper ${ZK_HOST}:2181 | grep -m1 "kefcharts-ei-*")
    firstYearEiIdx=${firstYearEiTopic: -1}

    lastYearEiTopic=$(bin/kafka-topics.sh --list --zookeeper ${ZK_HOST}:2181 | grep "kefcharts-ei-*" | tail -1)
    lastYearEiIdx=${lastYearEiTopic: -1}

    for i in $(eval echo "{$firstYearEiIdx..$lastYearEiIdx}")
    do
      topicbeeren=kefcharts-ei-beeren-201$i
      topicfunde=kefcharts-ei-funde-201$i
    
      bin/kafka-configs.sh --zookeeper ${ZK_HOST}:2181 --alter --entity-type topics --entity-name $topicbeeren --add-config retention.ms=86400000
      bin/kafka-configs.sh --zookeeper ${ZK_HOST}:2181 --alter --entity-type topics --entity-name $topicfunde --add-config retention.ms=86400000
    done
    
    firstYearFFTopic=$(bin/kafka-topics.sh --list --zookeeper ${ZK_HOST}:2181 | grep -m1 "kefcharts-ff-*")
    firstYearFFIdx=${firstYearFFTopic: -1}

    lastYearFFTopic=$(bin/kafka-topics.sh --list --zookeeper ${ZK_HOST}:2181 | grep "kefcharts-ff-*" | tail -1)
    lastYearFFIdx=${lastYearFFTopic: -1}

    for i in $(eval echo "{$firstYearFFIdx..$lastYearFFIdx}")
    do
      topicff=kefcharts-ff-201$i
    
      bin/kafka-configs.sh --zookeeper ${ZK_HOST}:2181 --alter --entity-type topics --entity-name $topicff --add-config retention.ms=86400000
    done
    
    bin/kafka-configs.sh --zookeeper ${ZK_HOST}:2181 --alter --entity-type topics --entity-name kefmaps --add-config retention.ms=86400000
}

function deleteKefTopics {

    firstYearEiTopic=$(bin/kafka-topics.sh --list --zookeeper ${ZK_HOST}:2181 | grep -m1 "kefcharts-ei-*")
    firstYearEiIdx=${firstYearEiTopic: -1}

    lastYearEiTopic=$(bin/kafka-topics.sh --list --zookeeper ${ZK_HOST}:2181 | grep "kefcharts-ei-*" | tail -1)
    lastYearEiIdx=${lastYearEiTopic: -1}

    for i in $(eval echo "{$firstYearEiIdx..$lastYearEiIdx}")
    do
      topicbeeren=kefcharts-ei-beeren-201$i
      topicfunde=kefcharts-ei-funde-201$i

      bin/kafka-topics.sh --zookeeper ${ZK_HOST}:2181 --delete --topic $topicbeeren
      bin/kafka-topics.sh --zookeeper ${ZK_HOST}:2181 --delete --topic $topicfunde
    done

    firstYearFFTopic=$(bin/kafka-topics.sh --list --zookeeper ${ZK_HOST}:2181 | grep -m1 "kefcharts-ff-*")
    firstYearFFIdx=${firstYearFFTopic: -1}

    lastYearFFTopic=$(bin/kafka-topics.sh --list --zookeeper ${ZK_HOST}:2181 | grep "kefcharts-ff-*" | tail -1)
    lastYearFFIdx=${lastYearFFTopic: -1}

    for i in $(eval echo "{$firstYearFFIdx..$lastYearFFIdx}")
    do
      topicff=kefcharts-ff-201$i

      bin/kafka-topics.sh --zookeeper ${ZK_HOST}:2181 --delete --topic $topicff
    done

    bin/kafka-topics.sh --zookeeper ${ZK_HOST}:2181 --delete --topic kefmaps
}

if [[ $1 == "set-kef-retention" ]]; then
    setKefRetentionTime
elif [[ $1 == "reset-kef-retention" ]]; then
    resetKefRetentionTime
elif [[ $1 == "list-kef-topics" ]]; then
    listKefTopics
elif [[ $1 == "delete-kef-topics" ]]; then
    deleteKefTopics
else
    echo "use one of the following args: set-kef-retention | reset-kef-retention | list-kef-topics | delete-kef-topics"
fi