//Copyright (c) 2016 by Disy Informationssysteme GmbH
package net.disy.biggis.kef.routes.base;

import static org.apache.camel.LoggingLevel.INFO;

import org.apache.camel.Exchange;
import org.apache.camel.spring.SpringRouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

// NOT_PUBLISHED
@Component
public class StopRouteBuilder extends SpringRouteBuilder {

  public static final String ROUTE_URI = "seda:stop-route"; //$NON-NLS-1$
  private static final String STOP_ROUTES_ID = "stop routes"; //$NON-NLS-1$

  private static final Logger LOG = LoggerFactory.getLogger(StopRouteBuilder.class);

  @Autowired
  private ShutdownService shutdownService;

  @Override
  public void configure() throws Exception {
    from(ROUTE_URI)
        .routeId(STOP_ROUTES_ID)
        .process(this::stopRoute)
        .log(INFO, "Stopped route: ${body}"); //$NON-NLS-1$
  }

  public void stopRoute(Exchange exchange) throws Exception {
    String routeName = exchange.getIn().getBody(String.class);
    LOG.info("Stopping down route: " + routeName); //$NON-NLS-1$
    exchange.getContext().stopRoute(routeName);
    shutdownService.routeStopped(routeName);
  }
}
