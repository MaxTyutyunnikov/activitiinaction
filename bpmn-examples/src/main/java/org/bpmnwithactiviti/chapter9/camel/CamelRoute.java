package org.bpmnwithactiviti.chapter9.camel;

import org.apache.camel.builder.RouteBuilder;

public class CamelRoute extends RouteBuilder {

  @Override
  public void configure() throws Exception {

    from("activiti:bookorder:serviceTask1").setBody()
        .property("var1")
        .to("mock:service1")
        .setProperty("var2")
        .constant("var2")
        .setBody()
        .properties();

    from("activiti:bookorder:serviceTask2?copyVariablesToBody=true").to("mock:service2");

    from("direct:start").to("activiti:bookorder");

    from("direct:receive").to("activiti:bookorder:receive");

  }

}
