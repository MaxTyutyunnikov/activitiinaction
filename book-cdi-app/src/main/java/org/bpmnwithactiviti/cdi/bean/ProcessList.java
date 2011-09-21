package org.bpmnwithactiviti.cdi.bean;

import java.util.List;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.ProcessDefinition;

/**
 * backing bean for retrieving a list of processes
 * 
 * @author meyerd
 */
public class ProcessList {

  @Inject
  private RepositoryService repositoryService;
  
  @Produces
  @Named("processDefinitionList")
  public List<ProcessDefinition> getProcessDefinitionList() {
    return repositoryService.createProcessDefinitionQuery()
            .list();
  }

}
