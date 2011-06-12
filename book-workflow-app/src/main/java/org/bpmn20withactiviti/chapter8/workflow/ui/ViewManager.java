package org.bpmn20withactiviti.chapter8.workflow.ui;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import org.bpmn20withactiviti.chapter8.workflow.WorkflowApplication;

import com.vaadin.ui.Panel;
import com.vaadin.ui.SplitPanel;
import com.vaadin.ui.Window;

public class ViewManager {

  public static final String PROJECT_OVERVIEW = "projectOverview";
  public static final String CREATE_NEW_PROJECT = "projectCreate";
  public static final String PROJECT_CREATED = "createdNewProject";

  protected WorkflowApplication application;
  protected SplitPanel splitPanel;
  protected Map<String, Panel> views = new HashMap<String, Panel>();
  protected Stack<Panel> screenStack = new Stack<Panel>();

  public ViewManager(WorkflowApplication application, SplitPanel splitPanel) {
    this.application = application;
    this.splitPanel = splitPanel;
  }

  public void switchWorkArea(String viewName, Panel workAreaPanel) {
    Panel panel = workAreaPanel;
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

  public void pushWorkArea(String viewName, Panel workAreaPanel) {
    screenStack.push((Panel) splitPanel.getSecondComponent());
    switchWorkArea(viewName, workAreaPanel);
  }

  public void popWorkArea() {
    splitPanel.setSecondComponent(screenStack.pop());
  }

  public WorkflowApplication getApplication() {
    return application;
  }

}
