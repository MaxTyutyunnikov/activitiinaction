package org.bpmn20withactiviti.chapter8.workflow;

import org.bpmn20withactiviti.chapter8.workflow.ui.ViewManager;
import org.bpmn20withactiviti.chapter8.workflow.ui.panel.ActionsPanel;
import org.bpmn20withactiviti.chapter8.workflow.ui.panel.BookOverviewPanel;

import com.vaadin.Application;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.SplitPanel;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Reindeer;

public class WorkflowApplication extends Application {

  protected static final long serialVersionUID = 6197397757268207621L;

  protected static final String TITLE = "Activiti KickStart";
  protected static final String THEME_NAME = "yakalo";
  protected static final String CONTENT_LOCATION = "content"; // id of div in layout where app needs to come

  // ui
  protected ViewManager viewManager;
  protected CustomLayout mainLayout; // general layout of the app
  protected SplitPanel splitPanel; // app uses a split panel: left actions, right work area
  protected ActionsPanel actionsPanel; // left panel with user actions
  protected Panel currentWorkArea; // right panel of ui where actual work happens

  public void init() {
    setTheme(THEME_NAME);
    initMainWindow();
    initDefaultWorkArea();
  }

  protected void initMainWindow() {
    mainLayout = new CustomLayout(THEME_NAME); // uses layout defined in
                                               // webapp/Vaadin/themes/yakalo
    mainLayout.setSizeFull();

    Window mainWindow = new Window(TITLE);
    setMainWindow(mainWindow);
    mainWindow.setContent(mainLayout);

    initSplitPanel();
    initViewManager();
    initActionsPanel();
  }

  protected void initSplitPanel() {
    splitPanel = new SplitPanel(SplitPanel.ORIENTATION_HORIZONTAL);
    splitPanel.setSplitPosition(170, SplitPanel.UNITS_PIXELS);
    splitPanel.setStyleName(Reindeer.LAYOUT_WHITE);
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

  protected void initDefaultWorkArea() {
    viewManager.switchWorkArea(ViewManager.PROJECT_OVERVIEW, new BookOverviewPanel(viewManager));
  }

  // GETTERS /////////////////////////////////////////////////////////////////////////

  public ViewManager getViewManager() {
    return viewManager;
  }

}
