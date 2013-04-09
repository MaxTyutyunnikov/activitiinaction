package org.bpmnwithactiviti.chapter10.ldap;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.identity.Group;
import org.activiti.engine.impl.GroupQueryImpl;
import org.activiti.engine.impl.Page;
import org.activiti.engine.impl.persistence.entity.GroupEntity;
import org.activiti.engine.impl.persistence.entity.GroupEntityManager;
import org.apache.commons.lang.StringUtils;
import org.apache.directory.ldap.client.api.LdapConnection;
import org.apache.directory.ldap.client.api.message.SearchResponse;
import org.apache.directory.ldap.client.api.message.SearchResultEntry;
import org.apache.directory.shared.ldap.cursor.Cursor;
import org.apache.directory.shared.ldap.entry.EntryAttribute;
import org.apache.directory.shared.ldap.filter.SearchScope;

public class LDAPGroupManager extends GroupEntityManager {

	private static final String USER_ENTRY = "ou=users,ou=system";
	private static final String GROUP_ENTRY = "ou=groups,ou=system";
	
	private LDAPConnectionParams connectionParams;
	
	public LDAPGroupManager(LDAPConnectionParams params) {
		this.connectionParams = params;
	}
	
	@Override
  public Group createNewGroup(String groupId) {
		throw new ActivitiException("LDAP group manager doesn't support creating a new group");
  }

	@Override
  public void insertGroup(Group group) {
		throw new ActivitiException("LDAP group manager doesn't support inserting a new group");
  }

	@Override
  public void updateGroup(GroupEntity updatedGroup) {
		throw new ActivitiException("LDAP group manager doesn't support updating a new group");
  }

	@Override
  public void deleteGroup(String groupId) {
		throw new ActivitiException("LDAP group manager doesn't support deleting a new group");
  }

	@Override
  public List<Group> findGroupByQueryCriteria(GroupQueryImpl query, Page page) {
		List<Group> groupList = new ArrayList<Group>();
		
	  // Query is a GroupQueryImpl instance
		GroupQueryImpl groupQuery = (GroupQueryImpl) query;
		StringBuilder searchQuery = new StringBuilder();
		if(StringUtils.isNotEmpty(groupQuery.getId())) {
			searchQuery.append("(cn=").append(groupQuery.getId()).append(")");
			
		} else if(StringUtils.isNotEmpty(groupQuery.getName())) {
			searchQuery.append("(cn=").append(groupQuery.getName()).append(")");
		
		} else if(StringUtils.isNotEmpty(groupQuery.getUserId())) {
			searchQuery.append("(uniqueMember= uid=").append(groupQuery.getUserId()).append("," + USER_ENTRY + ")");
		
		} else {
			searchQuery.append("(cn=*)");
		}
		LdapConnection connection = LDAPConnectionUtil.openConnection(connectionParams);
		try {
	  	Cursor<SearchResponse> cursor = connection.search(GROUP_ENTRY, searchQuery.toString(), SearchScope.ONELEVEL, "*");
	  	while (cursor.next()) {
	  		Group group = new GroupEntity();
	  		SearchResultEntry response = (SearchResultEntry) cursor.get();
        Iterator<EntryAttribute> itEntry = response.getEntry().iterator();
        while(itEntry.hasNext()) {
        	EntryAttribute attribute = itEntry.next();
        	String key = attribute.getId();
        	if("cn".equalsIgnoreCase(key)) {
        		group.setId(attribute.getString());
        		group.setName(attribute.getString());
        	} 
        }

        groupList.add(group);
	    }
	  	
	  	cursor.close();
	  	
	  } catch (Exception e) {
	  	throw new ActivitiException("LDAP connection search failure", e);
	  }
		
		LDAPConnectionUtil.closeConnection(connection);
		
		return groupList;
  }

	@Override
  public long findGroupCountByQueryCriteria(GroupQueryImpl query) {
	  return findGroupByQueryCriteria(query, null).size();
  }

	@Override
  public GroupEntity findGroupById(String groupId) {
		throw new ActivitiException("LDAP group manager doesn't support finding a group by id");
  }

	@Override
  public List<Group> findGroupsByUser(String userId) {
		List<Group> groupList = new ArrayList<Group>();
		
		LdapConnection connection = LDAPConnectionUtil.openConnection(connectionParams);
		try {
	  	Cursor<SearchResponse> cursor = connection.search(GROUP_ENTRY, "(uniqueMember= uid=" + userId
	  			+ "," + USER_ENTRY + ")", SearchScope.ONELEVEL, "*");
	  	while (cursor.next()) {
	  		Group group = new GroupEntity();
	  		SearchResultEntry response = (SearchResultEntry) cursor.get();
        Iterator<EntryAttribute> itEntry = response.getEntry().iterator();
        while(itEntry.hasNext()) {
        	EntryAttribute attribute = itEntry.next();
        	String key = attribute.getId();
        	if("cn".equalsIgnoreCase(key)) {
        		group.setId(attribute.getString());
        		group.setName(attribute.getString());
        	} 
        }

        groupList.add(group);
	    }
	  	
	  	cursor.close();
	  	
	  } catch (Exception e) {
	  	throw new ActivitiException("LDAP connection search failure", e);
	  }
		
		LDAPConnectionUtil.closeConnection(connection);
		
		return groupList;
  }
}
