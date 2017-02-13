package net.disy.biggis.kef.routes.maps;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.component.kafka.KafkaConstants;
import org.apache.camel.impl.converter.ToStringTypeConverter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
@SuppressWarnings("nls")
public class AddKafkaMessageHeadersProcessorTest {

  private static final String FILE_NAME_WITH_PATH = "/path/to/file.json";

  @Mock(answer = Answers.RETURNS_DEEP_STUBS)
  private Exchange exchange;

  @Mock
  private Message in;

  @Captor
  private ArgumentCaptor<String> captor;

  @Test
  public void correctTopicName() throws Exception {
    when(exchange.getIn()).thenReturn(in);
    when(exchange.getContext().getTypeConverter()).thenReturn(new ToStringTypeConverter());
    when(in.getHeader(Exchange.FILE_NAME_ONLY, String.class)).thenReturn("file.json");
    when(in.getHeader(Exchange.FILE_NAME, String.class)).thenReturn(getFileName());
    AddKafkaMessageHeadersProcessor processor = new AddKafkaMessageHeadersProcessor("topic");

    processor.process(exchange);

    verify(in).setHeader(eq(KafkaConstants.TOPIC), captor.capture());
    assertThat(captor.getValue(), is("topic-path-to"));
  }

  private String getFileName() {
    return FILE_NAME_WITH_PATH.replace("/", File.separator);
  }
}
