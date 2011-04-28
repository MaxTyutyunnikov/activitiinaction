package org.bpmnwithactiviti.chapter10.rules;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.List;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.apache.log4j.Logger;

public class ActivitiDelegate {

	private static Logger logger = Logger.getLogger(ActivitiDelegate.class);
	private static ProcessEngine processEngine;
	
	public static List<Deployment> getDeployments() {
    return getRepositoryService().createDeploymentQuery().list();
  }

	public static List<String> getDeployedResourceNames(String deploymentId) {
		List<String> fileNames = getRepositoryService()
				.getDeploymentResourceNames(deploymentId);
		return fileNames;
	}

	public static String getRuleResource(String deploymentId,
			String drlFileName) throws Exception {
	  
		InputStream stream = getRepositoryService().getResourceAsStream(
				deploymentId, drlFileName);
		return convertStreamToString(stream);
	}
	
	private static RepositoryService getRepositoryService() {
    return getProcessEngine().getRepositoryService();
  }
	
	private static ProcessEngine getProcessEngine() {
	  if (processEngine == null) {
      processEngine = ProcessEngines.getDefaultProcessEngine();
    }
	  return processEngine;
	}

	private static String convertStreamToString(InputStream is) throws IOException {
		if (is != null) {
			Writer writer = new StringWriter();
			char[] buffer = new char[1024];
			try {
				Reader reader = new BufferedReader(
				new InputStreamReader(is, "UTF-8"));
				int n;
				while ((n = reader.read(buffer)) != -1) {
					writer.write(buffer, 0, n);
				}
			} finally {
				is.close();
			}
			return writer.toString();
		} else {
			return "";
		}
	}

	public static void deployEditedRule(Deployment d, String ruleFileName,
			String drlContent) {
		logger.info("Ready to deploy " + ruleFileName + " in deployment " + d.getId());
		RepositoryService repositoryService = processEngine.getRepositoryService();
		DeploymentBuilder builder = repositoryService.createDeployment();
		builder.addClasspathResource("chapter9/loanrequest.bpmn20.xml");
		builder.addString(ruleFileName, drlContent);
		builder.name(d.getName());
		builder.deploy();
	}

}
