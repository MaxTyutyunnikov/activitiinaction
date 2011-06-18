package org.bpmnwithactiviti.chapter12.bam;

import java.text.DecimalFormat;

import org.apache.log4j.Logger;
import org.vaadin.vaadinvisualizations.Gauge;
import org.vaadin.vaadinvisualizations.PieChart;

import com.espertech.esper.client.EPAdministrator;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;
import com.github.wolfie.refresher.Refresher;
import com.vaadin.Application;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;

@SuppressWarnings("serial")
public class BAMApplication extends Application {

	private static final Logger log = Logger.getLogger(BAMApplication.class);

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
	private Panel requstedAmountPanel;
	private Gauge gauge;

	private static final DecimalFormat avgRequestedAmountformatter = new DecimalFormat("##0.0");
	private static final DecimalFormat avgProcessDurationformatter = new DecimalFormat("###0");

	@Override
	public void init() {
		log.info("Starting BAM application");
		initGraphicalVisualization();
	}

	private void initGraphicalVisualization() {
		setMainWindow(new Window("Business Application Monitor"));
		
		Refresher refresher = new Refresher();
		refresher.setRefreshInterval(1000);
		getMainWindow().addComponent(refresher);

		getMainWindow().addListener(new Window.CloseListener() {
			@Override
			public void windowClose(CloseEvent e) {
				log.info("Closing BAM application");
				EPAdministrator epAdmin = EPServiceProviderManager.getDefaultProvider().getEPAdministrator();
				epAdmin.getStatement(EsperStatementsCreator.REQUESTED_AMOUNT_STATEMENT_NAME).removeListener(requestedAmountListener);
				getMainWindow().getApplication().close();
			}
		});
		
	
		requstedAmountPanel = new Panel("Requested Amount");
		VerticalLayout layout = (VerticalLayout) requstedAmountPanel.getContent();
		layout.setMargin(true);
		layout.setSpacing(true);
		layout.setSizeFull();
		requstedAmountPanel.setHeight("200px");
		requstedAmountPanel.setWidth("436px");

		getMainWindow().addComponent(requstedAmountPanel);

		// Create the Esper event listeners and hook them up to the labels
		EPAdministrator epAdmin = EPServiceProviderManager.getDefaultProvider().getEPAdministrator();
		requestedAmountListener = new UpdateListener() {
			public void update(EventBean[] newEvents, EventBean[] oldEvents) {
				if (gauge != null) {
					requstedAmountPanel.removeComponent(gauge);
				}
				createGauge((Double) newEvents[0].get("avgRequestedAmount"), (Integer)newEvents[0].get("maxRequestedAmount"), (Integer)newEvents[0].get("sumRequestedAmount"));
				requstedAmountPanel.addComponent(gauge);
			}
		};
		epAdmin.getStatement(EsperStatementsCreator.REQUESTED_AMOUNT_STATEMENT_NAME).addListenerWithReplay(requestedAmountListener);
	}

	private void createGauge(Double avgRequestedAmount, Integer maxRequestedAmount, Integer sumRequestedAmount) {
		gauge = new Gauge();
		gauge.setOption("redFrom", 90);
		gauge.setOption("redTo", 100);
		gauge.setOption("yellowFrom", 75);
		gauge.setOption("yellowTo", 90);
		gauge.setOption("minorTicks", 20);
		gauge.setSizeFull();
		gauge.add("avg", (avgRequestedAmount!=null)?avgRequestedAmount.intValue():0);
		gauge.add("max", (maxRequestedAmount!=null)?maxRequestedAmount:0);
		gauge.add("sum", (sumRequestedAmount!=null)?sumRequestedAmount:0);
	}

	@SuppressWarnings("unused")
	private void initTextualPresentation() {
		setMainWindow(new Window("Business Application Monitor"));

		GridLayout layout = new GridLayout(2, 7);
		layout.setSpacing(true);
		addLabelAndValue(layout, "Average requested amount", avgRequestedAmountLabel);
		addLabelAndValue(layout, "Maximum requested amount", maxRequestedAmountLabel);
		addLabelAndValue(layout, "Sum of requested amount", sumRequestedAmountLabel);
		addLabelAndValue(layout, "Number of loans", numLoansLabel);
		addLabelAndValue(layout, "Sum of loaned amount", sumLoanedAmountLabel);
		addLabelAndValue(layout, "Average process duration", avgProcessDurationLabel);
		addLabelAndValue(layout, "Maximum process duration", maxProcessDurationLabel);
		getMainWindow().addComponent(layout);

		Refresher refresher = new Refresher();
		refresher.setRefreshInterval(1000);
		getMainWindow().addComponent(refresher);

		getMainWindow().addListener(new Window.CloseListener() {
			@Override
			public void windowClose(CloseEvent e) {
				log.info("Closing BAM application");
				EPAdministrator epAdmin = EPServiceProviderManager.getDefaultProvider().getEPAdministrator();
				epAdmin.getStatement(EsperStatementsCreator.REQUESTED_AMOUNT_STATEMENT_NAME).removeListener(
						requestedAmountListener);
				epAdmin.getStatement(EsperStatementsCreator.LOANED_AMOUNT_STATEMENT_NAME).removeListener(
						loanedAmountListener);
				epAdmin.getStatement(EsperStatementsCreator.PROCESS_DURATION_STATEMENT_NAME).removeListener(
						processDurationListener);
				getMainWindow().getApplication().close();
			}
		});

		// Create the Esper event listeners and hook them up to the labels
		EPAdministrator epAdmin = EPServiceProviderManager.getDefaultProvider().getEPAdministrator();
		requestedAmountListener = new UpdateListener() {
			public void update(EventBean[] newEvents, EventBean[] oldEvents) {
				Object avgRequestedAmount = newEvents[0].get("avgRequestedAmount");
				avgRequestedAmountLabel.setValue((avgRequestedAmount != null) ? avgRequestedAmountformatter
						.format(avgRequestedAmount) : "");
				maxRequestedAmountLabel.setValue(newEvents[0].get("maxRequestedAmount"));
				sumRequestedAmountLabel.setValue(newEvents[0].get("sumRequestedAmount"));
			}
		};
		epAdmin.getStatement(EsperStatementsCreator.REQUESTED_AMOUNT_STATEMENT_NAME).addListenerWithReplay(
				requestedAmountListener);

		// Start monitoring loaned amount
		loanedAmountListener = new UpdateListener() {
			public void update(EventBean[] newEvents, EventBean[] oldEvents) {
				numLoansLabel.setValue(newEvents[0].get("numLoans"));
				sumLoanedAmountLabel.setValue(newEvents[0].get("sumLoanedAmount"));
			}
		};
		epAdmin.getStatement(EsperStatementsCreator.LOANED_AMOUNT_STATEMENT_NAME).addListenerWithReplay(
				loanedAmountListener);

		// Start monitoring process duration
		processDurationListener = new UpdateListener() {
			public void update(EventBean[] newEvents, EventBean[] oldEvents) {
				Object avgProcessDuration = newEvents[0].get("avgProcessDuration");
				avgProcessDurationLabel.setValue((avgProcessDuration != null) ? avgProcessDurationformatter
						.format(avgProcessDuration) : "");
				maxProcessDurationLabel.setValue(newEvents[0].get("maxProcessDuration"));
			}
		};
		epAdmin.getStatement(EsperStatementsCreator.PROCESS_DURATION_STATEMENT_NAME).addListenerWithReplay(
				processDurationListener);
	}

	private void addLabelAndValue(GridLayout layout, String labelText, Component value) {
		Label label = new Label(labelText + ":");
		layout.addComponent(label);
		layout.setComponentAlignment(label, Alignment.MIDDLE_RIGHT);
		layout.addComponent(value);
		layout.setComponentAlignment(value, Alignment.MIDDLE_RIGHT);
	}
}
