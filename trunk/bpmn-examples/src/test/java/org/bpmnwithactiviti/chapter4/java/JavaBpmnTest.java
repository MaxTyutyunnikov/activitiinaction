package org.bpmnwithactiviti.chapter4.java;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.impl.ProcessEngineImpl;
import org.activiti.engine.impl.jobexecutor.JobExecutor;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.junit.Rule;
import org.junit.Test;

public class JavaBpmnTest {
	
	@Rule 
	public ActivitiRule activitiRule = new ActivitiRule("activiti.cfg-mem.xml");
	
	private ProcessInstance startProcessInstance() {
		RuntimeService runtimeService = activitiRule.getRuntimeService();
		Map<String, Object> variableMap = new HashMap<String, Object>();
		variableMap.put("isbn", 123456L);
		return runtimeService.startProcessInstanceByKey("bookorder", variableMap);
	}
	
	@Test
	@Deployment(resources={"chapter4/bookorder.java.bpmn20.xml"})
	public void executeJavaService() {
		ProcessInstance processInstance = startProcessInstance();
		RuntimeService runtimeService = activitiRule.getRuntimeService();
		Date validatetime = (Date) runtimeService.getVariable(processInstance.getId(), "validatetime");
		assertNotNull(validatetime);
		System.out.println("validatetime is " + validatetime);
	}
	
	@Test
	@Deployment(resources={"chapter4/bookorder.async.bpmn20.xml"})
	public void executeAsyncService() {
	    JobExecutor jobExecutor = ((ProcessEngineImpl) activitiRule.getProcessEngine()).getProcessEngineConfiguration().getJobExecutor();
	    jobExecutor.start();
		ProcessInstance processInstance = startProcessInstance();
		System.out.println("Started process instance");
		assertEquals(false, waitForJobExecutorToProcessAllJobs(5000, 100));
		RuntimeService runtimeService = activitiRule.getRuntimeService();
		Date validatetime = (Date) runtimeService.getVariable(processInstance.getId(), "validatetime");
		assertNotNull(validatetime);
		System.out.println("validatetime is " + validatetime);
	    jobExecutor.shutdown();
	}
	
	@Test
	@Deployment(resources={"chapter4/bookorder.java.field.bpmn20.xml"})
	public void executeJavaServiceWithExtensions() {
		ProcessInstance processInstance = startProcessInstance();
		RuntimeService runtimeService = activitiRule.getRuntimeService();
		Date validatetime = (Date) runtimeService.getVariable(processInstance.getId(), "validatetime");
		assertNotNull(validatetime);
		System.out.println("validatetime is " + validatetime);
	}
	
	@Test
	@Deployment(resources={"chapter4/bookorder.java.expression.bpmn20.xml"})
	public void executeJavaExpression() {
		RuntimeService runtimeService = activitiRule.getRuntimeService();
		BookOrder bookOrder = new BookOrder();
		Map<String, Object> variableMap = new HashMap<String, Object>();
		variableMap.put("isbn", 123456L);
		variableMap.put("bookOrder", bookOrder);
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("bookorder", variableMap);
		Date validatetime = (Date) runtimeService.getVariable(processInstance.getId(), "validatetime");
		assertNotNull(validatetime);
		System.out.println("validatetime is " + validatetime);
	}
	
	private boolean waitForJobExecutorToProcessAllJobs(long maxMillisToWait, long intervalMillis) {
      Timer timer = new Timer();
      InteruptTask task = new InteruptTask(Thread.currentThread());
      timer.schedule(task, maxMillisToWait);
      boolean areJobsAvailable = true;
      try {
        while (areJobsAvailable && !task.isTimeLimitExceeded()) {
          Thread.sleep(intervalMillis);
          areJobsAvailable = areJobsAvailable();
        }
      } catch (InterruptedException e) {
      } finally {
        timer.cancel();
      }
      return areJobsAvailable;
  }
	
	public boolean areJobsAvailable() {
    return !activitiRule.getManagementService()
      .createJobQuery()
      .executable()
      .list()
      .isEmpty();
  }

  private static class InteruptTask extends TimerTask {
    protected boolean timeLimitExceeded = false;
    protected Thread thread;
    public InteruptTask(Thread thread) {
      this.thread = thread;
    }
    public boolean isTimeLimitExceeded() {
      return timeLimitExceeded;
    }
    public void run() {
      timeLimitExceeded = true;
      thread.interrupt();
    }
  }
}
