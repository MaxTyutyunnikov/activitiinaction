package org.bpmnwithactiviti.chapter7.data.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;


/**
 * @author Tijs Rademakers
 */

@Entity
public class SalesOpportunity implements Serializable {
  
  private static final long serialVersionUID = 1L;

	@Id
  @GeneratedValue
  private long opportunityId;
  
  private String product;
  private long expectedQuantity;
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
  
  public long getExpectedQuantity() {
    return expectedQuantity;
  }
  
  public void setExpectedQuantity(long expectedQuantity) {
    this.expectedQuantity = expectedQuantity;
  }
  
  public String getDescription() {
    return description;
  }
  
  public void setDescription(String description) {
    this.description = description;
  }
}
