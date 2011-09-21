package org.bpmnwithactiviti.cdi.bean;

import javax.enterprise.event.Observes;

import org.activiti.cdi.BusinessProcessEvent;
import org.activiti.cdi.BusinessProcessEventType;

public class EventBean {
	
	public void onProcessEvent(@Observes BusinessProcessEvent businessProcessEvent) {
    System.out.println("----- event type " + businessProcessEvent.getType().getTypeName() + " ------");
    System.out.println("Process instance id " + businessProcessEvent.getProcessInstanceId());
    if(businessProcessEvent.getType() == BusinessProcessEventType.TAKE) {
    	System.out.println("Transition name " + businessProcessEvent.getTransitionName());
    } else {
    	System.out.println("Activity id " + businessProcessEvent.getActivityId());
    }
    System.out.println("Timestamp " + businessProcessEvent.getTimeStamp());
	}
}
