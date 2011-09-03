package org.bpmnwithactiviti.chapter8.workflow.ui.table;

import com.vaadin.ui.Button;
import com.vaadin.ui.Table;

public class TaskChapterTable extends Table {

  private static final long serialVersionUID = 6521446909987945815L;

  public TaskChapterTable() {
    setEditable(false);
    setColumnReorderingAllowed(true);
    setPageLength(size());

    addContainerProperty("id", String.class, null);
    addContainerProperty("name", String.class, null);
    addContainerProperty("bookTitle", String.class, null);
    addContainerProperty("chapterNumber", Integer.class, null);
    addContainerProperty("assignee", String.class, null);
    addContainerProperty("attachment", Button.class, "No attachment");

    setColumnHeader("id", "Id");
    setColumnHeader("name", "Name");
    setColumnHeader("bookTitle", "Book title");
    setColumnHeader("chapterNumber", "Chapter number");
    setColumnHeader("assignee", "Assignee");
    setColumnHeader("attachment", "Attachment");
  }

}
