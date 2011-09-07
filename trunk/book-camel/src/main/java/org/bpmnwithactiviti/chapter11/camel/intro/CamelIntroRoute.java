package org.bpmnwithactiviti.chapter11.camel.intro;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;

public class CamelIntroRoute extends RouteBuilder {

  @Override
  public void configure() throws Exception {

    from("direct:start")
        .log(LoggingLevel.INFO, "Received message ${in.body}")
        .choice()
            .when(xpath("/introduction/text() = 'Camel'"))
                .to("file://introduction?fileName=camel-intro-${in.header.name}-$simple{date:now:yyyyMMdd_HHmmss}.txt")
            .otherwise()
                .to("file://introduction?fileName=other-intro-${in.header.name}-$simple{date:now:yyyyMMdd_HHmmss}.txt");
  }

}
