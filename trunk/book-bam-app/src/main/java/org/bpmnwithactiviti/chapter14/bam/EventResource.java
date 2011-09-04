package org.bpmnwithactiviti.chapter14.bam;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.log4j.Logger;
import org.bpmnwithactiviti.chapter14.bam.event.LoanRequestProcessedEvent;
import org.bpmnwithactiviti.chapter14.bam.event.LoanRequestReceivedEvent;

import com.espertech.esper.client.EPServiceProviderManager;


@Path("/events")
@Consumes("application/xml")
public class EventResource {
	
	private static final Logger log = Logger.getLogger(EventResource.class);

	@POST
	@Path("LoanRequestReceivedEvent")
	public Response postEvent(LoanRequestReceivedEvent event) {
		try {
			log.info("Received event : "+event);
			EPServiceProviderManager.getDefaultProvider().getEPRuntime()
				.getEventSender("LoanRequestReceivedEvent")
				.sendEvent(event);
			return Response.status(Status.OK).build();
		} catch (RuntimeException e) {
			throw new WebApplicationException(e);
		}
	}

	@POST
	@Path("LoanRequestProcessedEvent")
	public Response postEvent(LoanRequestProcessedEvent event) {
		try {
			log.info("Received event : "+event);
			EPServiceProviderManager.getDefaultProvider().getEPRuntime()
			.getEventSender("LoanRequestProcessedEvent")
			.sendEvent(event);
			return Response.status(Status.OK).build();
		} catch (RuntimeException e) {
			throw new WebApplicationException(e);
		}
	}
}
