package org.bpmnwithactiviti.explorer.ui.management;

import org.activiti.explorer.ExplorerApp;
import org.activiti.explorer.navigation.ManagementNavigator;
import org.activiti.explorer.navigation.UriFragment;


public class BookManagementNavigator extends ManagementNavigator {

 public static final String MANAGEMENT_URI_PART = "book_management";
  
  public String getTrigger() {
    return MANAGEMENT_URI_PART;
  }
  
  public void handleManagementNavigation(UriFragment uriFragment) {
    String managementId = uriFragment.getUriPart(1);
    
    if(managementId != null) {
      ExplorerApp.get().getViewManager().showBookManagementPage(managementId);
    } else {
      ExplorerApp.get().getViewManager().showBookManagementPage();
    }
  }


}
