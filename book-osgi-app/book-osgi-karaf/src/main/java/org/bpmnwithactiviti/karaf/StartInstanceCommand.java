package org.bpmnwithactiviti.karaf;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.ProcessInstance;
import org.apache.felix.gogo.commands.Argument;
import org.apache.felix.gogo.commands.Command;
import org.apache.karaf.shell.console.OsgiCommandSupport;

@Command(scope = "activiti", name = "start", description="Start a new process instance")
public class StartInstanceCommand extends OsgiCommandSupport {
	
	@Argument(index = 0, name = "key", description = "Process definition key", required = true, multiValued = false)
	String key;
	
	private RuntimeService runtimeService;
	
	@Override
	protected Object doExecute() throws Exception {
    ProcessInstance instance = runtimeService.startProcessInstanceByKey(key);
    System.out.println("-----------------------------------");
    System.out.println("--Activiti start process instance--");
    System.out.println("-----------------------------------");
    System.out.println("Instance id\t\t\t" + instance.getProcessInstanceId());
    System.out.println("Ended?\t\t\t" + instance.isEnded());
    return null;
  }

	public void setRuntimeService(RuntimeService runtimeService) {
  	this.runtimeService = runtimeService;
  }
}
