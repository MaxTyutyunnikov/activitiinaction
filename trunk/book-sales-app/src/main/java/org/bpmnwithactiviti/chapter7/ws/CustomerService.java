package org.bpmnwithactiviti.chapter7.ws;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

import org.bpmnwithactiviti.chapter7.data.dao.CustomerDAO;
import org.bpmnwithactiviti.chapter7.data.model.Customer;
import org.bpmnwithactiviti.chapter7.data.model.SalesOpportunity;


@WebService
public class CustomerService {
  
  private CustomerDAO customerDAO;
  
  public Customer findCustomerById(@WebParam(name="customerId") long customerId) {
    return customerDAO.getCustomerById(customerId);
  }
  
  public Customer findCustomer(@WebParam(name="customerName") String customerName, 
          @WebParam(name="contactperson") String contactperson) {
    return customerDAO.getCustomerByNameOrContactPerson(customerName, contactperson);
  }

  public Customer storeSalesOpportunity(@WebParam(name="product") String product, 
          @WebParam(name="expectedQuantity") long expectedQuantity,
          @WebParam(name="description") String description,
          @WebParam(name="customerId") long customerId) {
    
    System.out.println("storing sales opportunity " + customerId);
    SalesOpportunity opportunity = new SalesOpportunity();
    opportunity.setProduct(product);
    opportunity.setExpectedQuantity(expectedQuantity);
    opportunity.setDescription(description);
    customerDAO.addOpprtunityToCustomer(opportunity, customerId);
    System.out.println("finding customer " + customerId);
    Customer customer = findCustomerById(customerId);
    System.out.println("found customer " + customer);
    return customer;
  }

  @WebMethod(exclude=true)
  public void setCustomerDAO(CustomerDAO customerDAO) {
    this.customerDAO = customerDAO;
  }
}
