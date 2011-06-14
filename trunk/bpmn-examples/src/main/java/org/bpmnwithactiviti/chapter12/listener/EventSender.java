package org.bpmnwithactiviti.chapter12.listener;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.xml.bind.JAXBContext;

public class EventSender {
	private static String HOST = "http://localhost:8080/book-bam-app/events/"; 
	
	public static void send(Object event) throws Exception {
		System.out.println(">>> Throwing event: "+event);
		URL url = new URL(HOST+event.getClass().getSimpleName());
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("POST");
		connection.setDoOutput(true);
		connection.setAllowUserInteraction(false);
		connection.setRequestProperty("Content-type", "application/xml; charset=UTF-8");
		OutputStream out = connection.getOutputStream();
		JAXBContext.newInstance(event.getClass()).createMarshaller().marshal(event, out);
		out.close();
		int rc = connection.getResponseCode();
		System.out.println(">>> rc="+rc);
		connection.disconnect();
	}
}
