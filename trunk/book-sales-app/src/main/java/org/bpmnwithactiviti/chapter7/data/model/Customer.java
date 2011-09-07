package org.bpmnwithactiviti.chapter7.data.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;


/**
 * @author Tijs Rademakers
 */

@Entity
public class Customer implements Serializable {
  
  private static final long serialVersionUID = 1L;

	@Id
  @GeneratedValue
  private long customerId;
  
  private String customerName;
  private String customerAddress;
  private String contactPerson;
  
  @ElementCollection(targetClass=org.bpmnwithactiviti.chapter7.data.model.SalesOpportunity.class, fetch=FetchType.EAGER)
  private List<SalesOpportunity> saleOpportunities;

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
  
  public String getCustomerAddress() {
    return customerAddress;
  }
  
  public void setCustomerAddress(String customerAddress) {
    this.customerAddress = customerAddress;
  }
  
  public String getContactPerson() {
    return contactPerson;
  }
 
  public void setContactPerson(String contactPerson) {
    this.contactPerson = contactPerson;
  }

  public List<SalesOpportunity> getSaleOpportunities() {
    return saleOpportunities;
  }

  public void setSaleOpportunities(List<SalesOpportunity> saleOpportunities) {
    this.saleOpportunities = saleOpportunities;
  }

}
