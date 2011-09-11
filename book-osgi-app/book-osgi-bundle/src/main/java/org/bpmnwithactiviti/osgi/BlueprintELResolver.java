package org.bpmnwithactiviti.osgi;

import java.beans.FeatureDescriptor;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.activiti.engine.impl.javax.el.ELContext;
import org.activiti.engine.impl.javax.el.ELResolver;
import org.activiti.engine.impl.pvm.delegate.ActivityBehavior;

/**
 * @see org.activiti.spring.ApplicationContextElResolver
 */
public class BlueprintELResolver extends ELResolver {
	
	private Map<String, ActivityBehavior> activityMap = new HashMap<String, ActivityBehavior>();

	public Object getValue(ELContext context, Object base, Object property) {
		if (base == null) {
			// according to javadoc, can only be a String
			String key = (String) property;
			for (String name : activityMap.keySet()) {
	      if(name.equalsIgnoreCase(key)) {
	      	context.setPropertyResolved(true);
	      	return activityMap.get(name);
	      }
	    }
		}

		return null;
	}
	
	public void bindService(ActivityBehavior activity, Map props) {
    System.out.println("bind service " + activity + " props " + props);
    String name = (String) props.get("osgi.service.blueprint.compname");
    if(activityMap.containsKey(name) == false) {
    	activityMap.put(name, activity);
    }
    System.out.println("added " + name + " to the map");
	}

	public void unbindService(ActivityBehavior activity, Map props) {
		System.out.println("unbind service " + activity + " " + props);
		String name = (String) props.get("osgi.service.blueprint.compname");
    if(activityMap.containsKey(name)) {
    	activityMap.remove(name);
    }
    System.out.println("removed " + name + " from the map");
	}

	public boolean isReadOnly(ELContext context, Object base, Object property) {
		return true;
	}

	public void setValue(ELContext context, Object base, Object property,
	    Object value) {
	}

	public Class<?> getCommonPropertyType(ELContext context, Object arg) {
		return Object.class;
	}

	public Iterator<FeatureDescriptor> getFeatureDescriptors(ELContext context,
	    Object arg) {
		return null;
	}

	public Class<?> getType(ELContext context, Object arg1, Object arg2) {
		return Object.class;
	}
}
