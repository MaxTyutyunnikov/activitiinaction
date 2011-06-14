package org.bpmnwithactiviti.chapter12.bam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.espertech.esper.client.EPAdministrator;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.SafeIterator;
import com.espertech.esper.client.UpdateListener;
import com.vaadin.Application;
import com.vaadin.terminal.gwt.server.HttpServletRequestListener;

public class BAMApplication extends Application implements HttpServletRequestListener {

	protected static final long serialVersionUID = 6197397757268207621L;

	protected static final String TITLE = "Activiti KickStart";
	protected static final String THEME_NAME = "yakalo";
	protected static final String CONTENT_LOCATION = "content";

	@Override
	public void onRequestStart(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		startMonitor();
	}

	@Override
	public void onRequestEnd(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
	}

	private void startMonitor() {
		EPAdministrator epAdmin = EPServiceProviderManager.getDefaultProvider().getEPAdministrator();
		// Start monitoring requested amount
		EPStatement epStatement = epAdmin.getStatement(EsperStatementsCreator.REQUESTED_AMOUNT_STATEMENT_NAME);
		SafeIterator<EventBean> eventIterator = epStatement.safeIterator();
		try {
			EventBean event = eventIterator.next();
			Double avgRequestedAmount = (Double) event.get("avgRequestedAmount");
			Integer maxRequestedAmount = (Integer) event.get("maxRequestedAmount");
			Integer sumRequestedAmount = (Integer) event.get("sumRequestedAmount");
			showMonitoredRequestedAmountInfo(avgRequestedAmount, maxRequestedAmount, sumRequestedAmount);
		} finally {
			eventIterator.close(); // Note: safe iterators must be closed
		}

		epStatement.addListener(new UpdateListener() {
			public void update(EventBean[] newEvents, EventBean[] oldEvents) {
				Double avgRequestedAmount = (Double) newEvents[0].get("avgRequestedAmount");
				Integer maxRequestedAmount = (Integer) newEvents[0].get("maxRequestedAmount");
				Integer sumRequestedAmount = (Integer) newEvents[0].get("sumRequestedAmount");
				showMonitoredRequestedAmountInfo(avgRequestedAmount, maxRequestedAmount, sumRequestedAmount);
			}
		});

		// Start monitoring loaned amount
		epStatement = epAdmin.getStatement(EsperStatementsCreator.LOANED_AMOUNT_STATEMENT_NAME);
		eventIterator = epStatement.safeIterator();
		try {
			EventBean event = eventIterator.next();
			Long numLoans = (Long) event.get("numLoans");
			Integer sumLoanedAmount = (Integer) event.get("sumLoanedAmount");
			showMonitoredLoanedAmountInfo(numLoans, sumLoanedAmount);
		} finally {
			eventIterator.close(); // Note: safe iterators must be closed
		}

		epStatement.addListener(new UpdateListener() {
			public void update(EventBean[] newEvents, EventBean[] oldEvents) {
				Long numLoans = (Long) newEvents[0].get("numLoans");
				Integer sumLoanedAmount = (Integer) newEvents[0].get("sumLoanedAmount");
				showMonitoredLoanedAmountInfo(numLoans, sumLoanedAmount);
			}
		});

		// Start monitoring process duration
		epStatement = epAdmin.getStatement(EsperStatementsCreator.PROCESS_DURATION_STATEMENT_NAME);
		eventIterator = epStatement.safeIterator();
		try {
			EventBean event = eventIterator.next();
			Double avgProcessDuration = (Double) event.get("avgProcessDuration");
			Long maxProcessDuration = (Long) event.get("maxProcessDuration");
			showMonitoredProcessDurationInfo(avgProcessDuration, maxProcessDuration);
		} finally {
			eventIterator.close(); // Note: safe iterators must be closed
		}

		epStatement.addListener(new UpdateListener() {
			public void update(EventBean[] newEvents, EventBean[] oldEvents) {
				Double avgProcessDuration = (Double) newEvents[0].get("avgProcessDuration");
				Long maxProcessDuration = (Long) newEvents[0].get("maxProcessDuration");
				showMonitoredProcessDurationInfo(avgProcessDuration, maxProcessDuration);
			}
		});
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

	/*
	 * // ui protected ViewManager viewManager; protected CustomLayout
	 * mainLayout; // general layout of the app protected HorizontalSplitPanel
	 * splitPanel; // app uses a split panel: left actions, right work area
	 * protected ActionsPanel actionsPanel; // left panel with user actions
	 * protected Panel currentWorkArea; // right panel of ui where actual work
	 * happens protected ContextHelper context;
	 * 
	 * // HttpServletRequestListener
	 * -------------------------------------------------------------------
	 * 
	 * public void onRequestStart(HttpServletRequest request,
	 * HttpServletResponse response) { if(getMainWindow() == null) {
	 * setTheme(THEME_NAME); initMainWindow(); } }
	 * 
	 * @Override public void onRequestEnd(HttpServletRequest request,
	 * HttpServletResponse response) { }
	 * 
	 * @Override public void init() { context = new ContextHelper(this);
	 * context.getRepositoryService().createDeployment() .addClasspathResource(
	 * "org/bpmn20withactiviti/chapter8/workflow/process/bookwriting.bpmn20.xml"
	 * ) .deploy(); }
	 * 
	 * protected void initMainWindow() { mainLayout = new
	 * CustomLayout(THEME_NAME); // uses layout defined in //
	 * webapp/Vaadin/themes/yakalo mainLayout.setSizeFull();
	 * 
	 * Window mainWindow = new Window(TITLE); setMainWindow(mainWindow);
	 * mainWindow.setContent(mainLayout);
	 * 
	 * initSplitPanel(); initViewManager(); initActionsPanel(); }
	 * 
	 * protected void initSplitPanel() { splitPanel = new
	 * HorizontalSplitPanel(); splitPanel.setSplitPosition(170,
	 * Sizeable.UNITS_PIXELS); splitPanel.setStyleName(Reindeer.LAYOUT_WHITE);
	 * splitPanel.setSizeFull();
	 * 
	 * mainLayout.addComponent(splitPanel, CONTENT_LOCATION); }
	 * 
	 * protected void initActionsPanel() { this.actionsPanel = new
	 * ActionsPanel(viewManager); splitPanel.setFirstComponent(actionsPanel); }
	 * 
	 * protected void initViewManager() { this.viewManager = new
	 * ViewManager(this, splitPanel); }
	 * 
	 * // GETTERS
	 * /////////////////////////////////////////////////////////////////////////
	 * 
	 * public ViewManager getViewManager() { return viewManager; }
	 * 
	 * public ActionsPanel getActionsPanel() { return actionsPanel; }
	 * 
	 * public ContextHelper getContextHelper() { return context; }
	 */
}
