package org.bpmnwithactiviti.chapter14;

import org.junit.Assert;
import org.junit.Test;

import com.espertech.esper.client.Configuration;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;

public class EventLenghtWindowTest {
	
	public class LoanRequestEvent {
		private final int amount;
		
		public LoanRequestEvent(int amount) {
			this.amount = amount;
		}

		public int getAmount() {
			return amount;
		}
	}

	private int sumAmount = 0;
	
	@Test
	public void testEventLengthWindow() {
		Configuration configuration = new Configuration();
		configuration.addEventType(LoanRequestEvent.class);
		EPServiceProvider epService = EPServiceProviderManager.getDefaultProvider(configuration);
		
		EPStatement epStatement = epService.getEPAdministrator().createEPL(
			"select sum(amount) as sumAmount from LoanRequestEvent.win:length(2)");
		
		epStatement.addListener(new UpdateListener () {
			public void update(EventBean[] newEvents, EventBean[] oldEvents) {
				sumAmount = (Integer) newEvents[0].get("sumAmount");
			}
		} );
		
		Assert.assertEquals(0, sumAmount);
		epService.getEPRuntime().sendEvent(new LoanRequestEvent(100));
		Assert.assertEquals(100, sumAmount);
		epService.getEPRuntime().sendEvent(new LoanRequestEvent(200));
		Assert.assertEquals(300, sumAmount);
		epService.getEPRuntime().sendEvent(new LoanRequestEvent(300));
		Assert.assertEquals(500, sumAmount);
	}
}
