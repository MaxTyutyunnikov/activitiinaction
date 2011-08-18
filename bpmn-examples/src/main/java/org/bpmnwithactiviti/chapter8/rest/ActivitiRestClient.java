package org.bpmnwithactiviti.chapter8.rest;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;
import org.restlet.data.ChallengeScheme;
import org.restlet.data.MediaType;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;

public class ActivitiRestClient {

	private static String REST_URI = "http://localhost:8080/activiti-rest/service";
	private static Logger logger = Logger.getLogger(ActivitiRestClient.class);

	private static ClientResource getClientResource(String uri) {
		ClientResource clientResource = new ClientResource(uri);
		clientResource.setChallengeResponse(ChallengeScheme.HTTP_BASIC,
				"kermit", "kermit");
		return clientResource;
	}
	
	public static String getVacationRequestProcessId() throws IOException,
      JSONException {
    String uri = REST_URI + "/process-definitions?size=100";
    Representation response = getClientResource(uri).get(
        MediaType.APPLICATION_JSON);
    JSONObject object = new JSONObject(response.getText());
    if (object != null) {
      JSONArray arr = (JSONArray) object.get("data");
      for (int i = 0; i < arr.length(); i++) {
        JSONObject jsonObject = (JSONObject) arr.get(i);
        if (jsonObject.get("key").equals("vacationRequest")) {
          logger.info("Returning processDefinitionId " + jsonObject.get("id"));
          return (String) jsonObject.get("id");
        }
      }
    }
    return null;
  }
	
	public static String startVacationRequestProcess(String processDefinitionId)
      throws Exception {
    String uri = REST_URI + "/process-instance";
    JSONStringer jsRequest = new JSONStringer();
    jsRequest.object();
    jsRequest.key("processDefinitionId").value(processDefinitionId);
    jsRequest.key("employeeName").value("Miss Piggy");
    jsRequest.key("numberOfDays").value("10");
    jsRequest.key("startDate").value("2011-01-01");
    jsRequest.key("returnDate").value("2011-01-11");
    jsRequest.key("vacationMotivation").value("tired");
    jsRequest.endObject();
    Representation rep = new JsonRepresentation(jsRequest);
    rep.setMediaType(MediaType.APPLICATION_JSON);
    JSONObject jsObj = new JSONObject(getClientResource(uri).post(rep).getText());
    logger.info("Returning processId " + jsObj.getString("id"));
    return jsObj.getString("id");
	}
	
	public static String getHandleVacationRequestTask(String processInstanceId)
      throws Exception {
    String uri = REST_URI + "/tasks?candidate=kermit";
    Representation response = getClientResource(uri).get(
        MediaType.APPLICATION_JSON);
    JSONObject object = new JSONObject(response.getText());
    if (object != null) {
      JSONArray arr = (JSONArray) object.get("data");
      for (int i = 0; i < arr.length(); i++) {
        JSONObject ob = (JSONObject) arr.get(i);
        if (ob.get("processInstanceId").equals(processInstanceId)) {
          logger.info("Returning taskId " + ob.get("id"));
          return (String) ob.get("id");
        }
      }
    }
    return null;
	}

	public static String claimHandleVacationRequestTask(String taskId)
			throws Exception {
		String uri = REST_URI + "/task/" + taskId + "/claim";
		Representation response = getClientResource(uri).put("{}",
				MediaType.APPLICATION_JSON);
		JSONObject object = new JSONObject(response.getText());
		logger.info("Claimed task " + taskId + " " + object.getString("success"));
		return object.getString("success");
	}

	public static String completeHandleVacationRequestTask(String taskId)
			throws Exception {
		String uri = REST_URI + "/task/" + taskId + "/complete";
		Representation response = getClientResource(uri).put(
				"{vacationApproved:true}", MediaType.APPLICATION_JSON);
		JSONObject object = new JSONObject(response.getText());
		logger.info("Completed task " + taskId + " " + object.getString("success"));
		return object.getString("success");
	}

	public static int getTasks(String status, String candidate)
			throws Exception {
		String uri = REST_URI + "/tasks?" + status + "=" + candidate;
		Representation response = getClientResource(uri).get(
				MediaType.APPLICATION_JSON);
		JSONObject object = new JSONObject(response.getText());
		if (object != null) {
			JSONArray arr = (JSONArray) object.get("data");
			logger.info("Tasklist " + candidate + " " + status + " size " + arr.length());
			return arr.length();
		}
		return -1;
	}

}
