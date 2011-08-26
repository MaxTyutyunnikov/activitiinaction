package org.bpmnwithactiviti.chapter7.data.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.bpmnwithactiviti.chapter7.data.model.Customer;
import org.bpmnwithactiviti.chapter7.data.model.SalesOpportunity;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Tijs Rademakers
 */
public class CustomerDAO {
  
  @PersistenceContext
  EntityManager entityManager;

  public Customer getCustomerById(long id) {
    return entityManager.find(Customer.class, id);
  }
  
  @SuppressWarnings("unchecked")
  public Customer getCustomerByNameOrContactPerson(String customerName, String contactperson) {
    StringBuilder builder = new StringBuilder();
    builder.append("FROM Customer c WHERE ");
    if(customerName != null && customerName.length() > 0) {
      builder.append("c.customerName = ?");
    }
    if(contactperson != null && contactperson.length() > 0) {
      if(customerName != null && customerName.length() > 0) {
        builder.append(" AND ");
      }
      builder.append("c.contactPerson = ?");
    }
    Query query = entityManager.createQuery(builder.toString());
    int counter = 1;
    if(customerName != null && customerName.length() > 0) {
      query.setParameter(counter, customerName);
      counter++;
    }
    if(contactperson != null && contactperson.length() > 0) {
      query.setParameter(counter, contactperson);
    }
    List<Customer> customerList = query.getResultList();
    if(customerList != null && customerList.size() > 0) {
      return customerList.get(0);
    } else {
      return null;
    }
  }
  
  @Transactional
  public void addOpprtunityToCustomer(SalesOpportunity opportunity, long customerId) {
    entityManager.persist(opportunity);
    Customer customer = getCustomerById(customerId);
    if(customer.getSaleOpportunities() == null) {
      customer.setSaleOpportunities(new ArrayList<SalesOpportunity>());
    }
    customer.getSaleOpportunities().add(opportunity);
    entityManager.merge(customer);
  }
}
