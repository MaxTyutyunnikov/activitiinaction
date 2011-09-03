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

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.TaskService;
import org.activiti.engine.task.Attachment;
import org.activiti.engine.task.Task;
import org.bpmnwithactiviti.chapter8.workflow.model.Chapter;
import org.bpmnwithactiviti.chapter8.workflow.model.User;
import org.bpmnwithactiviti.chapter8.workflow.ui.ViewManager;
import org.bpmnwithactiviti.chapter8.workflow.ui.model.ChapterCase;
import org.bpmnwithactiviti.chapter8.workflow.ui.popup.UploadPopupWindow;
import org.bpmnwithactiviti.chapter8.workflow.ui.table.TaskChapterTable;
import org.bpmnwithactiviti.chapter8.workflow.ui.util.InputStreamStreamSource;

import com.vaadin.data.Item;
import com.vaadin.terminal.Resource;
import com.vaadin.terminal.StreamResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.Window.CloseListener;
import com.vaadin.ui.themes.Reindeer;

public class CasesOverviewPanel extends Panel {

	protected static final long serialVersionUID = 1L;

	//ui
	protected Table tasksTable;
	protected Table myTasksTable;
	protected User user;

	// dependencies
	protected ViewManager viewManager;

	public CasesOverviewPanel(ViewManager viewManager) {
		this.viewManager = viewManager;
		init();
	}

	protected void init() {
		user = (User) viewManager.getApplication().getUser();
		setSizeFull();
		setStyleName(Reindeer.PANEL_LIGHT);
		initUi();
	}

	protected void initUi() {
		initTitle();

		GridLayout layout = new GridLayout(3, 2);
		layout.setSpacing(true);
		addComponent(layout);

		if(user.getRole().equals(User.ROLE_REVIEWER) == false) {
			initTasksTable(layout);
		}
		initMyTasksTable(layout);
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
		tasksTable = new TaskChapterTable();
		tasksTable.setSelectable(true);
		tasksTable.setImmediate(true);
		fillCases();
    layout.addComponent(new Label("Tasks"));
    layout.addComponent(tasksTable);
    
    VerticalLayout buttonLayout = new VerticalLayout();
    Button assignButton = new Button("Assign to me");
    assignButton.addListener(new ClickListener() {

      private static final long serialVersionUID = 1L;

			@Override
      public void buttonClick(ClickEvent event) {
				String taskId = (String) tasksTable.getValue();
				if(taskId != null) {
					viewManager.getApplication().getContextHelper()
    					.getTaskService().claim(taskId, ((User) viewManager.getApplication().getUser()).getUsername());
					refreshCases();
				}
      }
    	
    });
    buttonLayout.addComponent(assignButton);
    layout.addComponent(buttonLayout);
  }
	
	protected void initMyTasksTable(GridLayout layout) {
		myTasksTable = new TaskChapterTable();
		myTasksTable.setSelectable(true);
		myTasksTable.setImmediate(true);
		fillMyCases();
    layout.addComponent(new Label("My tasks"));
    layout.addComponent(myTasksTable);
    VerticalLayout buttonLayout = null;
    if(user.getRole().equals(User.ROLE_REVIEWER)) {
    	buttonLayout = createReviewerButtons();
    } else {
    	buttonLayout = createAuthorButtons();
    } 
    
    layout.addComponent(buttonLayout);
  }
	
	private void completeReviewTask(boolean approved) {
		String taskId = (String) myTasksTable.getValue();
		if(taskId != null) {
			Map<String, Object> taskVarMap = new HashMap<String, Object>();
			taskVarMap.put("chapterApproved", approved);
			viewManager.getApplication().getContextHelper()
					.getTaskService().complete(taskId, taskVarMap);
			refreshCases();
		}
	}
	
	private VerticalLayout createReviewerButtons() {
		VerticalLayout buttonLayout = new VerticalLayout();
    Button approveButton = new Button("Approve");
    approveButton.addListener(new ClickListener() {

      private static final long serialVersionUID = 1L;

			@Override
      public void buttonClick(ClickEvent event) {
				completeReviewTask(true);
      }
    	
    });
    buttonLayout.addComponent(approveButton);
    
    Button disapproveButton = new Button("Disapprove");
    disapproveButton.addListener(new ClickListener() {

      private static final long serialVersionUID = 1L;

			@Override
      public void buttonClick(ClickEvent event) {
				completeReviewTask(false);
      }
    	
    });
    buttonLayout.addComponent(disapproveButton);
    return buttonLayout;
	}
	
	private VerticalLayout createAuthorButtons() {
		VerticalLayout buttonLayout = new VerticalLayout();
    Button releaseButton = new Button("Release");
    releaseButton.addListener(new ClickListener() {

      private static final long serialVersionUID = 1L;

			@Override
      public void buttonClick(ClickEvent event) {
				String taskId = (String) myTasksTable.getValue();
				if(taskId != null) {
					viewManager.getApplication().getContextHelper()
    					.getTaskService().setAssignee(taskId, null);
					refreshCases();
				}
      }
    	
    });
    buttonLayout.addComponent(releaseButton);
    
    Button attachmentButton = new Button("Upload attachment");
    attachmentButton.addListener(new ClickListener() {

      private static final long serialVersionUID = 1L;

			@Override
      public void buttonClick(ClickEvent event) {
				final String taskId = (String) myTasksTable.getValue();
				if(taskId != null) {
					UploadPopupWindow uploadWindow = new UploadPopupWindow();
					uploadWindow.addListener(new CloseListener() {

            private static final long serialVersionUID = 1L;

						@Override
            public void windowClose(CloseEvent e) {
							UploadPopupWindow uploadPopup = (UploadPopupWindow) e.getWindow();
							viewManager.getApplication().getContextHelper()
		    					.getTaskService().createAttachment(uploadPopup.getMimeType(), 
		    							taskId, null, uploadPopup.getFileName(), null, 
		    							new ByteArrayInputStream(uploadPopup.getByteArrayOutputStream().toByteArray()));
							refreshCases();
            }
						
					});
					viewManager.getApplication().getMainWindow().addWindow(uploadWindow);
					refreshCases();
				}
      }
    	
    });
    buttonLayout.addComponent(attachmentButton);
    
    Button completeButton = new Button("Complete");
    completeButton.addListener(new ClickListener() {

      private static final long serialVersionUID = 1L;

			@Override
      public void buttonClick(ClickEvent event) {
				String taskId = (String) myTasksTable.getValue();
				if(taskId != null) {
					viewManager.getApplication().getContextHelper()
    					.getTaskService().complete(taskId);
					refreshCases();
				}
      }
    	
    });
    buttonLayout.addComponent(completeButton);
    return buttonLayout;
	}
	
	private void refreshCases() {
		if(user.getRole().equals(User.ROLE_REVIEWER) == false) {
			fillCases();
		}
		fillMyCases();
	}
	
	private void fillMyCases() {
		myTasksTable.removeAllItems();
		TaskService taskService = viewManager.getApplication().getContextHelper().getTaskService();
		User user = (User) viewManager.getApplication().getUser();
		List<Task> taskList = taskService.createTaskQuery().taskAssignee(user.getUsername()).list();
		List<ChapterCase> caseList = createCaseList(taskList);
		fillCasesInTable(caseList, myTasksTable);
	}
	
	private void fillCases() {
		tasksTable.removeAllItems();
		TaskService taskService = viewManager.getApplication().getContextHelper().getTaskService();
		User user = (User) viewManager.getApplication().getUser();
		List<Task> taskList = null;
		if(user.getRole().equals(User.ROLE_ADMIN)) {
			taskList = taskService.createTaskQuery().list();
		} else {
			taskList = taskService.createTaskQuery().taskCandidateUser(user.getUsername()).list();
		}
		
		List<ChapterCase> caseList = createCaseList(taskList);
		fillCasesInTable(caseList, tasksTable);
	}
	
	private List<ChapterCase> createCaseList(List<Task> taskList) {
		TaskService taskService = viewManager.getApplication().getContextHelper().getTaskService();
		List<ChapterCase> caseList = new ArrayList<ChapterCase>();
		for (Task task : taskList) {
			Chapter chapter = (Chapter) taskService.getVariable(task.getId(), "chapter");
			ChapterCase caze = new ChapterCase();
			caze.task = task;
			caze.chapter = chapter;
			caseList.add(caze);
		}
		Collections.sort(caseList, new Comparator<ChapterCase>() {

			@Override
      public int compare(ChapterCase caze1, ChapterCase caze2) {
	      if(caze1.chapter.getBookTitle().equals(caze2.chapter.getBookTitle()) == false) {
	      	return caze1.chapter.getBookTitle().compareTo(caze2.chapter.getBookTitle());
	      } else {
	      	return caze1.chapter.getChapterNumber().compareTo(caze2.chapter.getChapterNumber());
	      }
      }
		});
		return caseList;
	}
	
	private void fillCasesInTable(List<ChapterCase> caseList, Table table) {
		for (ChapterCase caze : caseList) {
			
			Item newItem = table.addItem(caze.task.getId());
		  
		  newItem.getItemProperty("id").setValue(caze.task.getProcessInstanceId());
		  newItem.getItemProperty("name").setValue(caze.task.getName());
		  newItem.getItemProperty("bookTitle").setValue(caze.chapter.getBookTitle());
		  newItem.getItemProperty("chapterNumber").setValue(caze.chapter.getChapterNumber());
		  
		  String assignee = "Not assigned";
		  if(caze.task.getAssignee() != null) {
		  	assignee = caze.task.getAssignee();
		  }
		  newItem.getItemProperty("assignee").setValue(assignee);
		  
		  List<Attachment> attachmentList = viewManager.getApplication().getContextHelper().getTaskService().getTaskAttachments(caze.task.getId());
		  if(attachmentList != null && attachmentList.size() > 0) {
		  	
			  Button detailsField = new Button("show chapter");
		    detailsField.setData(attachmentList.get(0));
		    detailsField.addListener(new Button.ClickListener() {
		     
          private static final long serialVersionUID = 1L;

					public void buttonClick(ClickEvent event) {
            Attachment attachment = (Attachment) event.getButton().getData();
            TaskService taskService = viewManager.getApplication().getContextHelper().getTaskService();
            Resource res = new StreamResource(new InputStreamStreamSource(taskService.getAttachmentContent(attachment.getId())),
                attachment.getName() + extractExtention(attachment.getType()), viewManager.getApplication());   
			      viewManager.getApplication().getMainWindow().open(res);
	        } 
		    });
		    detailsField.addStyleName("link");
		    newItem.getItemProperty("attachment").setValue(detailsField);
		  }
		}
	}
	
	private String extractExtention(String type) {
    int lastIndex = type.lastIndexOf('/');
    if(lastIndex > 0 && lastIndex < type.length() - 1) {
      return "." + type.substring(lastIndex + 1);
    }
    return "." + type;
  }
}
