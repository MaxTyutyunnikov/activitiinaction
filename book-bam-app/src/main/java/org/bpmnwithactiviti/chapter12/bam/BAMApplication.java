package org.bpmnwithactiviti.chapter12.bam;

import com.espertech.esper.client.EPAdministrator;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;
import com.github.wolfie.refresher.Refresher;
import com.vaadin.Application;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.ui.Label;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;

@SuppressWarnings("serial")
public class BAMApplication extends Application {

	private UpdateListener requestedAmountListener;
	private UpdateListener loanedAmountListener;
	private UpdateListener processDurationListener;
	private Label avgRequestedAmountLabel;
	
	@Override
	public void init() {
		System.out.println(">>> Starting the application");
		setMainWindow( new Window("Business Application Monitor"));
		avgRequestedAmountLabel = new Label();
		getMainWindow().addComponent(avgRequestedAmountLabel);
		startMonitor();
		Refresher refresher = new Refresher();
		refresher.setRefreshInterval(1000);
		getMainWindow().addComponent(refresher);
		getMainWindow().addListener(new Window.CloseListener() {
			@Override
			public void windowClose(CloseEvent e) {
				System.out.println(">>> Closing the application");
				EPAdministrator epAdmin = EPServiceProviderManager.getDefaultProvider().getEPAdministrator();
				epAdmin.getStatement(EsperStatementsCreator.REQUESTED_AMOUNT_STATEMENT_NAME).removeListener(requestedAmountListener);
				epAdmin.getStatement(EsperStatementsCreator.LOANED_AMOUNT_STATEMENT_NAME).removeListener(loanedAmountListener);
				epAdmin.getStatement(EsperStatementsCreator.PROCESS_DURATION_STATEMENT_NAME).removeListener(processDurationListener);
				getMainWindow().getApplication().close();
			}
		});
	}

	private void startMonitor() {
		EPAdministrator epAdmin = EPServiceProviderManager.getDefaultProvider().getEPAdministrator();
		
		// Start monitoring requested amount
		requestedAmountListener = new UpdateListener() {
			public void update(EventBean[] newEvents, EventBean[] oldEvents) {
				Double avgRequestedAmount = (Double) newEvents[0].get("avgRequestedAmount");
				Integer maxRequestedAmount = (Integer) newEvents[0].get("maxRequestedAmount");
				Integer sumRequestedAmount = (Integer) newEvents[0].get("sumRequestedAmount");
				if (avgRequestedAmount == null) {
					avgRequestedAmount = 0.0;
				}
				showMonitoredRequestedAmountInfo(avgRequestedAmount, maxRequestedAmount, sumRequestedAmount);
				avgRequestedAmountLabel.setValue(avgRequestedAmount);
			}
		};
		epAdmin.getStatement(EsperStatementsCreator.REQUESTED_AMOUNT_STATEMENT_NAME).addListenerWithReplay(requestedAmountListener);

		// Start monitoring loaned amount
		loanedAmountListener = new UpdateListener() {
			public void update(EventBean[] newEvents, EventBean[] oldEvents) {
				Long numLoans = (Long) newEvents[0].get("numLoans");
				Integer sumLoanedAmount = (Integer) newEvents[0].get("sumLoanedAmount");
				showMonitoredLoanedAmountInfo(numLoans, sumLoanedAmount);
			}
		};
		epAdmin.getStatement(EsperStatementsCreator.LOANED_AMOUNT_STATEMENT_NAME).addListenerWithReplay(loanedAmountListener);

		// Start monitoring process duration
		processDurationListener = new UpdateListener() {
			public void update(EventBean[] newEvents, EventBean[] oldEvents) {
				Double avgProcessDuration = (Double) newEvents[0].get("avgProcessDuration");
				Long maxProcessDuration = (Long) newEvents[0].get("maxProcessDuration");
				showMonitoredProcessDurationInfo(avgProcessDuration, maxProcessDuration);
			}
		};
		epAdmin.getStatement(EsperStatementsCreator.PROCESS_DURATION_STATEMENT_NAME).addListenerWithReplay(processDurationListener);
	}

	private void showMonitoredRequestedAmountInfo(Double avgRequestedAmount, Integer maxRequestedAmount, Integer sumRequestedAmount) {
		System.out.println("<<< avgRequestedAmount=" + avgRequestedAmount + ", maxRequestedAmount="
				+ maxRequestedAmount + ", sumRequestedAmount=" + sumRequestedAmount);
	}

	private void showMonitoredLoanedAmountInfo(Long numLoans, Integer sumLoanedAmount) {
		System.out.println("<<< numLoans=" + numLoans + ", sumLoanedAmount=" + sumLoanedAmount);
	}

	private void showMonitoredProcessDurationInfo(Double avgProcessDuration, Long maxProcessDuration) {
		System.out.println("<<< avgProcessDuration=" + avgProcessDuration + ", maxProcessDuration="
				+ maxProcessDuration);
	}
}
