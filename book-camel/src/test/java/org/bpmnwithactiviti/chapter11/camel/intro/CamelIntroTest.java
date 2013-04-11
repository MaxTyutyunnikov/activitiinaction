package org.bpmnwithactiviti.chapter11.camel.intro;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.impl.DefaultCamelContext;
import org.junit.Test;

public class CamelIntroTest {

	@Test
	public void sendMessages() throws Exception {
	  CamelContext camelContext = new DefaultCamelContext();
	  camelContext.addRoutes(new CamelIntroRoute());
	  camelContext.start();
    ProducerTemplate tpl = camelContext.createProducerTemplate();
    tpl.sendBodyAndHeader("direct:start", "<introduction>Camel</introduction>", "name", "Rademakers");
    tpl.sendBodyAndHeader("direct:start", "<introduction>Mule</introduction>", "name", "Rademakers");
    camelContext.stop();
	}
}
