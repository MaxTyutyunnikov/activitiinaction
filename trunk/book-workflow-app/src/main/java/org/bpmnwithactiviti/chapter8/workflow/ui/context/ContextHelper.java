package org.bpmnwithactiviti.chapter8.workflow.ui.context;

import javax.servlet.ServletContext;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.vaadin.Application;
import com.vaadin.terminal.gwt.server.WebApplicationContext;

public class ContextHelper {
	
	private ApplicationContext context;
	
	public ContextHelper(Application application) {
		ServletContext servletContext = ((WebApplicationContext) application.getContext()).getHttpSession().getServletContext();
    context = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
	}
	
	public RepositoryService getRepositoryService() {
		return (RepositoryService) context.getBean("repositoryService");
	}
	
	public RuntimeService getRuntimeService() {
		return (RuntimeService) context.getBean("runtimeService");
	}
	
	public TaskService getTaskService() {
		return (TaskService) context.getBean("taskService");
	}
}
