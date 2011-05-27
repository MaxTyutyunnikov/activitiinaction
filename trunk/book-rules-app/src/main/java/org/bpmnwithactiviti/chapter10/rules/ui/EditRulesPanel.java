package org.bpmnwithactiviti.chapter10.rules.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.activiti.engine.repository.Deployment;
import org.apache.log4j.Logger;
import org.bpmnwithactiviti.chapter10.rules.ActivitiDelegate;

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
	}

	protected void initProcessTable(final GridLayout layout) {
		deploymentTable = new DeploymentTable();
		try {
			Collection<Deployment> deployments = ActivitiDelegate.getDeployments();
			for (final Deployment d : deployments) {
				Button rulesButton = new Button("show rules");
				rulesButton.setData(d);
				rulesButton.addListener(new Button.ClickListener() {

					private static final long serialVersionUID = 1L;

					public void buttonClick(ClickEvent event) {
						List<String> deployedFileNames = ActivitiDelegate
								.getDeployedResourceNames(d.getId());
						Collection<String> deployedRuleFiles = new ArrayList<String>();
						for (String s : deployedFileNames) {
							if (s.endsWith(".drl")) {
								logger.info("Found rule file : " + s);
								deployedRuleFiles.add(s);
							}
						}
						if (deployedRuleFiles.size() > 0) {
							initRuleFileTable(layout, d,
									deployedRuleFiles);
						} else {
							viewManager
									.getApplication()
									.getMainWindow()
									.showNotification(
											"There are no rule files in this deployment..",
											Notification.TYPE_ERROR_MESSAGE);
						}
					}
				});
				rulesButton.addStyleName("link");
				deploymentTable
						.addItem(new Object[] { d.getId(), d.getName(),
								rulesButton }, d.getId());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		layout.addComponent(deploymentTable);
	}

	protected void initRuleFileTable(GridLayout layout,
			final Deployment d, Collection<String> ruleFileNames) {
		System.out.println("Initing rule table");
		RuleFileTable ruleTable = new RuleFileTable();
		layout.addComponent(ruleTable);
		for (final String s : ruleFileNames) {
			Button ruleButton = new Button("show rule");
			ruleButton.setData(s);
			ruleButton.addListener(new Button.ClickListener() {

				private static final long serialVersionUID = 1L;

				@SuppressWarnings("deprecation")
				public void buttonClick(ClickEvent event) {
					String ruleFileContent;
					try {
						ruleFileContent = ActivitiDelegate.getRuleResource(d.getId(), s);
						initSubTitle();
						VerticalLayout vl = new VerticalLayout();
						addComponent(vl);
						final TextArea ruleField = new TextArea();
						ruleField.setRows(20);
						ruleField.setColumns(48);
						ruleField.setValue(ruleFileContent);
						vl.addComponent(ruleField);
						Button deployEditedRuleButton = new Button("Deploy Edited Rule");
						deployEditedRuleButton.setWidth("300px");
						deployEditedRuleButton.addListener(new Button.ClickListener() {

							private static final long serialVersionUID = 1L;

							@Override
							public void buttonClick(ClickEvent event) {
								ActivitiDelegate.deployEditedRule(d, s, ruleField.getValue().toString());
								viewManager.switchWorkArea(ViewManager.EDIT_RULES, new EditRulesPanel(viewManager));
							}
						});
						Label emptyLabel = new Label("");
						emptyLabel.setHeight("1.5em");
						vl.addComponent(emptyLabel);
						vl.addComponent(deployEditedRuleButton);
					} catch (Exception e) {
						e.printStackTrace();
						viewManager
								.getApplication()
								.getMainWindow()
								.showNotification(
										"Something went wrong reading the file..",
										Notification.TYPE_ERROR_MESSAGE);
					}
				}
			});
			ruleButton.addStyleName("link");
			ruleTable.addItem(new Object[] { s, ruleButton }, s);
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
