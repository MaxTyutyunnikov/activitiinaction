package org.bpmnwithactiviti.chapter7.test;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.bpmnwithactiviti.chapter7.data.model.Customer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestData {
  
  public static void main(String args[]) {
    ApplicationContext ctx = new ClassPathXmlApplicationContext("sales-testdata-context.xml");
    EntityManagerFactory factory = ctx.getBean("entityManagerFactory", EntityManagerFactory.class);
    EntityManager entityManager = factory.createEntityManager();
    entityManager.getTransaction().begin();
    deleteCustomer(entityManager);
    createCustomer(entityManager);
    entityManager.getTransaction().commit();
  }

  @SuppressWarnings("unchecked")
  private static void deleteCustomer(EntityManager entityManager) {
    List<Customer> customerList = entityManager.createQuery("from Customer c where c.customerName = ?")
        .setParameter(1, "Alfresco")
        .getResultList();
    if(customerList != null) {
      for (Customer customer : customerList) {
        System.out.println("deleting customer " + customer.getCustomerId());
        entityManager.remove(customer);
      }
    }
  }
  
  private static void createCustomer(EntityManager entityManager) {
    Customer customer = new Customer();
    customer.setCustomerName("Alfresco");
    customer.setContactPerson("Tom Baeyens");
    customer.setCustomerAddress("Highlands 343");
    entityManager.persist(customer);
    System.out.println("created customer " + customer.getCustomerId());
  }
}