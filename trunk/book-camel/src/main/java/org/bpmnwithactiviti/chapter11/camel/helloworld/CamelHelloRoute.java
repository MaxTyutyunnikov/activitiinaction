package org.bpmnwithactiviti.chapter11.camel.helloworld;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;

public class CamelHelloRoute extends RouteBuilder {

  @Override
  public void configure() throws Exception {

    from("activiti:helloCamelProcess:serviceTask1")
        .log(LoggingLevel.INFO, "Received message on service task ${property.var1}")
        .setProperty("var2").constant("world")
        .setBody().properties();

    from("direct:start").to("activiti:helloCamelProcess");
  }

}
