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
package org.bpmn20withactiviti.chapter8.workflow.ui.panel;

import org.bpmn20withactiviti.chapter8.workflow.ui.ViewManager;
import org.bpmn20withactiviti.chapter8.workflow.ui.context.ContextHelper;
import org.bpmn20withactiviti.chapter8.workflow.ui.popup.ErrorPopupWindow;

import com.vaadin.ui.Button;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.themes.Reindeer;

public class CreateBookProjectPanel extends Panel {

	protected static final long serialVersionUID = -2074647293591779784L;

	protected static final String NAME_FIELD = "Name";
	protected static final String DESCRIPTION_FIELD = "Description";

	// ui
	protected Label titleLabel;
	protected TextField nameField;
	protected TextField descriptionField;

	// dependencies
	protected ViewManager viewManager;
	protected ContextHelper context;

	public CreateBookProjectPanel(ViewManager viewManager) {
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
		initButtons(layout);
	}

	protected void initTitle() {
		VerticalLayout verticalLayout = new VerticalLayout();
		titleLabel = new Label("Create book project");
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

	protected void initButtons(GridLayout layout) {
		final Button startButton = new Button("Start process");
		startButton.addListener(new Button.ClickListener() {

			private static final long serialVersionUID = 1L;

			public void buttonClick(ClickEvent event) {
				try {
					context.getRuntimeService().startProcessInstanceByKey("bookdevelopment");
					Panel successPanel = new Panel();
					successPanel.setStyleName(Reindeer.PANEL_LIGHT);
					Label successLabel = new Label(
							"Process successfully deployed");
					successPanel.addComponent(successLabel);
					viewManager.switchWorkArea(
							ViewManager.PROJECT_CREATED,
							successPanel);
				} catch (Exception e) {
					viewManager.showPopupWindow(new ErrorPopupWindow(e));
				}
			}
		});

		HorizontalLayout footer = new HorizontalLayout();
		footer.setSpacing(true);
		footer.addComponent(startButton);
		layout.addComponent(new Label());
		layout.addComponent(footer);
	}

}
