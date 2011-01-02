package org.bpmnwithactiviti.chapter6;

import javax.transaction.UserTransaction;

import org.activiti.engine.RuntimeService;
import org.objectweb.jotm.Jotm;
import org.objectweb.transaction.jta.TMService;

public class JTABean {

	private RuntimeService runtimeService;

	public JTABean(RuntimeService rt) {
		this.runtimeService = rt;
	}

	public void doingTransactionalStuff(boolean error) throws Exception {
		TMService jotm = new Jotm(true, false);
		UserTransaction ut = jotm.getUserTransaction();
		ut.begin();
		System.out.println("In JTABean doingTransactionalStuff, error = "
						+ error
						+ " transaction is available = "
						+ ut.getStatus());
		runtimeService.startProcessInstanceByKey("transactionTest");
		if (error) {
			throw new UnsupportedOperationException("Rollback needed!!");
		}
		ut.commit();
	}

//	public static final int	STATUS_ACTIVE	0
//	public static final int	STATUS_COMMITTED	3
//	public static final int	STATUS_COMMITTING	8
//	public static final int	STATUS_MARKED_ROLLBACK	1
//	public static final int	STATUS_NO_TRANSACTION	6
//	public static final int	STATUS_PREPARED	2
//	public static final int	STATUS_PREPARING	7
//	public static final int	STATUS_ROLLEDBACK	4
//	public static final int	STATUS_ROLLING_BACK	9
//	public static final int	STATUS_UNKNOWN	5

	
}
