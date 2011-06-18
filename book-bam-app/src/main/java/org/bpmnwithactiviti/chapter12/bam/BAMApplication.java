package org.bpmnwithactiviti.chapter12.bam;

import java.text.DecimalFormat;

import org.apache.log4j.Logger;
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

	private static final DecimalFormat avgRequestedAmountformatter = new DecimalFormat("##0.0");
	private static final DecimalFormat avgProcessDurationformatter = new DecimalFormat("###0");

	@Override
	public void init() {
		log.info("Starting BAM application");
		initGraphicalVisualization();
	}

	private void initGraphicalVisualization() {
		Window mainWindow = new Window("Testvisualizations Application");
		Label label = new Label("Hello Vaadin user");
		mainWindow.addComponent(label);
		PieChart pc = new PieChart();
		pc.setSizeFull();
		pc.add("Work", 7);
		pc.add("Play", 3);
		pc.add("Eat", 1);
		pc.add("Sleep", 6);
		pc.add("Do Vaadin", 7);
		pc.setOption("width", 400);
		pc.setOption("height", 400);
		pc.setOption("is3D", true);
		mainWindow.addWindow(createSubWindow(pc, "Pie Chart", "536px", "436px"));
		setMainWindow(mainWindow);
	}

	private Window createSubWindow(Component component, String type, String height, String width) {
		Window subwindow = new Window("A subwindow showing " + type);
		// Configure the windws layout; by default a VerticalLayout
		VerticalLayout layout = (VerticalLayout) subwindow.getContent();
		layout.setMargin(true);
		layout.setSpacing(true);
		layout.setSizeFull();
		// layout.setHeight("400px");
		// layout.setWidth("400px");
		subwindow.setHeight(height);
		subwindow.setWidth(width);
		subwindow.addComponent(component);
		return subwindow;
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
