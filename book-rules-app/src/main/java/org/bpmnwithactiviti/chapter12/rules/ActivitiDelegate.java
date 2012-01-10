package org.bpmnwithactiviti.chapter12.rules;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.log4j.Logger;

public class ActivitiDelegate {

	private static Logger logger = Logger.getLogger(ActivitiDelegate.class);
	private static String REST_URI = "http://localhost:8080/activiti-rest/service";
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

	public static void deployEditedRule(Deployment deployment, String ruleFileName, String drlContent) {
		logger.info("Ready to deploy " + ruleFileName + " in deployment " + deployment.getId());
		List<String> fileNames = getRepositoryService().getDeploymentResourceNames(deployment.getId());
		fileNames.remove(ruleFileName);
		deploy(fileNames, deployment, ruleFileName, drlContent);
	}
		
	public static void deploy(List<String> fileNames, Deployment deployment, String ruleFileName, String drlContent) {
		// Create a buffer for reading the files
		byte[] buf = new byte[1024];
		String filename = deployment.getName();
		if(filename.toLowerCase().endsWith(".bar") == false) {
			filename += ".bar";
		}
		try {
			ZipOutputStream out = new ZipOutputStream(new FileOutputStream(filename));
			for (String file : fileNames) {
				
				if(ruleFileName.equalsIgnoreCase(file)) continue;
				
				InputStream resourceStream = getRepositoryService().getResourceAsStream(deployment.getId(), file);
	      
	      // Add ZIP entry to output stream.
	      out.putNextEntry(new ZipEntry(file));
	
	      // Transfer bytes from the file to the ZIP file
	      int len;
	      while ((len = resourceStream.read(buf)) > 0) {
	          out.write(buf, 0, len);
	      }
	
	      // Complete the entry
	      out.closeEntry();
	      resourceStream.close();
			}
			out.putNextEntry(new ZipEntry(ruleFileName));
			out.write(drlContent.getBytes());
			out.closeEntry();
		} catch(Exception e) {
			logger.error("Error while deploying the changed rule " + ruleFileName, e);
		}

		DefaultHttpClient client = new DefaultHttpClient();
		try {
			client.getCredentialsProvider().setCredentials(new AuthScope("localhost", 8080), 
			    new UsernamePasswordCredentials("kermit", "kermit"));
			
			// Create AuthCache instance
      AuthCache authCache = new BasicAuthCache();
      // Generate BASIC scheme object and add it to the local
      // auth cache
      BasicScheme basicAuth = new BasicScheme();
      authCache.put(new HttpHost("localhost", 8080, "http"), basicAuth);

      // Add AuthCache to the execution context
      BasicHttpContext localcontext = new BasicHttpContext();
      localcontext.setAttribute(ClientContext.AUTH_CACHE, authCache);
			
			HttpPost postMethod = new HttpPost(REST_URI + "/deployment");
			
			FileBody barFile = new FileBody(new File(filename));
      
      MultipartEntity reqEntity = new MultipartEntity();
      reqEntity.addPart(filename, barFile);
      
      postMethod.setEntity(reqEntity);
	
	    HttpResponse response = client.execute(postMethod, localcontext);
	
	    System.out.println("response>>>" + response.getStatusLine());
		
		} catch(Exception e) {
			e.printStackTrace();
		
		} finally {
      client.getConnectionManager().shutdown();
		}
	}

}
