package org.bpmnwithactiviti.chapter14;

import org.junit.Assert;
import org.junit.Test;

import com.espertech.esper.client.Configuration;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;

public class HelloWorldEsperTest {
	
  public class HelloWorldEvent {
    private String value;

    public HelloWorldEvent(String value){
      this.value = value;
    }    

    public String getValue(){                                            
      return value;                                                   
    }                                                                    
  }                                                                      
	
  @Test
  public void helloEsper() {
    Configuration configuration = new Configuration();
    configuration.addEventType(HelloWorldEvent.class);
    EPServiceProvider epService = EPServiceProviderManager.getDefaultProvider(configuration);

    EPStatement epStatement = epService.getEPAdministrator()
        .createEPL("select value as eventValue from HelloWorldEvent");          
		
    epStatement.addListener(new UpdateListener(){
      public void update(EventBean[] newEvents, EventBean[] oldEvents){
        Assert.assertEquals("Hello!", (String) newEvents[0].get("eventValue"));
        System.out.println("received message " + newEvents[0]);
      }
    });

    HelloWorldEvent helloworld = new HelloWorldEvent("Hello!");
    epService.getEPRuntime().sendEvent(helloworld);
  }
}

