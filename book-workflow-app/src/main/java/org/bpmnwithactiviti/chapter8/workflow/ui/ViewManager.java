package org.bpmnwithactiviti.chapter8.workflow.ui;

import org.bpmnwithactiviti.chapter8.workflow.WorkflowApplication;
import org.bpmnwithactiviti.chapter8.workflow.ui.panel.BookOverviewPanel;
import org.bpmnwithactiviti.chapter8.workflow.ui.panel.CasesOverviewPanel;
import org.bpmnwithactiviti.chapter8.workflow.ui.panel.CreateBookProjectPanel;
import org.bpmnwithactiviti.chapter8.workflow.ui.panel.LoginPanel;

import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.Notification;

public class ViewManager {

	public static final String CASES_OVERVIEW = "casesOverview";
  public static final String PROJECT_OVERVIEW = "projectOverview";
  public static final String CREATE_NEW_PROJECT = "projectCreate";
  public static final String PROJECT_CREATED = "createdNewProject";

  protected WorkflowApplication application;
  protected HorizontalSplitPanel splitPanel;
  protected Panel lastPanel;
  protected boolean inLoginPage;

  public ViewManager(WorkflowApplication application, HorizontalSplitPanel splitPanel) {
    this.application = application;
    this.splitPanel = splitPanel;
  }
  
  public void showLoginPage() {
  	setInLoginPage(true);
  	switchWorkArea(new LoginPanel(this));
  }
  
  public void showCasesPage() {
  	switchWorkArea(new CasesOverviewPanel(this));
  }
  
  public void showBookProjectsPage() {
  	switchWorkArea(new BookOverviewPanel(this));
  }
  
  public void showNewBookProjectPage() {
  	switchWorkArea(new CreateBookProjectPanel(this));
  }
  
  public void showErrorMessage(String message) {
  	application.getMainWindow().showNotification(message, Notification.TYPE_ERROR_MESSAGE);
	}
  
  public void showInfoMessage(String message) {
  	application.getMainWindow().showNotification(message, Window.Notification.TYPE_HUMANIZED_MESSAGE);
  }

  private void switchWorkArea(Panel workAreaPanel) {
  	if(lastPanel != null) {
  		splitPanel.removeComponent(lastPanel);
  	}
  	lastPanel = workAreaPanel;
    splitPanel.setSecondComponent(workAreaPanel);
  }

  public WorkflowApplication getApplication() {
    return application;
  }
  
  public boolean isInLoginPage() {
  	return inLoginPage;
  }
  
  public void setInLoginPage(boolean inLoginPage) {
  	this.inLoginPage = inLoginPage;
  }

}
