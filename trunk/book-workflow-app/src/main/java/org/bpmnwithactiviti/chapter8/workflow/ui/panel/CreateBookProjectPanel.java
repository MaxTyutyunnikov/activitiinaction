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

import java.util.HashMap;
import java.util.Map;

import org.bpmnwithactiviti.chapter8.workflow.model.BookProject;
import org.bpmnwithactiviti.chapter8.workflow.model.User;
import org.bpmnwithactiviti.chapter8.workflow.ui.ViewManager;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

public class CreateBookProjectPanel extends Panel {

	protected static final long serialVersionUID = -2074647293591779784L;

	protected static final String NAME_FIELD = "Book title";
	protected static final String AUTHOR_FIELD = "Authors";
	protected static final String NR_CHAPTERS_FIELD = "Nr of chapters";
	protected static final String DESCRIPTION_FIELD = "Description";

	// ui
	protected TextField bookTitleField;
	protected TextField nrChaptersField;
	protected ListSelect authorsSelect;
	protected TextArea descriptionField;

	// dependencies
	protected ViewManager viewManager;
	
	public CreateBookProjectPanel(ViewManager viewManager) {
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

		initBookTitleField(layout);
		initNrOfChaptersField(layout);
		initAuthorsField(layout);
		initDescriptionField(layout);
		initButtons(layout);
	}

	protected void initTitle() {
		VerticalLayout verticalLayout = new VerticalLayout();
		Label titleLabel = new Label("Create new book project");
		titleLabel.setStyleName(Reindeer.LABEL_H1);
		verticalLayout.addComponent(titleLabel);

		// add some empty space
		Label emptyLabel = new Label("");
		emptyLabel.setHeight("1.5em");
		verticalLayout.addComponent(emptyLabel);

		addComponent(verticalLayout);
	}

	protected void initBookTitleField(GridLayout layout) {
		bookTitleField = initTextField(NAME_FIELD, layout);
	}
	
	protected void initNrOfChaptersField(GridLayout layout) {
		nrChaptersField = initTextField(NR_CHAPTERS_FIELD, layout);
	}
	
	private TextField initTextField(String label, GridLayout layout) {
		TextField textField = new TextField();
		textField.setWriteThrough(true);
		textField.setImmediate(true);

		layout.addComponent(new Label(label));
		layout.addComponent(textField);
		return textField;
	}
	
	protected void initAuthorsField(GridLayout layout) {
		authorsSelect = new ListSelect();
		authorsSelect.setWriteThrough(true);
		authorsSelect.setImmediate(true);
		authorsSelect.setNewItemsAllowed(true);
		authorsSelect.setMultiSelect(true);

		layout.addComponent(new Label(AUTHOR_FIELD));
		layout.addComponent(authorsSelect);
	}

	protected void initDescriptionField(GridLayout layout) {
		descriptionField = new TextArea();
		descriptionField.setRows(4);
		descriptionField.setColumns(35);

		layout.addComponent(new Label(DESCRIPTION_FIELD));
		layout.addComponent(descriptionField);
	}

	protected void initButtons(GridLayout layout) {
		final Button saveButton = new Button("Create new project");
		saveButton.addListener(new Button.ClickListener() {

			private static final long serialVersionUID = 1L;

			public void buttonClick(ClickEvent event) {
				if(bookTitleField.getValue().toString().length() == 0) {
					viewManager.showErrorMessage("Book title is required");
					
				} else if(nrChaptersField.getValue().toString().length() == 0) {
					viewManager.showErrorMessage("Nr of chapters is required");
				
				} else {
					User reviewer = null;
					Map<String, User> userMap = viewManager.getApplication().getUserMap();
					for (User user : userMap.values()) {
	          if(user.getRole().equals(User.ROLE_REVIEWER)) {
	          	
	          	if(reviewer == null || user.getNumberOfReviews() < reviewer.getNumberOfReviews()) {
	          		reviewer = user;
	          	}
	          }
          }
					reviewer.setNumberOfReviews(reviewer.getNumberOfReviews() + 1);
					
					Map<String, Object> variableMap = new HashMap<String, Object>();
					variableMap.put("bookProject", new BookProject()
							.setTitle(bookTitleField.getValue().toString())
							.setNrOfChapters(Integer.valueOf(nrChaptersField.getValue().toString()))
							.setDescription(descriptionField.getValue().toString())
							.setReviewer(reviewer.getUsername()));
					try {
						viewManager.getApplication().getContextHelper()
								.getRuntimeService().startProcessInstanceByKey("bookWritingProcess", variableMap);
						viewManager.showInfoMessage("New book project for " + bookTitleField.getValue().toString() + " was created");
						viewManager.showBookProjectsPage();
					} catch (Exception e) {
						viewManager.showErrorMessage(e.getMessage());
					}
				}
			}
		});

		HorizontalLayout footer = new HorizontalLayout();
		footer.setSpacing(true);
		footer.addComponent(saveButton);
		layout.addComponent(new Label());
		layout.addComponent(footer);
	}

}
