package org.bpmnwithactiviti.chapter10.ldap;

public class LDAPConnectionParams {
	
	private String ldapServer;
	private int ldapPort;
	private String ldapUser;
	private String ldapPassword;
	
	public String getLdapServer() {
  	return ldapServer;
  }
	public void setLdapServer(String ldapServer) {
  	this.ldapServer = ldapServer;
  }
	public int getLdapPort() {
  	return ldapPort;
  }
	public void setLdapPort(int ldapPort) {
  	this.ldapPort = ldapPort;
  }
	public String getLdapUser() {
  	return ldapUser;
  }
	public void setLdapUser(String ldapUser) {
  	this.ldapUser = ldapUser;
  }
	public String getLdapPassword() {
  	return ldapPassword;
  }
	public void setLdapPassword(String ldapPassword) {
  	this.ldapPassword = ldapPassword;
  }
}
