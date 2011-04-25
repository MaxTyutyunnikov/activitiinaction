package org.bpmnwithactiviti.chapter9.rules.ui;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.apache.log4j.Logger;
import org.bpmnwithactiviti.chapter9.rules.RuleApplication;

import com.vaadin.ui.Button;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class ActivitiLocationPopup extends Window {

	private static final long serialVersionUID = 7490285675725086859L;

	// dependencies
	protected ViewManager viewManager;
	private static Logger logger = Logger.getLogger(ActivitiLocationPopup.class);

	protected TextField activitiURLField;
	protected Button setActivitiURLButton;

	public ActivitiLocationPopup(ViewManager vm) {
		this.viewManager = vm;
		init();
	}

	private void init() {
		setModal(true);
		setWidth("20%");
		center();
		setCaption("Activiti URL");

		VerticalLayout layout = new VerticalLayout();
		layout.setSpacing(true);

		activitiURLField = new TextField("Enter Activiti URL");
		activitiURLField.setWidth("300px");
		activitiURLField.focus();
		activitiURLField.setValue("http://localhost:8080/activiti-rest/service");

		setActivitiURLButton = new Button("Set");
		setActivitiURLButton.addListener(new Button.Listener() {

			private static final long serialVersionUID = 1L;

			public void componentEvent(Event event) {
				if(activitiURLField != null && activitiURLField.getValue().toString().length() != 0){
					try {
					    URL url = new URL(activitiURLField.getValue().toString());
					    URLConnection conn = url.openConnection();
					    conn.connect();
					    RuleApplication.activitiURL = activitiURLField.getValue().toString();
					    logger.info("Setting Activiti location to : " + RuleApplication.activitiURL);
						viewManager.removePopupWindow(ActivitiLocationPopup.this);
						viewManager.switchWorkArea(ViewManager.EDIT_RULES, new EditRulesPanel(viewManager));
					} catch (MalformedURLException e) {
						viewManager
						.getApplication()
						.getMainWindow()
						.showNotification("You need to enter a valid URL",
								Notification.TYPE_ERROR_MESSAGE);
					} catch (IOException e) {
						viewManager
						.getApplication()
						.getMainWindow()
						.showNotification("A connection at the entered URL cannot be established, is your Activiti server up and running?",
								Notification.TYPE_ERROR_MESSAGE);
					}
				}else{
					viewManager
					.getApplication()
					.getMainWindow()
					.showNotification("You need to enter a URL",
							Notification.TYPE_ERROR_MESSAGE);
				}
			}

		});
		layout.addComponent(activitiURLField);
		layout.addComponent(setActivitiURLButton);
		addComponent(layout);
	}

}