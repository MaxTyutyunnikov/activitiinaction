package org.bpmnwithactiviti.chapter12.rules.ui;

import com.vaadin.ui.Button;
import com.vaadin.ui.Table;

public class RuleFileTable extends Table {

	private static final long serialVersionUID = 6521446909987945815L;

	public RuleFileTable() {
		setSelectable(true);
		setImmediate(true);
		setEditable(false);
		setColumnReorderingAllowed(true);
		setPageLength(10);

		addContainerProperty("ruleFileName", String.class, null);
		addContainerProperty("Content", Button.class, null);

		setColumnHeader("ruleFileName", "Rulefile Name");
		setColumnFooter("ruleFileName", "");
		setFooterVisible(true);
	}

}