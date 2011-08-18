package org.bpmnwithactiviti.chapter8.startup;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.ProcessEngines;

public class ActivitiJavaServletContextListener implements ServletContextListener {
  
  @Override
  public void contextInitialized(ServletContextEvent event) {
    ProcessEngines.init();
    ProcessEngine processEngine = ProcessEngineConfiguration
        .createStandaloneProcessEngineConfiguration()
        .setJdbcUrl("jdbc:mysql://localhost:3306/activiti?autoReconnect=true")
        .setJdbcDriver("com.mysql.jdbc.Driver")
        .setJdbcUsername("activiti")
        .setJdbcPassword("test")
        .setJobExecutorActivate(true)
        .buildProcessEngine();
    ProcessEngines.registerProcessEngine(processEngine);
  }

  @Override
  public void contextDestroyed(ServletContextEvent event) {
    ProcessEngines.destroy();
  }

}
