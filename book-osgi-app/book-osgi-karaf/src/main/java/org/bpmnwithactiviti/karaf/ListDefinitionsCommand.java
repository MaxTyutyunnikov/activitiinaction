package org.bpmnwithactiviti.karaf;

import java.util.List;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.ProcessDefinition;
import org.apache.felix.gogo.commands.Command;
import org.apache.karaf.shell.console.OsgiCommandSupport;

@Command(scope = "activiti", name = "list-definitions", description="List all process definitions")
public class ListDefinitionsCommand extends OsgiCommandSupport {
	
	private RepositoryService repositoryService;
	
	@Override
	protected Object doExecute() throws Exception {
    List<ProcessDefinition> definitionList = repositoryService.createProcessDefinitionQuery().list();
    if(definitionList != null && definitionList.size() > 0) {
    	System.out.println("--------------------------------");
    	System.out.println("--Activiti process definitions--");
    	System.out.println("--------------------------------");
    	for (ProcessDefinition processDefinition : definitionList) {
    		System.out.println("");
    		System.out.println("--------------------------------");
	      System.out.println("Name\t\t\t\t" + processDefinition.getName());
	      System.out.println("Key\t\t\t\t" + processDefinition.getKey());
	      System.out.println("Id\t\t\t\t" + processDefinition.getId());
	      System.out.println("--------------------------------");
	      System.out.println("");
    	}
    }
    return null;
  }

	public void setRepositoryService(RepositoryService repositoryService) {
  	this.repositoryService = repositoryService;
  }
}
