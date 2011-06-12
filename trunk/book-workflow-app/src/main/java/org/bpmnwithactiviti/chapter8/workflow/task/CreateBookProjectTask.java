package org.bpmnwithactiviti.chapter8.workflow.task;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.bpmnwithactiviti.chapter8.workflow.model.BookProject;
import org.bpmnwithactiviti.chapter8.workflow.model.Chapter;

public class CreateBookProjectTask implements JavaDelegate {

	@Override
  public void execute(DelegateExecution execution) throws Exception {
	  BookProject bookProject = (BookProject) execution.getVariable("bookProject");
	  Calendar productionCal = new GregorianCalendar();
	  productionCal.add(Calendar.DAY_OF_YEAR, 20 * bookProject.getNrOfChapters());
	  bookProject.setToProductionDate(productionCal.getTime());
	  for(int i = 1; i < bookProject.getNrOfChapters(); i++) {
	  	bookProject.addChapter(new Chapter().setChapterNumber(i)
	  			.setTitle(bookProject.getTitle() + " chapter " + i));
	  }
	  execution.setVariable("bookProject", bookProject);
  }

}
