package org.bpmnwithactiviti.chapter9.mule;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.ProcessInstance;
import org.bpmnwithactiviti.common.AbstractTest;
import org.junit.Test;
import org.mule.api.MuleContext;
import org.mule.context.DefaultMuleContextFactory;

public class BookOrderMuleTest extends AbstractTest {

  @Test
  public void testSend() throws Exception {
    MuleContext muleContext = new DefaultMuleContextFactory().createMuleContext("application-context.xml");
    muleContext.start();
    RuntimeService runtimeService = (RuntimeService) muleContext.getRegistry().get("runtimeService");
    ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("muleProcess");
    assertFalse(processInstance.isEnded());
    Object result = runtimeService.getVariable(processInstance.getProcessInstanceId(), "theVariable");
    assertEquals(10, result);
    
    muleContext.stop();
    muleContext.dispose();
  }
}
