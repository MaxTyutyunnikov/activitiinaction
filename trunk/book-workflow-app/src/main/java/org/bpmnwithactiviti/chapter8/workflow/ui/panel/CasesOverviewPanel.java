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
package org.bpmnwithactiviti.chapter8.workflow.ui.panel;

import java.util.List;

import org.activiti.engine.task.Task;
import org.bpmnwithactiviti.chapter8.workflow.model.Chapter;
import org.bpmnwithactiviti.chapter8.workflow.ui.ViewManager;
import org.bpmnwithactiviti.chapter8.workflow.ui.table.BookProjectTable;

import com.vaadin.data.Item;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

public class CasesOverviewPanel extends Panel {

	protected static final long serialVersionUID = 1L;

	//ui
	protected Table tasksTable;

	// dependencies
	protected ViewManager viewManager;

	public CasesOverviewPanel(ViewManager viewManager) {
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

		GridLayout layout = new GridLayout(2, 7);
		layout.setSpacing(true);
		addComponent(layout);

		initTasksTable(layout);
	}

	protected void initTitle() {
		VerticalLayout verticalLayout = new VerticalLayout();
		Label titleLabel = new Label("Tasks");
		titleLabel.setStyleName(Reindeer.LABEL_H1);
		verticalLayout.addComponent(titleLabel);

		// add some empty space
		Label emptyLabel = new Label("");
		emptyLabel.setHeight("1.5em");
		verticalLayout.addComponent(emptyLabel);

		addComponent(verticalLayout);
	}

	protected void initTasksTable(GridLayout layout) {
		tasksTable = new BookProjectTable();
    List<Task> taskList = viewManager.getApplication().getContextHelper()
    		.getTaskService().createTaskQuery().list();
    for (Task task : taskList) {
    	Chapter chapter = (Chapter) viewManager.getApplication().getContextHelper()
    			.getRuntimeService().getVariable("chapter", task.getProcessInstanceId());
    	Object newItemId = tasksTable.addItem();
      Item newItem = tasksTable.getItem(newItemId);

      newItem.getItemProperty("id").setValue(task.getProcessInstanceId());
      newItem.getItemProperty("name").setValue(task.getName());
      newItem.getItemProperty("title").setValue(chapter.getChapterNumber());
    }
    layout.addComponent(new Label("Tasks"));
    layout.addComponent(tasksTable);
  }
}
