FROM biggis/kafka:0.10.1.1

MAINTAINER wipatrick

ENV KAFKA_HOST=kafka.middleware
ENV ZK_HOST=zookeeper.middleware
ENV TIMEZONE=Europe/Berlin

COPY start.sh /opt/kafka

CMD ["/opt/kafka/start.sh"]