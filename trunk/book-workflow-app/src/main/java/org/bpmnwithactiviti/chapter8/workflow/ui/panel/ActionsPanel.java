package org.bpmnwithactiviti.chapter8.workflow.ui.panel;

import org.bpmnwithactiviti.chapter8.workflow.ui.ViewManager;

import com.vaadin.ui.Button;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

public class ActionsPanel extends Panel {

  protected static final long serialVersionUID = 1L;

  protected static final String MY_CASES = "Case overview";
  protected static final String MY_BOOKS = "Book overview";
  protected static final String CREATE_BOOK_PROJECT = "Create book project";
  protected static final String LOGOUT = "Logout";
  
  private Button casesButton;
  private Button overviewProjectButton;
  private Button createProjectButton;
  private Button logOutButton;

  public ActionsPanel(final ViewManager viewManager) {
    setStyleName(Reindeer.PANEL_LIGHT);
    VerticalLayout layout = new VerticalLayout();
    layout.setSpacing(true);
    
    casesButton = createButton(MY_CASES);
    casesButton.addListener(new Button.Listener() {
    	
			private static final long serialVersionUID = 1L;
	
			public void componentEvent(Event event) {
				viewManager.showCasesPage();
			}
    	
    });

    overviewProjectButton = createButton(MY_BOOKS);
    overviewProjectButton.addListener(new Button.Listener() {
    	
			private static final long serialVersionUID = 1L;
	
			public void componentEvent(Event event) {
				viewManager.showBookProjectsPage();
			}
	    	
	  });

    createProjectButton = createButton(CREATE_BOOK_PROJECT);
    createProjectButton.addListener(new Button.Listener() {
    	
			private static final long serialVersionUID = 1L;
	
			public void componentEvent(Event event) {
				viewManager.showNewBookProjectPage();
			}
    	
    });
    
    logOutButton = createButton(LOGOUT);
    logOutButton.addListener(new Button.Listener() {
    	
			private static final long serialVersionUID = 1L;
	
			public void componentEvent(Event event) {
				viewManager.getApplication().setUser(null);
				enableButtons(false);
				viewManager.showLoginPage();
			}
    	
    });

    layout.addComponent(casesButton);
    layout.addComponent(overviewProjectButton);
    layout.addComponent(createProjectButton);
    layout.addComponent(logOutButton);
    addComponent(layout);
  }
  
  public void enableButtons(boolean enabled) {
  	casesButton.setEnabled(enabled);
    overviewProjectButton.setEnabled(enabled);
    createProjectButton.setEnabled(enabled);
    logOutButton.setEnabled(enabled);
  }
  
  private Button createButton(String text) {
  	Button button = new Button(text);
  	button.setEnabled(false);
  	button.setWidth("132px");
  	return button;
  }

}
