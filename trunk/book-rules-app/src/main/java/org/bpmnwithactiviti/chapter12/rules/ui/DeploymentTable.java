package org.bpmnwithactiviti.chapter12.rules.ui;

import com.vaadin.ui.Button;
import com.vaadin.ui.Table;

public class DeploymentTable extends Table {

	private static final long serialVersionUID = 6521446909987945815L;

	public DeploymentTable() {
		setSelectable(true);
		setImmediate(true);
		setEditable(false);
		setColumnReorderingAllowed(true);
		setPageLength(10);

		addContainerProperty("deploymentId", String.class, null);
		addContainerProperty("deploymentName", String.class, null);
		addContainerProperty("Rules", Button.class, null);

		setColumnHeader("deploymentId", "Deployment ID");
		setColumnHeader("deploymentName", "Deployment Name");
		setColumnFooter("deploymentId", "");
		setColumnFooter("deploymentName", "");
		setFooterVisible(true);
	}

}