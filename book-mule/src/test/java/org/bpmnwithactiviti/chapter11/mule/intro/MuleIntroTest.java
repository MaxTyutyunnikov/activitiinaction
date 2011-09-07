package org.bpmnwithactiviti.chapter11.mule.intro;

import org.bpmnwithactiviti.common.AbstractTest;
import org.junit.Test;
import org.mule.DefaultMuleMessage;
import org.mule.api.MuleContext;
import org.mule.api.client.MuleClient;
import org.mule.client.DefaultLocalMuleClient;
import org.mule.context.DefaultMuleContextFactory;

public class MuleIntroTest extends AbstractTest {

  @Test
  public void testSend() throws Exception {
    MuleContext muleContext = new DefaultMuleContextFactory().createMuleContext("intro/mule-context.xml");
    muleContext.start();
    MuleClient muleClient = new DefaultLocalMuleClient(muleContext);
    muleClient.send("vm://in", new DefaultMuleMessage("<introduction>Mule</introduction>", muleContext));
    muleClient.send("vm://in", new DefaultMuleMessage("<introduction>Camel</introduction>", muleContext));
    muleContext.stop();
    muleContext.dispose();
  }
}
