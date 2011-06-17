package org.bpmnwithactiviti.chapter12.listener;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.xml.bind.JAXBContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class EventSender {
	private static final Log log = LogFactory.getLog(EventSender.class);

	private static String HOST = "http://localhost:8081/book-bam-app/events/";

	public static void send(Object event) {
		HttpURLConnection connection = null;
		try {
			log.info("Throwing event: " + event);
			URL url = new URL(HOST + event.getClass().getSimpleName());
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setDoOutput(true);
			connection.setAllowUserInteraction(false);
			connection.setRequestProperty("Content-type", "application/xml; charset=UTF-8");
			OutputStream out = connection.getOutputStream();
			JAXBContext.newInstance(event.getClass()).createMarshaller().marshal(event, out);
			out.close();
			int rc = connection.getResponseCode();
			log.debug("rc="+rc);
		} catch (Exception e) {
			log.warn("Event could not be send to BAM application",e);
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
	}
}
