package org.bpmnwithactiviti.chapter12.rules.ui;

import com.vaadin.ui.Button;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

public class ActionsPanel extends Panel {

  protected static final long serialVersionUID = 1L;
  
  protected ViewManager viewManager;
  protected Button editRulesButton;

  public ActionsPanel(final ViewManager viewManager) {
    this.viewManager = viewManager;
    VerticalLayout layout = new VerticalLayout();
    layout.setSpacing(true);

    editRulesButton = new Button("Drools Rules");
    editRulesButton.setWidth("170px");
    editRulesButton.addListener(new Button.Listener() {
    	
  		private static final long serialVersionUID = 1L;
  
  		public void componentEvent(Event event) {
  		  viewManager.switchWorkArea(ViewManager.EDIT_RULES, new EditRulesPanel(viewManager));
  		}
    	
    });
    
    layout.addComponent(editRulesButton);
    addComponent(layout);
  }

}
