package org.bpmnwithactiviti.chapter7.javaexception;

import javax.persistence.Id;

public class Order {
  
  @Id
  private int orderId;
  private String customerName;
  private String productName;
  private int quantity;
  
  public int getOrderId() {
    return orderId;
  }
  
  public void setOrderId(int orderId) {
    this.orderId = orderId;
  }
  
  public String getCustomerName() {
    return customerName;
  }
  
  public void setCustomerName(String customerName) {
    this.customerName = customerName;
  }
  
  public String getProductName() {
    return productName;
  }
  
  public void setProductName(String productName) {
    this.productName = productName;
  }
  
  public int getQuantity() {
    return quantity;
  }
  
  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }
}
