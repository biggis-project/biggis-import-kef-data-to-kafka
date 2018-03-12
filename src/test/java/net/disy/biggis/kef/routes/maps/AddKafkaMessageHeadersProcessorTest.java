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
  private Exchange exchange1;

  @Mock(answer = Answers.RETURNS_DEEP_STUBS)
  private Exchange exchange2;

  @Mock
  private Message in1;

  @Mock
  private Message in2;

  @Captor
  private ArgumentCaptor<String> captor1;

  @Captor
  private ArgumentCaptor<String> captor2;

  @Test
  public void correctTopicName() throws Exception {

    when(exchange1.getIn()).thenReturn(in1);
    when(exchange1.getContext().getTypeConverter()).thenReturn(new ToStringTypeConverter());
    when(in1.getHeader(Exchange.FILE_NAME_ONLY, String.class)).thenReturn("file.json");
    when(in1.getHeader(Exchange.FILE_NAME, String.class)).thenReturn(getFileName());
    AddKafkaMessageHeadersProcessor processor1 = new AddKafkaMessageHeadersProcessor("topic");

    processor1.process(exchange1);

    verify(in1).setHeader(eq(KafkaConstants.TOPIC), captor1.capture());
    assertThat(captor1.getValue(), is("topic-path-to"));

//    when(exchange1.getIn()).thenReturn(in1);
//    when(exchange1.getContext().getTypeConverter()).thenReturn(new ToStringTypeConverter());
//    when(in1.getHeader(Exchange.FILE_NAME_ONLY, String.class)).thenReturn("file.json");
//    when(in1.getHeader(Exchange.FILE_NAME, String.class)).thenReturn(getFileName());
//    AddKafkaMessageHeadersProcessor processor1 = new AddKafkaMessageHeadersProcessor("topic","maps");
//
//    processor1.process(exchange1);
//
//    verify(in1).setHeader(eq(KafkaConstants.TOPIC), captor1.capture());
//    assertThat(captor1.getValue(), is("topic-path-to"));
//
//    when(exchange2.getIn()).thenReturn(in2);
//    when(exchange2.getContext().getTypeConverter()).thenReturn(new ToStringTypeConverter());
//    when(in2.getHeader(Exchange.FILE_NAME_ONLY, String.class)).thenReturn("file.json");
//    when(in2.getHeader(Exchange.FILE_NAME, String.class)).thenReturn(getFileName());
//    AddKafkaMessageHeadersProcessor processor2 = new AddKafkaMessageHeadersProcessor("topic","charts");
//
//    processor1.process(exchange1);
//
//    verify(in2).setHeader(eq(KafkaConstants.TOPIC), captor2.capture());
//    assertThat(captor2.getValue(), is("topic-path-to"));
  }

  private String getFileName() {
    return FILE_NAME_WITH_PATH.replace("/", File.separator);
  }
}
