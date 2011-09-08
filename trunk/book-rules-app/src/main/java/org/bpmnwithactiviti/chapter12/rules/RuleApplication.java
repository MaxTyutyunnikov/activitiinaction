package org.bpmnwithactiviti.chapter12.rules;

import org.bpmnwithactiviti.chapter12.rules.ui.ActionsPanel;
import org.bpmnwithactiviti.chapter12.rules.ui.ViewManager;

import com.vaadin.Application;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Window;

public class RuleApplication extends Application {

	protected static final long serialVersionUID = 6197397757268207621L;

	protected static final String TITLE = "Activiti Rules Application";
	protected static final String THEME_NAME = "yakalo";
	protected static final String CONTENT_LOCATION = "content";
	
	protected ViewManager viewManager;
	protected CustomLayout mainLayout;
	protected HorizontalSplitPanel splitPanel;
	protected ActionsPanel actionsPanel;
	protected Panel currentWorkArea;

	public void init() {
		setTheme(THEME_NAME);
		initMainWindow();
	}

	protected void initMainWindow() {
		mainLayout = new CustomLayout(THEME_NAME);
		mainLayout.setSizeFull();

		Window mainWindow = new Window(TITLE);
		setMainWindow(mainWindow);
		mainWindow.setContent(mainLayout);
		mainWindow.setScrollable(true);

		initSplitPanel();
		initViewManager();
		initActionsPanel();
	}

	protected void initSplitPanel() {
		splitPanel = new HorizontalSplitPanel();
		splitPanel.setSplitPosition(210, HorizontalSplitPanel.UNITS_PIXELS);
		splitPanel.setSizeFull();

		mainLayout.addComponent(splitPanel, CONTENT_LOCATION);
	}

	protected void initActionsPanel() {
		this.actionsPanel = new ActionsPanel(viewManager);
		splitPanel.setFirstComponent(actionsPanel);
	}

	protected void initViewManager() {
		this.viewManager = new ViewManager(this, splitPanel);
	}

	public ViewManager getViewManager() {
		return viewManager;
	}

	public ActionsPanel getActionsPanel() {
		return actionsPanel;
	}

}