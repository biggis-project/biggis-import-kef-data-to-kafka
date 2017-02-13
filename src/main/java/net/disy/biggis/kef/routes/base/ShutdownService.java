//Copyright (c) 2016 by Disy Informationssysteme GmbH
package net.disy.biggis.kef.routes.base;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.camel.CamelContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

// NOT_PUBLISHED
@Component
public class ShutdownService {

  private static final Logger LOG = LoggerFactory.getLogger(ShutdownService.class);

  private Set<String> registeredRoutes = ConcurrentHashMap.newKeySet();
  private Set<String> stoppedRoutes = ConcurrentHashMap.newKeySet();

  @Autowired
  private CamelContext camelContext;

  public void registerRoute(String routeName) {
    LOG.debug("Adding route to watch for shutdown: " + routeName); //$NON-NLS-1$
    registeredRoutes.add(routeName);
  }

  public boolean isRouteActive() {
    return !stoppedRoutes.containsAll(registeredRoutes);
  }

  public void routeStopped(String routeId) throws Exception {
    if (registeredRoutes.contains(routeId)) {
      stoppedRoutes.add(routeId);
    }
    if (stoppedRoutes.containsAll(registeredRoutes)) {
      LOG.info("All register routes are stopped. Shutting down context"); //$NON-NLS-1$
      camelContext.stop();
    }
  }

}
