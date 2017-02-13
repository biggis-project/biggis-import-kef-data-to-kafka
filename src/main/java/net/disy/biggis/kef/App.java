//Copyright (c) 2016 by Disy Informationssysteme GmbH
package net.disy.biggis.kef;

import net.disy.biggis.kef.routes.base.ShutdownService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// NOT_PUBLISHED

@SpringBootApplication
public class App implements CommandLineRunner {

  private static final Logger LOG = LoggerFactory.getLogger(App.class);

  @Autowired
  private ShutdownService shutdownService;

  public static void main(String[] args) throws Exception {
    SpringApplication.run(App.class, args);
  }

  @Override
  public void run(String... args) throws Exception {
    while (shutdownService.isRouteActive()) {
      LOG.debug("Waiting for routes to finish."); //$NON-NLS-1$
      Thread.sleep(1000);
    }
  }

}
