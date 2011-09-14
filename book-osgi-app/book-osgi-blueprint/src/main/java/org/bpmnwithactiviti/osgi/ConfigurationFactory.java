package org.bpmnwithactiviti.osgi;

import javax.sql.DataSource;

import org.activiti.engine.impl.cfg.StandaloneProcessEngineConfiguration;

public class ConfigurationFactory {

    DataSource dataSource;

    public StandaloneProcessEngineConfiguration getConfiguration() {
  		StandaloneProcessEngineConfiguration conf =
              new StandaloneProcessEngineConfiguration();
      conf.setDataSource(dataSource);
      conf.setDatabaseSchemaUpdate("true");
      conf.setJobExecutorActivate(true);
      return conf;
    }

    public void setDataSource(DataSource dataSource) {
       this.dataSource = dataSource;
    }
}
