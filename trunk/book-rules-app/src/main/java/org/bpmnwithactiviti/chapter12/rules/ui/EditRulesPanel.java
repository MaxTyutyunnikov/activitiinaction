package org.bpmnwithactiviti.chapter12.rules.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.activiti.engine.repository.Deployment;
import org.apache.log4j.Logger;
import org.bpmnwithactiviti.chapter12.rules.ActivitiDelegate;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window.Notification;
import com.vaadin.ui.themes.Reindeer;

public class EditRulesPanel extends Panel {

	protected static final long serialVersionUID = -2074647293591779784L;
	private static Logger logger = Logger.getLogger(EditRulesPanel.class);

	// ui
	protected Label titleLabel;
	private DeploymentTable deploymentTable;
	private RuleFileTable ruleTable;
	private TextArea ruleField;
	private Deployment selectedDeployment;
	private String selectedRuleFile;

	// dependencies
	protected ViewManager viewManager;

	public EditRulesPanel(ViewManager viewManager) {
		this.viewManager = viewManager;
		init();
	}

	protected void init() {
		setSizeFull();
		setStyleName(Reindeer.PANEL_LIGHT);
		initUi();
	}

	protected void initUi() {
		initTitle();
		GridLayout layout = new GridLayout(2, 2);
		layout.setSpacing(true);
		addComponent(layout);
		initProcessTable(layout);
		initRuleFileTable(layout);
		initRuleField();
	}

	protected void initProcessTable(final GridLayout layout) {
		deploymentTable = new DeploymentTable();
		try {
			Collection<Deployment> deployments = ActivitiDelegate.getDeployments();
			for (final Deployment deployment : deployments) {
				Button rulesButton = new Button("show rules");
				rulesButton.setData(deployment);
				rulesButton.addListener(new Button.ClickListener() {

					private static final long serialVersionUID = 1L;

					public void buttonClick(ClickEvent event) {
						if(ruleField != null) {
							ruleField.setValue("");
						}
						List<String> deployedFileNames = ActivitiDelegate
								.getDeployedResourceNames(deployment.getId());
						Collection<String> deployedRuleFiles = new ArrayList<String>();
						for (String s : deployedFileNames) {
							if (s.endsWith(".drl")) {
								logger.info("Found rule file : " + s);
								deployedRuleFiles.add(s);
							}
						}
						if (deployedRuleFiles.size() > 0) {
							fillRuleFileTable(deployment, deployedRuleFiles);
						} else {
							viewManager.getApplication()
									.getMainWindow()
									.showNotification("There are no rule files in this deployment..",
											Notification.TYPE_ERROR_MESSAGE);
						}
					}
				});
				rulesButton.addStyleName("link");
				deploymentTable.addItem(new Object[] { deployment.getId(), 
						deployment.getName(), rulesButton }, deployment.getId());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		layout.addComponent(deploymentTable);
	}
	
	protected void initRuleFileTable(GridLayout layout) {
		ruleTable = new RuleFileTable();
		layout.addComponent(ruleTable, 1, 0);
	}
	
	protected void initRuleField() {
		initSubTitle();
		VerticalLayout vl = new VerticalLayout();
		addComponent(vl);
		ruleField = new TextArea();
		ruleField.setRows(20);
		ruleField.setColumns(70);
		vl.addComponent(ruleField);
		Button deployEditedRuleButton = new Button("Deploy Edited Rule");
		deployEditedRuleButton.setWidth("300px");
		deployEditedRuleButton.addListener(new Button.ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				ActivitiDelegate.deployEditedRule(selectedDeployment, selectedRuleFile, ruleField.getValue().toString());
				viewManager.switchWorkArea(ViewManager.EDIT_RULES, new EditRulesPanel(viewManager));
			}
		});
		Label emptyLabel = new Label("");
		emptyLabel.setHeight("1.5em");
		vl.addComponent(emptyLabel);
		vl.addComponent(deployEditedRuleButton);
	}
	
	protected void fillRuleFileTable(final Deployment deployment, 
			Collection<String> ruleFileNames) {
		
		selectedDeployment = deployment;
		ruleTable.removeAllItems();
		for (final String ruleFile : ruleFileNames) {
			Button ruleButton = new Button("show rule");
			ruleButton.setData(ruleFile);
			ruleButton.addListener(new Button.ClickListener() {
				
				private static final long serialVersionUID = 1L;

				public void buttonClick(ClickEvent event) {
					try {
						String ruleFileContent = ActivitiDelegate.getRuleResource(deployment.getId(), ruleFile);
						ruleField.setValue(ruleFileContent);
						selectedRuleFile = ruleFile;
						
					} catch (Exception e) {
						e.printStackTrace();
						viewManager.getApplication()
								.getMainWindow()
								.showNotification("Something went wrong reading the file..",
										Notification.TYPE_ERROR_MESSAGE);
					}
				}
			});
			ruleButton.addStyleName("link");
			ruleTable.addItem(new Object[] { ruleFile, ruleButton }, ruleFile);
		}

	}

	protected void initTitle() {
		VerticalLayout verticalLayout = new VerticalLayout();
		titleLabel = new Label("Activiti Rule Editing");
		titleLabel.setStyleName(Reindeer.LABEL_H1);
		verticalLayout.addComponent(titleLabel);
		Label emptyLabel = new Label("");
		emptyLabel.setHeight("1.5em");
		verticalLayout.addComponent(emptyLabel);
		addComponent(verticalLayout);
	}
	
	protected void initSubTitle() {
		VerticalLayout verticalLayout = new VerticalLayout();
		titleLabel = new Label("Edit DRL");
		titleLabel.setStyleName(Reindeer.LABEL_H2);
		verticalLayout.addComponent(titleLabel);
		Label emptyLabel = new Label("");
		emptyLabel.setHeight("1.5em");
		verticalLayout.addComponent(emptyLabel);
		addComponent(verticalLayout);
	}

}
