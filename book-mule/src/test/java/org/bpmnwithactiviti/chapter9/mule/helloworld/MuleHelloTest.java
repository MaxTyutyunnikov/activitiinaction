package org.bpmnwithactiviti.chapter9.mule.helloworld;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.ProcessInstance;
import org.bpmnwithactiviti.common.AbstractTest;
import org.junit.Test;
import org.mule.api.MuleContext;
import org.mule.context.DefaultMuleContextFactory;

public class MuleHelloTest extends AbstractTest {

  @Test
  public void testSend() throws Exception {
    MuleContext muleContext = new DefaultMuleContextFactory().createMuleContext("helloworld/application-context.xml");
    muleContext.start();
    RuntimeService runtimeService = (RuntimeService) muleContext.getRegistry().get("runtimeService");
    Map<String, Object> variableMap = new HashMap<String, Object>();
    variableMap.put("var1", "hello");
    ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("helloWorldMule", variableMap);
    assertFalse(processInstance.isEnded());
    Object result = runtimeService.getVariable(processInstance.getProcessInstanceId(), "var2");
    assertEquals("world", result);
    muleContext.stop();
    muleContext.dispose();
  }
}
