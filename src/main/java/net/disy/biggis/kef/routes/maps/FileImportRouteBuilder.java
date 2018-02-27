// Copyright (c) 2016 by Disy Informationssysteme GmbH
package net.disy.biggis.kef.routes.maps;

import static org.apache.camel.LoggingLevel.INFO;
import static org.apache.camel.builder.ExpressionBuilder.fileNameExpression;
import static org.apache.camel.processor.idempotent.MemoryIdempotentRepository.memoryIdempotentRepository;

import java.nio.charset.StandardCharsets;

import org.apache.camel.spring.SpringRouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import net.disy.biggis.kef.routes.base.ShutdownService;
import net.disy.biggis.kef.routes.base.StopRouteBuilder;

// NOT_PUBLISHED
@Component
public class FileImportRouteBuilder extends SpringRouteBuilder {

  private static final String ROUTE_ID = "read files"; //$NON-NLS-1$

  @Value("${kafka-route-config}")
  private String kafkaRouteConfiguration;

  @Value("${kafka-topic-prefix}")
  private String kafkaTopicPrefix;

  @Value("${import-data-folder}")
  private String importFolder;

  @Autowired
  private ShutdownService shutdownService;

  @Override
  public void configure() throws Exception {

    /*
      Overwrite variable
    */
    String kafkaHost = System.getenv("KAFKA_HOST");
    if ( kafkaHost != null && !kafkaHost.isEmpty())
      kafkaRouteConfiguration = "kafka:" + kafkaHost + ":9092?clientId=kef-import";

    shutdownService.registerRoute(ROUTE_ID);
    // @formatter:off
    from("file:" + importFolder + "?recursive=true&noop=true&idempotent=true&sendEmptyMessageWhenIdle=true&delay=5000") //$NON-NLS-1$
      .idempotentConsumer(fileNameExpression(), memoryIdempotentRepository(20000))
      .routeId(ROUTE_ID)
      .choice()
        .when(body().isNull())
          .log(INFO, "No more files.") //$NON-NLS-1$
          .setBody(simple(ROUTE_ID))
          .to(StopRouteBuilder.ROUTE_URI)
        .otherwise()
          .log(INFO, "Processing file ${header[CamelFileName]}") //$NON-NLS-1$
          .process(new AddKafkaMessageHeadersProcessor(kafkaTopicPrefix))
          .convertBodyTo(String.class, StandardCharsets.UTF_8.name())
          .log(INFO, "Sending message to topic ${header[kafka.TOPIC]} with key ${header[kafka.CONTENT_TYPE]}")
          .to(kafkaRouteConfiguration);
    // @formatter:on
  }

}
