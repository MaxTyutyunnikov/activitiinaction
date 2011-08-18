package org.bpmnwithactiviti.chapter7.javaexception;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.delegate.ActivityBehavior;
import org.activiti.engine.impl.pvm.delegate.ActivityExecution;

public class OrderPersistTask implements ActivityBehavior {
  
  @PersistenceContext
  EntityManager entityManager;

  @Override
  public void execute(ActivityExecution execution) throws Exception {
    PvmTransition transition = null;
    try {
      Order order = (Order) execution.getVariable("order");
      entityManager.persist(order);
      transition = execution.getActivity().findOutgoingTransition("orderPersisted");
    } catch(Throwable e) {
      transition = execution.getActivity().findOutgoingTransition("orderNotPersisted");
    }
    execution.take(transition);
  }
}
