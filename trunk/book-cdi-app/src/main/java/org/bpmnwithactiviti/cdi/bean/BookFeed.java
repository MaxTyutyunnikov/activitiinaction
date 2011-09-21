package org.bpmnwithactiviti.cdi.bean;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Named;

@ApplicationScoped
public class BookFeed {

  private List<String> approvedOrders = new ArrayList<String>();
  private List<String> notApprovedOrders = new ArrayList<String>();

  public void addApprovedIsbn(String isbn) {
  	approvedOrders.add(isbn);
  }
  
  public void addNotApprovedIsbn(String isbn) {
  	notApprovedOrders.add(isbn);
  }

  @Produces
  @Named("approvedOrders")
  public List<String> getApprovedOrders() {
    return approvedOrders;
  }
  
  @Produces
  @Named("notApprovedOrders")
  public List<String> getNotApprovedOrders() {
    return notApprovedOrders;
  }

}
