package org.bpmnwithactiviti.chapter12.bam;

import java.text.DecimalFormat;

import org.apache.log4j.Logger;
import org.vaadin.vaadinvisualizations.Gauge;

import com.espertech.esper.client.EPAdministrator;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;
import com.github.wolfie.refresher.Refresher;
import com.vaadin.Application;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;

@SuppressWarnings("serial")
public class BAMApplication extends Application {

	private static final Logger log = Logger.getLogger(BAMApplication.class);

	private static final DecimalFormat avgRequestedAmountformatter = new DecimalFormat("##0.0");
	private static final DecimalFormat avgProcessDurationformatter = new DecimalFormat("###0");

	private UpdateListener requestedAmountListener;
	private UpdateListener loanedAmountListener;
	private UpdateListener processDurationListener;
	
	Panel loanInfoPanel;
	Panel processInfoPanel;
	
	private Label avgRequestedAmountLabel = new Label();
	private Label maxRequestedAmountLabel = new Label();
	private Label sumRequestedAmountLabel = new Label();
	private Label numLoansLabel = new Label();
	private Label sumLoanedAmountLabel = new Label();
	private Label avgProcessDurationLabel = new Label();
	private Label maxProcessDurationLabel = new Label();

	@Override
	public void init() {
		log.info("Starting BAM application");
		setMainWindow(new Window("Business Application Monitor"));
		GridLayout mainWindowLayout = new GridLayout(2,3);
		getMainWindow().setContent(mainWindowLayout);
		mainWindowLayout.setSizeFull();
		mainWindowLayout.setMargin(true);
		// mainWindowLayout.setSpacing(true);
		
		// Add the window refresher component
		Refresher refresher = new Refresher();
		refresher.setRefreshInterval(1000);
		mainWindowLayout.addComponent(refresher,0,0);

		// Add a listener for when the window is closed by the user.
		// This listener removes the Esper statement listeners from the Esper statements
		getMainWindow().addListener(new Window.CloseListener() {
			@Override
			public void windowClose(CloseEvent e) {
				log.info("Closing BAM application");
				EPAdministrator epAdmin = EPServiceProviderManager.getDefaultProvider().getEPAdministrator();
				epAdmin.getStatement(EsperStatementsCreator.REQUESTED_AMOUNT_STATEMENT_NAME).removeListener(requestedAmountListener);
				getMainWindow().getApplication().close();
			}
		});
		
		// Panel containing 2 gauges with loan information.
		loanInfoPanel = new Panel("Loan Information");
		loanInfoPanel.setHeight("170px");
		loanInfoPanel.setWidth("300px");
		HorizontalLayout horizontalLayout = new HorizontalLayout();
		loanInfoPanel.setContent(horizontalLayout);
		horizontalLayout.setMargin(true);
		horizontalLayout.setSpacing(true);
		horizontalLayout.setSizeFull();
		mainWindowLayout.addComponent(loanInfoPanel,0,1);

		// Panel containing 2 gauges with process information.
		processInfoPanel = new Panel("Process Information");
		processInfoPanel.setHeight("170px");
		processInfoPanel.setWidth("300px");
		horizontalLayout = new HorizontalLayout();
		processInfoPanel.setContent(horizontalLayout);
		horizontalLayout.setMargin(true);
		horizontalLayout.setSpacing(true);
		horizontalLayout.setSizeFull();
		mainWindowLayout.addComponent(processInfoPanel,0,2);
		
		// Panel containing a textual representation
		Panel textualRepresentationPanel = new Panel("Textual Representation");
		// textualRepresentationPanel.setHeight("90%");
		textualRepresentationPanel.setWidth("300px");
		GridLayout gridLayout = new GridLayout(2, 7);
		textualRepresentationPanel.setContent(gridLayout);
		gridLayout.setMargin(true);
		gridLayout.setSpacing(true);
		addLabelAndValue(gridLayout, "Average requested amount", avgRequestedAmountLabel);
		addLabelAndValue(gridLayout, "Maximum requested amount", maxRequestedAmountLabel);
		addLabelAndValue(gridLayout, "Sum of requested amount", sumRequestedAmountLabel);
		addLabelAndValue(gridLayout, "Number of loans", numLoansLabel);
		addLabelAndValue(gridLayout, "Sum of loaned amount", sumLoanedAmountLabel);
		addLabelAndValue(gridLayout, "Average process duration", avgProcessDurationLabel);
		addLabelAndValue(gridLayout, "Maximum process duration", maxProcessDurationLabel);
		mainWindowLayout.addComponent(textualRepresentationPanel,1,1,1,2);

		// Monitor requested amount
		requestedAmountListener = new UpdateListener() {
			public void update(EventBean[] newEvents, EventBean[] oldEvents) {
				Double  avgRequestedAmount = (Double) newEvents[0].get("avgRequestedAmount");
				Integer maxRequestedAmount = (Integer)newEvents[0].get("maxRequestedAmount");
				Integer sumRequestedAmount = (Integer)newEvents[0].get("sumRequestedAmount");
				
				avgRequestedAmountLabel.setValue((avgRequestedAmount!=null) ? avgRequestedAmountformatter.format(avgRequestedAmount) : "");
				maxRequestedAmountLabel.setValue(maxRequestedAmount);
				sumRequestedAmountLabel.setValue(sumRequestedAmount);
			}
		};
		EPAdministrator epAdmin = EPServiceProviderManager.getDefaultProvider().getEPAdministrator();
		epAdmin.getStatement(EsperStatementsCreator.REQUESTED_AMOUNT_STATEMENT_NAME).addListenerWithReplay(requestedAmountListener);

		// Monitor loans
		loanedAmountListener = new UpdateListener() {
			public void update(EventBean[] newEvents, EventBean[] oldEvents) {
				Long numLoans = (Long) newEvents[0].get("numLoans");
				Integer sumLoanedAmount = (Integer) newEvents[0].get("sumLoanedAmount");
				
				numLoansLabel.setValue(numLoans);
				sumLoanedAmountLabel.setValue(sumLoanedAmount);
				
				loanInfoPanel.removeAllComponents();
				
				Gauge numLoansGauge= new Gauge();
				numLoansGauge.setOption("redFrom", 0);
				numLoansGauge.setOption("redTo", 10);
				numLoansGauge.setOption("yellowFrom", 10);
				numLoansGauge.setOption("yellowTo", 30);
				numLoansGauge.setOption("minorTicks",5);
				numLoansGauge.setSizeFull();
				numLoansGauge.add("#", numLoans*10);
				
				Gauge sumLoanedAmountGauge= new Gauge();
				sumLoanedAmountGauge.setOption("redFrom", 0);
				sumLoanedAmountGauge.setOption("redTo", 30);
				sumLoanedAmountGauge.setOption("yellowFrom", 30);
				sumLoanedAmountGauge.setOption("yellowTo", 60);
				sumLoanedAmountGauge.setOption("minorTicks",10);
				sumLoanedAmountGauge.setSizeFull();
				sumLoanedAmountGauge.add("€", (sumLoanedAmount!=null)?sumLoanedAmount/10:0);
				
				loanInfoPanel.addComponent(numLoansGauge);
				loanInfoPanel.addComponent(sumLoanedAmountGauge);
			}
		};
		epAdmin.getStatement(EsperStatementsCreator.LOANED_AMOUNT_STATEMENT_NAME).addListenerWithReplay(loanedAmountListener);

		// Monitor process duration
		processDurationListener = new UpdateListener() {
			public void update(EventBean[] newEvents, EventBean[] oldEvents) {
				Double avgProcessDuration = (Double) newEvents[0].get("avgProcessDuration");
				Long maxProcessDuration = (Long) newEvents[0].get("maxProcessDuration");
				
				avgProcessDurationLabel.setValue((avgProcessDuration!=null) ? avgProcessDurationformatter.format(avgProcessDuration) : "");
				maxProcessDurationLabel.setValue(maxProcessDuration);

				processInfoPanel.removeAllComponents();
				
				Gauge avgProcessDurationGauge= new Gauge();
				avgProcessDurationGauge.setOption("yellowFrom", 70);
				avgProcessDurationGauge.setOption("yellowTo", 85);
				avgProcessDurationGauge.setOption("redFrom", 85);
				avgProcessDurationGauge.setOption("redTo", 100);
				avgProcessDurationGauge.setOption("minorTicks",5);
				avgProcessDurationGauge.setSizeFull();
				avgProcessDurationGauge.add("avg", (avgProcessDuration!=null) ? new Double(avgProcessDuration/20).intValue() : 0);
				
				Gauge maxProcessDurationGauge= new Gauge();
				maxProcessDurationGauge.setOption("yellowFrom", 70);
				maxProcessDurationGauge.setOption("yellowTo", 85);
				maxProcessDurationGauge.setOption("redFrom", 85);
				maxProcessDurationGauge.setOption("redTo", 100);
				maxProcessDurationGauge.setOption("minorTicks",5);
				maxProcessDurationGauge.setSizeFull();
				maxProcessDurationGauge.add("max", (maxProcessDuration!=null) ? maxProcessDuration/20 : 0);
				
				processInfoPanel.addComponent(avgProcessDurationGauge);
				processInfoPanel.addComponent(maxProcessDurationGauge);
			}
		};
		epAdmin.getStatement(EsperStatementsCreator.PROCESS_DURATION_STATEMENT_NAME).addListenerWithReplay(	processDurationListener);
	}

	private void addLabelAndValue(GridLayout layout, String labelText, Component value) {
		Label label = new Label(labelText + ":");
		layout.addComponent(label);
		layout.setComponentAlignment(label, Alignment.MIDDLE_RIGHT);
		layout.addComponent(value);
		layout.setComponentAlignment(value, Alignment.MIDDLE_RIGHT);
	}
}
