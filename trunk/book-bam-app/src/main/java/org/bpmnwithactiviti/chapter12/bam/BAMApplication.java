package org.bpmnwithactiviti.chapter12.bam;

import com.espertech.esper.client.EPAdministrator;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;
import com.github.wolfie.refresher.Refresher;
import com.vaadin.Application;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;

@SuppressWarnings("serial")
public class BAMApplication extends Application {

	private UpdateListener requestedAmountListener;
	private UpdateListener loanedAmountListener;
	private UpdateListener processDurationListener;
	private Label avgRequestedAmountLabel = new Label();
	private Label maxRequestedAmountLabel = new Label();
	private Label sumRequestedAmountLabel = new Label();
	private Label numLoansLabel = new Label();
	private Label sumLoanedAmountLabel = new Label();
	private Label avgProcessDurationLabel = new Label();
	private Label maxProcessDurationLabel = new Label();
	
	@Override
	public void init() {
		System.out.println(">>> Starting the application");
		setMainWindow( new Window("Business Application Monitor"));
		
		HorizontalLayout layout = new HorizontalLayout();
		layout.addComponent(new Label("Average requested amount:"));
		layout.addComponent(avgRequestedAmountLabel);
		getMainWindow().addComponent(layout);
		
		layout = new HorizontalLayout();
		layout.addComponent(new Label("Maximum requested amount:"));
		layout.addComponent(maxRequestedAmountLabel);
		getMainWindow().addComponent(layout);
		
		layout = new HorizontalLayout();
		layout.addComponent(new Label("Sum of requested amount:"));
		layout.addComponent(sumRequestedAmountLabel);
		getMainWindow().addComponent(layout);
		
		layout = new HorizontalLayout();
		layout.addComponent(new Label("Number of loans:"));
		layout.addComponent(numLoansLabel);
		getMainWindow().addComponent(layout);
		
		layout = new HorizontalLayout();
		layout.addComponent(new Label("Sum of loaned amount:"));
		layout.addComponent(sumLoanedAmountLabel);
		getMainWindow().addComponent(layout);
		
		layout = new HorizontalLayout();
		layout.addComponent(new Label("Average process duration:"));
		layout.addComponent(avgProcessDurationLabel);
		getMainWindow().addComponent(layout);
		
		layout = new HorizontalLayout();
		layout.addComponent(new Label("maximum process duration:"));
		layout.addComponent(maxProcessDurationLabel);
		getMainWindow().addComponent(layout);
		
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
		
		// Create the Esper event listeners and hook them up to the labels
		EPAdministrator epAdmin = EPServiceProviderManager.getDefaultProvider().getEPAdministrator();
		requestedAmountListener = new UpdateListener() {
			public void update(EventBean[] newEvents, EventBean[] oldEvents) {
				avgRequestedAmountLabel.setValue(newEvents[0].get("avgRequestedAmount"));
				maxRequestedAmountLabel.setValue(newEvents[0].get("maxRequestedAmount"));
				sumRequestedAmountLabel.setValue(newEvents[0].get("sumRequestedAmount"));
			}
		};
		epAdmin.getStatement(EsperStatementsCreator.REQUESTED_AMOUNT_STATEMENT_NAME).addListenerWithReplay(requestedAmountListener);

		// Start monitoring loaned amount
		loanedAmountListener = new UpdateListener() {
			public void update(EventBean[] newEvents, EventBean[] oldEvents) {
				numLoansLabel.setValue(newEvents[0].get("numLoans"));
				sumLoanedAmountLabel.setValue(newEvents[0].get("sumLoanedAmount"));
			}
		};
		epAdmin.getStatement(EsperStatementsCreator.LOANED_AMOUNT_STATEMENT_NAME).addListenerWithReplay(loanedAmountListener);

		// Start monitoring process duration
		processDurationListener = new UpdateListener() {
			public void update(EventBean[] newEvents, EventBean[] oldEvents) {
				Double avgProcessDuration = (Double) newEvents[0].get("avgProcessDuration");
				Long maxProcessDuration = (Long) newEvents[0].get("maxProcessDuration");
				showMonitoredProcessDurationInfo(avgProcessDuration, maxProcessDuration);
				avgProcessDurationLabel.setValue(newEvents[0].get("avgProcessDuration"));
				maxProcessDurationLabel.setValue(newEvents[0].get("maxProcessDuration"));
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
