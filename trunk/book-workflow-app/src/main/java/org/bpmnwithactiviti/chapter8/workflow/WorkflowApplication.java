package org.bpmnwithactiviti.chapter8.workflow;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bpmnwithactiviti.chapter8.workflow.model.BookProject;
import org.bpmnwithactiviti.chapter8.workflow.model.User;
import org.bpmnwithactiviti.chapter8.workflow.ui.ViewManager;
import org.bpmnwithactiviti.chapter8.workflow.ui.context.ContextHelper;
import org.bpmnwithactiviti.chapter8.workflow.ui.panel.ActionsPanel;

import com.vaadin.Application;
import com.vaadin.terminal.Sizeable;
import com.vaadin.terminal.gwt.server.HttpServletRequestListener;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Reindeer;

public class WorkflowApplication extends Application implements HttpServletRequestListener {

  protected static final long serialVersionUID = 6197397757268207621L;

  protected static final String TITLE = "Activiti KickStart";
  protected static final String THEME_NAME = "yakalo";
  protected static final String CONTENT_LOCATION = "content"; // id of div in layout where app needs to come

  // ui
  protected ViewManager viewManager;
  protected CustomLayout mainLayout; // general layout of the app
  protected HorizontalSplitPanel splitPanel; // app uses a split panel: left actions, right work area
  protected ActionsPanel actionsPanel; // left panel with user actions
  protected Panel currentWorkArea; // right panel of ui where actual work happens
  protected ContextHelper context;
  
  protected Map<String, User> userMap = new HashMap<String, User>();
  
  // HttpServletRequestListener -------------------------------------------------------------------
  
  public void onRequestStart(HttpServletRequest request, HttpServletResponse response) {
  	if(getMainWindow() == null) {
  		setTheme(THEME_NAME);
  		initMainWindow();
  	}
  	
    // Authentication: check if user is found, otherwise send to login page
  	User user = (User) getUser();
    if (user == null && viewManager.isInLoginPage() == false) {
    	viewManager.showLoginPage();
    }
  }
  
  @Override
  public void onRequestEnd(HttpServletRequest request,
      HttpServletResponse response) {
	  
  }
  
  @Override
  public void init() {
  	context = new ContextHelper(this);
  	context.getRepositoryService().createDeployment()
			.addClasspathResource("org/bpmn20withactiviti/chapter8/workflow/process/bookwriting.bpmn20.xml")
			.deploy();
  	
  	Map<String, Object> variableMap = new HashMap<String, Object>();
		variableMap.put("bookProject", new BookProject()
				.setTitle("Activiti in Action")
				.setNrOfChapters(14)
				.addAuthor("trademak")
				.addAuthor("rvliempd")
				.setReviewer("johndoe")
				.setDescription("Executable BPMN 2.0 processes"));
  	context.getRuntimeService().startProcessInstanceByKey("bookWritingProcess", variableMap);
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
  
  public Map<String, User> getUserMap() {
  	return userMap;
  }
}
