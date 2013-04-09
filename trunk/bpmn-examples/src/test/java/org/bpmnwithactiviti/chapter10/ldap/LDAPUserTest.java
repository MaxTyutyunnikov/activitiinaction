package org.bpmnwithactiviti.chapter10.ldap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.activiti.engine.identity.User;
import org.activiti.engine.test.ActivitiRule;
import org.junit.Rule;
import org.junit.Test;

public class LDAPUserTest {

	@Rule
	public ActivitiRule activitiRule = new ActivitiRule("chapter10/ldap/activiti.cfg-mem-ldap.xml");

	@Test
	public void testCheckLogin() {
		boolean validated = activitiRule.getIdentityService().checkPassword("kermit", "kermit");
		assertTrue(validated);
	}
	
	@Test
	public void testCheckLoginFailure() {
		boolean validated = activitiRule.getIdentityService().checkPassword("kermit", "kermit2");
		assertFalse(validated);
	}
	
	@Test
	public void findUserById() throws Exception {
		User user = activitiRule.getIdentityService().createUserQuery().userId("kermit").singleResult();
		assertNotNull(user);
		assertEquals("kermit", user.getId());
		assertEquals("the Frog", user.getLastName());
	}
	
	@Test
	public void findUserByLastname() throws Exception {
		User user = activitiRule.getIdentityService().createUserQuery().userLastName("the Frog").singleResult();
		assertNotNull(user);
		assertEquals("kermit", user.getId());
		assertEquals("the Frog", user.getLastName());
	}
	
	@Test
	public void countUsers() throws Exception {
		assertEquals(2, activitiRule.getIdentityService().createUserQuery().count());
	}
}