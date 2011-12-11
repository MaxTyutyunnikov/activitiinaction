package org.bpmnwithactiviti.chapter8.startup;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.apache.log4j.Logger;

public class ActivitiServletContextListener implements ServletContextListener {
  
  private static final Logger logger = Logger.getLogger(ActivitiServletContextListener.class);

  @Override
  public void contextInitialized(ServletContextEvent event) {
    ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
    if (processEngine == null) {
      logger.error("Could not start the Activiti Engine");
    }
  }

  @Override
  public void contextDestroyed(ServletContextEvent event) {
    ProcessEngines.destroy();
  }
}
