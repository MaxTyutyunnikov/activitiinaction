package org.bpmnwithactiviti.chapter9.rules;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.DeploymentBuilder;
import org.apache.log4j.Logger;
import org.bpmnwithactiviti.chapter9.rules.model.Deployment;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.data.ChallengeScheme;
import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;

public class ActivitiDelegate {

	private static Logger logger = Logger.getLogger(ActivitiDelegate.class);
	private static ProcessEngine processEngine;

	private static ClientResource getClientResource(String uri) {
		logger.info("Getting client resource for : " + uri);
		ClientResource clientResource = new ClientResource(uri);
		clientResource.setChallengeResponse(ChallengeScheme.HTTP_BASIC,
				"kermit", "kermit");
		return clientResource;
	}

	public static Collection<Deployment> getDeployments() throws IOException,
			JSONException {
		Collection<Deployment> deployments = new ArrayList<Deployment>();
		String uri = RuleApplication.activitiURL + "/deployments";
		Representation response = getClientResource(uri).get(
				MediaType.APPLICATION_JSON);
		JSONObject object = new JSONObject(response.getText());
		if (object != null) {
			JSONArray arr = (JSONArray) object.get("data");
			logger.info("Found deployments : " + arr.length());
			for (int i = 0; i < arr.length(); i++) {
				JSONObject jsonObject = (JSONObject) arr.get(i);
				logger.info("Returning deploymentId " + jsonObject.get("id"));
				Deployment deployment = new Deployment();
				deployment.setId((String) jsonObject.get("id"));
				try{
					deployment.setName((String) jsonObject.get("name"));
				}catch(Exception e){
					deployment.setName("No name found");
				}
				deployments.add(deployment);
			}
		}
		return deployments;
	}

	public static List<String> getDeployedResourceNames(String deploymentId) {
		if (processEngine == null) {
			processEngine = ProcessEngines.getDefaultProcessEngine();
		}
		RepositoryService repositoryService = processEngine
				.getRepositoryService();
		List<String> fileNames = repositoryService
				.getDeploymentResourceNames(deploymentId);
		return fileNames;
	}

	public static String getRuleResource(String deploymentId,
			String drlFileName) throws Exception {
		if (processEngine == null) {
			processEngine = ProcessEngines.getDefaultProcessEngine();
		}
		RepositoryService repositoryService = processEngine
				.getRepositoryService();
		InputStream stream = repositoryService.getResourceAsStream(
				deploymentId, drlFileName);
		return convertStreamToString(stream);
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
