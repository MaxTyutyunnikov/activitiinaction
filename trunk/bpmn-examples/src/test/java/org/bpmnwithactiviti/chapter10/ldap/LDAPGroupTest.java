package org.bpmnwithactiviti.chapter10.ldap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.activiti.engine.identity.Group;
import org.activiti.engine.test.ActivitiRule;
import org.junit.Rule;
import org.junit.Test;

public class LDAPGroupTest {

	@Rule
	public ActivitiRule activitiRule = new ActivitiRule("chapter10/ldap/activiti.cfg-mem-ldap.xml");
	
	@Test
	public void findGroupById() throws Exception {
		Group group = activitiRule.getIdentityService().createGroupQuery().groupId("sales").singleResult();
		assertNotNull(group);
		assertEquals("sales", group.getId());
		assertEquals("sales", group.getName());
	}
	
	@Test
	public void findGroupByName() throws Exception {
		Group group = activitiRule.getIdentityService().createGroupQuery().groupName("manager").singleResult();
		assertNotNull(group);
		assertEquals("manager", group.getId());
		assertEquals("manager", group.getName());
	}
	
	@Test
	public void findGroupByMember() throws Exception {
		List<Group> groupList = activitiRule.getIdentityService().createGroupQuery().groupMember("kermit").list();
		assertNotNull(groupList);
		assertEquals(2, groupList.size());
	}
	
	@Test
	public void countGroups() throws Exception {
		// two activiti groups + default Administrator LDAP group
		assertEquals(3, activitiRule.getIdentityService().createGroupQuery().count());
	}
}