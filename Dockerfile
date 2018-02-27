FROM anapsix/alpine-java

MAINTAINER wipatrick

ADD ./target/kef-import-kafka-0.1-SNAPSHOT.jar  /kef-import-kafka-0.1-SNAPSHOT.jar

CMD ["java", "-jar", "/kef-import-kafka-0.1-SNAPSHOT.jar"]