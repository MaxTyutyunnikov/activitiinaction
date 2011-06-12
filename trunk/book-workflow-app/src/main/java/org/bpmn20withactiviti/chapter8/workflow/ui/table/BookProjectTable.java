package org.bpmn20withactiviti.chapter8.workflow.ui.table;

import com.vaadin.ui.Table;

public class BookProjectTable extends Table {

  private static final long serialVersionUID = 6521446909987945815L;

  public BookProjectTable() {
    setEditable(false);
    setColumnReorderingAllowed(true);
    setPageLength(size());

    addContainerProperty("id", String.class, null);
    addContainerProperty("name", String.class, null);

    setColumnHeader("id", "Id");
    setColumnHeader("name", "Name");
  }

}
