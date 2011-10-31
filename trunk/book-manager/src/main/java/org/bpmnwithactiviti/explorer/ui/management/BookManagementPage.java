package org.bpmnwithactiviti.explorer.ui.management;

import org.activiti.explorer.ExplorerApp;
import org.activiti.explorer.navigation.UriFragment;
import org.activiti.explorer.ui.management.ManagementPage;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.ui.Table;


public class BookManagementPage extends ManagementPage {

  private static final long serialVersionUID = 1L;
  protected String managementId;
  protected Table managementTable;
  
  public BookManagementPage() {
    ExplorerApp.get().setCurrentUriFragment(
            new UriFragment(BookManagementNavigator.MANAGEMENT_URI_PART));
  }
  
  public BookManagementPage(String managementId) {
    this.managementId = managementId;
  }
  
  @Override
  protected void initUi() {
    super.initUi();
    int index = 0;
    if (managementId != null) {
      index = Integer.valueOf(managementId);
    }
    managementTable.select(index);
    managementTable.setCurrentPageFirstItemId(index);
  }

  protected Table createList() {
  	managementTable = new Table();
    
  	managementTable.setEditable(false);
  	managementTable.setImmediate(true);
  	managementTable.setSelectable(true);
  	managementTable.setNullSelectionAllowed(false);
  	managementTable.setSortDisabled(true);
  	managementTable.setSizeFull();
    
    // Column headers
    managementTable.addContainerProperty("name", String.class, null);
    managementTable.setColumnHeaderMode(Table.COLUMN_HEADER_MODE_HIDDEN);
    
    managementTable.addItem(new String[] {"Running process instances"}, 0);
    managementTable.addItem(new String[] {"Completed process instances"}, 1);
    managementTable.addItem(new String[] {"Database settings"}, 2);
    managementTable.addItem(new String[] {"History settings"}, 3);
            
    // Listener to change right panel when clicked on a user
    managementTable.addListener(new Property.ValueChangeListener() {
      private static final long serialVersionUID = 1L;
      public void valueChange(ValueChangeEvent event) {
        Item item = managementTable.getItem(event.getProperty().getValue()); // the value of the property is the itemId of the table entry
        if(item != null) {
        	
        	if("0".equals(event.getProperty().getValue().toString())) {
        		setDetailComponent(new RunningInstancesPanel());
        	} else if("1".equals(event.getProperty().getValue().toString())) {
        		setDetailComponent(new CompletedInstancesPanel());
        	} else if("2".equals(event.getProperty().getValue().toString())) {
        		setDetailComponent(new DatabaseSettingsPanel());
        	} else if("3".equals(event.getProperty().getValue().toString())) {
        		setDetailComponent(new HistorySettingsPanel());
        	}
          
          // Update URL
          ExplorerApp.get().setCurrentUriFragment(
            new UriFragment(BookManagementNavigator.MANAGEMENT_URI_PART, event.getProperty().getValue().toString()));
        } else {
          // Nothing is selected
          setDetailComponent(null);
          ExplorerApp.get().setCurrentUriFragment(new UriFragment(BookManagementNavigator.MANAGEMENT_URI_PART, managementId));
        }
      }
    });
    
    return managementTable;
  }
  
  public void notifyGroupChanged(String managementId) {
    // Clear cache
  	managementTable.removeAllItems();
    
    // select changed group
    managementTable.select(Integer.valueOf(managementId));
  }
  
}
