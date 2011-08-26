package org.bpmnwithactiviti.chapter7.errorevent;

import static org.junit.Assert.assertNull;

import org.bpmnwithactiviti.chapter7.ws.Customer;
import org.bpmnwithactiviti.chapter7.ws.CustomerServiceService;
import org.junit.Test;


public class CustomerWSTest {
  
  @Test
  public void getCustomerById() {
    CustomerServiceService service = new CustomerServiceService();
    Customer customer = service.getCustomerServicePort().findCustomerById(1l);
    assertNull(customer);
  }

}
