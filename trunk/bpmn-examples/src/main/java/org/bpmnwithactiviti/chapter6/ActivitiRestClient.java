package org.bpmnwithactiviti.chapter6;

import java.io.IOException;

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

	private static String REST_URL = "http://localhost:8080/activiti-rest/service";

	private static ClientResource getClientResource(String url) {
		ClientResource clientResource = new ClientResource(url);
		clientResource.setChallengeResponse(ChallengeScheme.HTTP_BASIC,
				"kermit", "kermit");
		return clientResource;
	}

	public static String claimHandleVacationRequestTask(String taskId)
			throws Exception {
		String url = REST_URL + "/task/" + taskId + "/claim";
		Representation response = getClientResource(url).put("{}",
				MediaType.APPLICATION_JSON);
		JSONObject object = new JSONObject(response.getText());
		System.out.println("Claimed task " + taskId + " " + object.getString("success"));
		return object.getString("success");
	}

	public static String completeHandleVacationRequestTask(String taskId)
			throws Exception {
		String url = REST_URL + "/task/" + taskId + "/complete";
		Representation response = getClientResource(url).put(
				"{vacationApproved:true}", MediaType.APPLICATION_JSON);
		JSONObject object = new JSONObject(response.getText());
		System.out.println("Completed task " + taskId + " " + object.getString("success"));
		return object.getString("success");
	}

	public static String getHandleVacationRequestTask(String processInstanceId)
			throws Exception {
		String url = REST_URL + "/tasks?candidate=kermit";
		Representation response = getClientResource(url).get(
				MediaType.APPLICATION_JSON);
		JSONObject object = new JSONObject(response.getText());
		if (object != null) {
			JSONArray arr = (JSONArray) object.get("data");
			for (int i = 0; i < arr.length(); i++) {
				JSONObject ob = (JSONObject) arr.get(i);
				if (ob.get("processInstanceId").equals(processInstanceId)) {
					System.out.println("Returning taskId " + ob.get("id"));
					return (String) ob.get("id");
				}
			}
		}
		return null;
	}

	@SuppressWarnings("unused")
	public static int getTasks(String status, String candidate)
			throws Exception {
		String url = REST_URL + "/tasks?" + status + "=" + candidate;
		Representation response = getClientResource(url).get(
				MediaType.APPLICATION_JSON);
		JSONObject object = new JSONObject(response.getText());
		if (object != null) {
			JSONArray arr = (JSONArray) object.get("data");
			System.out.println("Tasklist " + candidate + " " + status + " size " + arr.length());
			return arr.length();
		}
		return -1;
	}

	public static String startVacationRequestProcess(String processDefinitionId)
			throws Exception {
		String url = REST_URL + "/process-instance";
		JSONStringer jsRequest = new JSONStringer();
		try {
			jsRequest.object();
			jsRequest.key("processDefinitionId").value("vacationRequest:1");
			jsRequest.key("employeeName").value("Miss Piggy");
			jsRequest.key("numberOfDays").value("10");
			jsRequest.key("startDate").value("2011-01-01");
			jsRequest.key("returnDate").value("2011-01-11");
			jsRequest.key("vacationMotivation").value("tired");
			jsRequest.endObject();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		Representation rep = new JsonRepresentation(jsRequest);
		rep.setMediaType(MediaType.APPLICATION_JSON);
		JSONObject jsObj = new JSONObject(getClientResource(url).post(rep)
				.getText());
		System.out.println("Returning processId " + jsObj.getString("id"));
		return jsObj.getString("id");
	}

	public static String getVacationRequestProcessId() throws IOException,
			JSONException {
		String url = REST_URL + "/process-definitions?size=100";
		Representation response = getClientResource(url).get(
				MediaType.APPLICATION_JSON);
		JSONObject object = new JSONObject(response.getText());
		if (object != null) {
			JSONArray arr = (JSONArray) object.get("data");
			for (int i = 0; i < arr.length(); i++) {
				JSONObject ob = (JSONObject) arr.get(i);
				if (ob.get("key").equals("vacationRequest")) {
					System.out.println("Returning processDefinitionId " + ob.get("id"));
					return (String) ob.get("id");
				}
			}
		}
		return null;
	}

}
