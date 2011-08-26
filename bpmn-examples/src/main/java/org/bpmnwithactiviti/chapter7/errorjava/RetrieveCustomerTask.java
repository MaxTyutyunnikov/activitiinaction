package org.bpmnwithactiviti.chapter7.errorjava;

import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.delegate.ActivityBehavior;
import org.activiti.engine.impl.pvm.delegate.ActivityExecution;
import org.bpmnwithactiviti.chapter7.ws.Customer;
import org.bpmnwithactiviti.chapter7.ws.CustomerServiceService;

public class RetrieveCustomerTask implements ActivityBehavior {
  
  private CustomerServiceService customerService = new CustomerServiceService();

  @Override
  public void execute(ActivityExecution execution) throws Exception {
    Long customerId = (Long) execution.getVariable("customerNumber");
    Customer customer = null;
    if(customerId > 0) {
      customer = customerService.getCustomerServicePort().findCustomerById(customerId);
    } else {
      String customerName = (String) execution.getVariable("customerName");
      String contactPerson = (String) execution.getVariable("contactPerson");
      customer = customerService.getCustomerServicePort().findCustomer(customerName, contactPerson);
    }
    CustomerVariable variable = new CustomerVariable();
    if(customer != null && customer.getCustomerId() > 0) {
      variable.setCustomerFound(true);
      variable.setCustomerId(customer.getCustomerId());
      variable.setCustomerName(customer.getCustomerName());
      variable.setContactPerson(customer.getContactPerson());
      variable.setCustomerAddress(customer.getCustomerAddress());
    } else {
      variable.setCustomerFound(false);
    }
    execution.setVariable("customer", variable);
    PvmTransition transition = null;
    if(variable.isCustomerFound() == true) {
      transition = execution.getActivity().findOutgoingTransition("customerFound");
    } else {
      transition = execution.getActivity().findOutgoingTransition("customerNotFound");
    }
    execution.take(transition);
  }

}
