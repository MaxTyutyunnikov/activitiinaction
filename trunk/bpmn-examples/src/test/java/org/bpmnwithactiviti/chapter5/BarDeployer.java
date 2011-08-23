package org.bpmnwithactiviti.chapter5;

import java.io.FileInputStream;
import java.util.List;
import java.util.zip.ZipInputStream;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;

public class BarDeployer {

	public static void main(String[] args) throws Exception {
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		RepositoryService repositoryService = processEngine.getRepositoryService();
		String barFileName = "src/main/resources/chapter5/dist/loanrequest.bar";
		ZipInputStream inputStream = new ZipInputStream(new FileInputStream(barFileName));
		String deploymentID = repositoryService.createDeployment().name(barFileName).addZipInputStream(inputStream).deploy().getId();
		List<String> deployedResources = repositoryService.getDeploymentResourceNames(deploymentID);
		for (String deployedResource : deployedResources) {
			System.out.println("Deployed : " + deployedResource);
		}
		inputStream.close();
	}
}
