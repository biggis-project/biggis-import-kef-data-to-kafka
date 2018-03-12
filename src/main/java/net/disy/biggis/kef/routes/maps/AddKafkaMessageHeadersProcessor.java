//Copyright (c) 2016 by Disy Informationssysteme GmbH
package net.disy.biggis.kef.routes.maps;

import static org.apache.camel.builder.ExpressionBuilder.fileNameExpression;
import static org.apache.camel.builder.ExpressionBuilder.fileOnlyNameExpression;

import java.io.File;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.component.kafka.KafkaConstants;

// NOT_PUBLISHED
public class AddKafkaMessageHeadersProcessor implements Processor {

  private static final String TOPIC_DELIMITER = "-"; //$NON-NLS-1$

  private String topicPrefix;

  public AddKafkaMessageHeadersProcessor(String topicPrefix) {
    this.topicPrefix = topicPrefix;
  }

  @Override
  public void process(Exchange exchange) throws Exception {
    exchange.getIn().setHeader(KafkaConstants.KEY, getFileName(exchange));
    exchange.getIn().setHeader(KafkaConstants.TOPIC, createTopicName(exchange));
  }

  private String getFileName(Exchange exchange) {
    return fileNameExpression().evaluate(exchange, String.class);
  }

  private String createTopicName(Exchange exchange) {
    String fileOnlyName = fileOnlyNameExpression().evaluate(exchange, String.class);

    String pattern = Pattern.quote(File.separator);

    String topicname = Stream
            .of(getFileName(exchange).split(pattern))
            .filter(s -> !s.equalsIgnoreCase(fileOnlyName))
            .collect(Collectors.joining(TOPIC_DELIMITER, topicPrefix, ""));

    return topicname;

  }

}
