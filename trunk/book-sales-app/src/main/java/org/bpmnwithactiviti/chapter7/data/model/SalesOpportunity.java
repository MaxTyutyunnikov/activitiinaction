package org.bpmnwithactiviti.chapter7.data.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;


/**
 * @author Tijs Rademakers
 */

@Entity
public class SalesOpportunity {
  
  @Id
  @GeneratedValue
  private long opportunityId;
  
  private String product;
  private int expectedQuantity;
  private String description;
  
  public long getOpportunityId() {
    return opportunityId;
  }
  
  public void setOpportunityId(long opportunityId) {
    this.opportunityId = opportunityId;
  }
  
  public String getProduct() {
    return product;
  }
  
  public void setProduct(String product) {
    this.product = product;
  }
  
  public int getExpectedQuantity() {
    return expectedQuantity;
  }
  
  public void setExpectedQuantity(int expectedQuantity) {
    this.expectedQuantity = expectedQuantity;
  }
  
  public String getDescription() {
    return description;
  }
  
  public void setDescription(String description) {
    this.description = description;
  }
}
