package org.bpmn20withactiviti.chapter7.workflow.ui.panel;

import org.bpmn20withactiviti.chapter7.workflow.ui.ViewManager;

import com.vaadin.ui.Button;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

public class ActionsPanel extends Panel {

  protected static final long serialVersionUID = -8360966745210668309L;

  protected static final String MY_BOOKS = "Book overview";
  protected static final String CREATE_BOOK_PROJECT = "Create book project";

  public ActionsPanel(final ViewManager viewManager) {
    setStyleName(Reindeer.PANEL_LIGHT);
    VerticalLayout layout = new VerticalLayout();
    layout.setSpacing(true);

    Button overviewProjectButton = new Button(MY_BOOKS);
    overviewProjectButton.setWidth("132px");
    overviewProjectButton.addListener(new Button.Listener() {
    	
		private static final long serialVersionUID = 6421022078037428099L;

		public void componentEvent(Event event) {
			viewManager.switchWorkArea(ViewManager.PROJECT_OVERVIEW, new BookOverviewPanel(viewManager));
			
		}
    	
    });

    Button createProjectButton = new Button(CREATE_BOOK_PROJECT);
    createProjectButton.setWidth("132px");
    createProjectButton.addListener(new Button.Listener() {
    	
		private static final long serialVersionUID = 6421022078037428099L;

		public void componentEvent(Event event) {
			viewManager.switchWorkArea(ViewManager.CREATE_NEW_PROJECT, new CreateBookProjectPanel(viewManager));
			
		}
    	
    });

    layout.addComponent(overviewProjectButton);
    layout.addComponent(createProjectButton);
    addComponent(layout);
  }

}
