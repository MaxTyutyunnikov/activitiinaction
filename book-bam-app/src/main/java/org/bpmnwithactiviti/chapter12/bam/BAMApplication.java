package org.bpmnwithactiviti.chapter12.bam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.vaadin.Application;
import com.vaadin.terminal.gwt.server.HttpServletRequestListener;

public class BAMApplication extends Application implements HttpServletRequestListener {

  protected static final long serialVersionUID = 6197397757268207621L;

  protected static final String TITLE = "Activiti KickStart";
  protected static final String THEME_NAME = "yakalo";
  protected static final String CONTENT_LOCATION = "content"; // id of div in layout where app needs to come
  
  @Override
  public void onRequestStart(HttpServletRequest request, HttpServletResponse response) {
	// TODO Auto-generated method stub
  }
  
  @Override
  public void onRequestEnd(HttpServletRequest request, HttpServletResponse response) {
	// TODO Auto-generated method stub
  }

  @Override
  public void init() {
	  // TODO Auto-generated method stub
  }

  /*
  // ui
  protected ViewManager viewManager;
  protected CustomLayout mainLayout; // general layout of the app
  protected HorizontalSplitPanel splitPanel; // app uses a split panel: left actions, right work area
  protected ActionsPanel actionsPanel; // left panel with user actions
  protected Panel currentWorkArea; // right panel of ui where actual work happens
  protected ContextHelper context;
  
  // HttpServletRequestListener -------------------------------------------------------------------
  
  public void onRequestStart(HttpServletRequest request, HttpServletResponse response) {
  	if(getMainWindow() == null) {
  		setTheme(THEME_NAME);
  		initMainWindow();
  	}
  }
  
  @Override
  public void onRequestEnd(HttpServletRequest request, HttpServletResponse response) {
  }
  
  @Override
  public void init() {
  	context = new ContextHelper(this);
  	context.getRepositoryService().createDeployment()
			.addClasspathResource("org/bpmn20withactiviti/chapter8/workflow/process/bookwriting.bpmn20.xml")
			.deploy();
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
    splitPanel = new HorizontalSplitPanel();
    splitPanel.setSplitPosition(170, Sizeable.UNITS_PIXELS);
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

  // GETTERS /////////////////////////////////////////////////////////////////////////

  public ViewManager getViewManager() {
    return viewManager;
  }
  
  public ActionsPanel getActionsPanel() {
  	return actionsPanel;
  }

  public ContextHelper getContextHelper() {
  	return context;
  }
  */
}
