/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.bpmn20withactiviti.chapter7.workflow.ui.panel;

import java.util.List;

import org.activiti.engine.runtime.ProcessInstance;
import org.bpmn20withactiviti.chapter7.workflow.ui.ViewManager;
import org.bpmn20withactiviti.chapter7.workflow.ui.context.ContextHelper;
import org.bpmn20withactiviti.chapter7.workflow.ui.table.BookProjectTable;

import com.vaadin.data.Item;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

public class BookOverviewPanel extends Panel {

	protected static final long serialVersionUID = -2074647293591779784L;

	protected static final String NAME_FIELD = "Name";
	protected static final String DESCRIPTION_FIELD = "Description";

	// ui
	protected Label titleLabel;
	protected TextField nameField;
	protected TextField descriptionField;
	protected Table projectTable;

	// dependencies
	protected ViewManager viewManager;
	protected ContextHelper context;

	public BookOverviewPanel(ViewManager viewManager) {
		this.viewManager = viewManager;
		init();
	}

	protected void init() {
		this.context = new ContextHelper(viewManager.getApplication());
		setSizeFull();
		setStyleName(Reindeer.PANEL_LIGHT);
		initUi();
	}

	protected void initUi() {
		initTitle();

		GridLayout layout = new GridLayout(2, 7);
		layout.setSpacing(true);
		addComponent(layout);

		initNameField(layout);
		initDescriptionField(layout);
		initProjectTable(layout);
	}

	protected void initTitle() {
		VerticalLayout verticalLayout = new VerticalLayout();
		titleLabel = new Label("My books");
		titleLabel.setStyleName(Reindeer.LABEL_H1);
		verticalLayout.addComponent(titleLabel);

		// add some empty space
		Label emptyLabel = new Label("");
		emptyLabel.setHeight("1.5em");
		verticalLayout.addComponent(emptyLabel);

		addComponent(verticalLayout);
	}

	protected void initNameField(GridLayout layout) {
		nameField = new TextField();
		nameField.setWriteThrough(true);
		nameField.setImmediate(true);

		layout.addComponent(new Label(NAME_FIELD));
		layout.addComponent(nameField);
	}

	protected void initDescriptionField(GridLayout layout) {
		descriptionField = new TextField();
		descriptionField.setRows(4);
		descriptionField.setColumns(35);

		layout.addComponent(new Label(DESCRIPTION_FIELD));
		layout.addComponent(descriptionField);
	}

	protected void initProjectTable(GridLayout layout) {
	    projectTable = new BookProjectTable();
	    List<ProcessInstance> instanceList = context.getRuntimeService().createProcessInstanceQuery().list();
	    for (ProcessInstance processInstance : instanceList) {
	    	Object newItemId = projectTable.addItem();
	        Item newItem = projectTable.getItem(newItemId);

	        newItem.getItemProperty("id").setValue(processInstance.getProcessInstanceId());
	        newItem.getItemProperty("name").setValue(processInstance.getBusinessKey());
		}
	    layout.addComponent(new Label("Book projects"));
	    layout.addComponent(projectTable);
	  }

}
