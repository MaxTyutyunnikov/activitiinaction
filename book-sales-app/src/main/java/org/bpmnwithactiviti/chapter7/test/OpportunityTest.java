package org.bpmnwithactiviti.chapter7.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.bpmnwithactiviti.chapter7.data.model.Customer;
import org.bpmnwithactiviti.chapter7.data.model.SalesOpportunity;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class OpportunityTest {
  
  @Test
  public void validateOpportunity() {
    ApplicationContext ctx = new ClassPathXmlApplicationContext("sales-testdata-context.xml");
    EntityManagerFactory factory = ctx.getBean("entityManagerFactory", EntityManagerFactory.class);
    EntityManager entityManager = factory.createEntityManager();
    Customer customer = entityManager.find(Customer.class, 1l);
    assertNotNull(customer);
    assertNotNull(customer.getSaleOpportunities());
    assertEquals(1, customer.getSaleOpportunities().size());
    SalesOpportunity opportunity = customer.getSaleOpportunities().get(0);
    assertEquals("Apple iMac", opportunity.getProduct());
  }

}
