package org.bpmnwithactiviti.chapter11.mule.helloworld;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.RuntimeService;
import org.bpmnwithactiviti.common.AbstractTest;
import org.junit.Test;
import org.mule.DefaultMuleMessage;
import org.mule.api.MuleContext;
import org.mule.api.MuleMessage;
import org.mule.api.client.MuleClient;
import org.mule.api.transport.PropertyScope;
import org.mule.client.DefaultLocalMuleClient;
import org.mule.context.DefaultMuleContextFactory;
import org.mule.module.activiti.action.model.ProcessInstance;

public class MuleHelloTest extends AbstractTest {

  @Test
  public void testSend() throws Exception {
    MuleContext muleContext = new DefaultMuleContextFactory().createMuleContext("helloworld/application-context.xml");
    muleContext.start();
    MuleClient muleClient = new DefaultLocalMuleClient(muleContext);
    DefaultMuleMessage message = new DefaultMuleMessage("", muleContext);
    Map<String, Object> variableMap = new HashMap<String, Object>();
    variableMap.put("var1", "hello");
    variableMap.put("processDefinitionKey", "helloWorldMule");
    message.setProperty("createProcessParameters", variableMap , PropertyScope.OUTBOUND);
    MuleMessage responseMessage = muleClient.send("vm://create", message);
    ProcessInstance processInstance = (ProcessInstance) responseMessage.getPayload();
    assertFalse(processInstance.isEnded());
    RuntimeService runtimeService = (RuntimeService) muleContext.getRegistry().get("runtimeService");
    Object result = runtimeService.getVariable(processInstance.getId(), "var2");
    assertEquals("world", result);
    muleContext.stop();
    muleContext.dispose();
  }
}
