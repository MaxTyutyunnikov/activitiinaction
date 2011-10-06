package org.bpmnwithactiviti.chapter10.ldap;

import org.activiti.engine.impl.interceptor.Session;
import org.activiti.engine.impl.interceptor.SessionFactory;
import org.activiti.engine.impl.persistence.entity.UserManager;

public class LDAPUserManagerFactory implements SessionFactory {

	private LDAPConnectionParams connectionParams;
	
	public LDAPUserManagerFactory(LDAPConnectionParams params) {
		this.connectionParams = params;
	}
	
	@Override
  public Class<?> getSessionType() {
	  return UserManager.class;
  }

	@Override
  public Session openSession() {
	  return new LDAPUserManager(connectionParams);
  }

}
