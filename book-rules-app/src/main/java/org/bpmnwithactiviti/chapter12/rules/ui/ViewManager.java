package org.bpmnwithactiviti.chapter12.rules.ui;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import org.bpmnwithactiviti.chapter12.rules.RuleApplication;

import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Window;

public class ViewManager {
	
	public static final String EDIT_RULES = "editRules";

	protected RuleApplication application;
	protected HorizontalSplitPanel splitPanel;
	protected Map<String, Panel> views = new HashMap<String, Panel>();
	protected Stack<Panel> screenStack = new Stack<Panel>();

	public ViewManager(RuleApplication application,
			HorizontalSplitPanel splitPanel) {
		this.application = application;
		this.splitPanel = splitPanel;
	}

	public Panel getWorkAreaPanel(String viewName) {
		return views.get(viewName);
	}

	public void switchWorkArea(String viewName, Panel workAreaPanel) {
		Panel panel = workAreaPanel;
		panel.setScrollable(true);
		if (workAreaPanel != null) {
			views.put(viewName, workAreaPanel);
		} else {
			panel = views.get(viewName);
		}
		splitPanel.setSecondComponent(panel);
	}

	public void showPopupWindow(Window popupWindow) {
		application.getMainWindow().addWindow(popupWindow);
	}
	
	public void removePopupWindow(Window popupWindow) {
		application.getMainWindow().removeWindow(popupWindow);
	}

	public void pushWorkArea(String viewName, Panel workAreaPanel) {
		screenStack.push((Panel) splitPanel.getSecondComponent());
		switchWorkArea(viewName, workAreaPanel);
	}

	public void popWorkArea() {
		splitPanel.setSecondComponent(screenStack.pop());
	}

	public RuleApplication getApplication() {
		return application;
	}

}
