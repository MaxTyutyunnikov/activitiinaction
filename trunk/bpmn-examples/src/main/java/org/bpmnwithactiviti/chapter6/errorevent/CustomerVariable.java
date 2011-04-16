package org.bpmnwithactiviti.chapter6.errorevent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.bpmnwithactiviti.chapter6.ws.SalesOpportunity;

public class CustomerVariable implements Serializable {

  private static final long serialVersionUID = 1L;
  
  private String contactPerson;
  private String customerAddress;
  private boolean customerFound;
  private long customerId;
  private String customerName;
  private List<SalesOpportunity> salesOpportunities = new ArrayList<SalesOpportunity>();
  
  public String getContactPerson() {
    return contactPerson;
  }
  
  public void setContactPerson(String contactPerson) {
    this.contactPerson = contactPerson;
  }
  
  public String getCustomerAddress() {
    return customerAddress;
  }
  
  public void setCustomerAddress(String customerAddress) {
    this.customerAddress = customerAddress;
  }
  
  public boolean isCustomerFound() {
    return customerFound;
  }
  
  public void setCustomerFound(boolean customerFound) {
    this.customerFound = customerFound;
  }
  
  public long getCustomerId() {
    return customerId;
  }
  
  public void setCustomerId(long customerId) {
    this.customerId = customerId;
  }
  
  public String getCustomerName() {
    return customerName;
  }
  
  public void setCustomerName(String customerName) {
    this.customerName = customerName;
  }
  
  public List<SalesOpportunity> getSalesOpportunities() {
    return salesOpportunities;
  }
  
  public void setSalesOpportunities(List<SalesOpportunity> salesOpportunities) {
    this.salesOpportunities = salesOpportunities;
  }
}
