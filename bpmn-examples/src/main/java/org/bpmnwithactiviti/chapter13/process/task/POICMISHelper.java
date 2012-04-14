package org.bpmnwithactiviti.chapter13.process.task;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.apache.chemistry.opencmis.client.api.CmisObject;
import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.client.api.ObjectType;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.bpmnwithactiviti.chapter13.cmis.CmisUtil;

public class POICMISHelper {
	
	private static final String ALFRESCO_CMIS_URL = "http://localhost:9090/alfresco/service/cmis";
	private static final String ALFRESCO_ADMIN_PASSWORD = "rademakers";
	
	public Folder documentFolder;
	
	private Session session;
	private InputStream sheetInputStream;
	private XSSFWorkbook excelWorkbook;
	private Sheet sheet;
	private XSSFFormulaEvaluator evaluator;

  public void openWorkbook(String cmisWorkbookId) {
  	session = CmisUtil.createCmisSession("admin", ALFRESCO_ADMIN_PASSWORD, ALFRESCO_CMIS_URL);
		Document doc = (Document) session.getObject(cmisWorkbookId);
		sheetInputStream = doc.getContentStream().getStream();
		try {
			excelWorkbook = new XSSFWorkbook(sheetInputStream);
		} catch(Exception e) {
			throw new ActivitiException("Error opening Excel workbook", e);
		}
    sheet = excelWorkbook.getSheetAt(0);
    evaluator = new XSSFFormulaEvaluator(excelWorkbook);
  }
  
  public void createCmisSession() {
  	session = CmisUtil.createCmisSession("admin", ALFRESCO_ADMIN_PASSWORD, ALFRESCO_CMIS_URL);
  }
	
  public void setCellValue(String text, int rowNumber, int cellNumber, boolean create) {
  	Cell cell = getCell(rowNumber, cellNumber, create);
    cell.setCellValue(text);
  }
  
  public void setCellValue(Long number, int rowNumber, int cellNumber, boolean create) {
  	Cell cell = getCell(rowNumber, cellNumber, create);
    cell.setCellValue(number);
  }
  
  public void evaluateFormulaCell(int rowNumber, int cellNumber) {
    evaluator.evaluateFormulaCell(getCell(rowNumber, cellNumber));
  }
  
  public boolean getBooleanCellValue(int rowNumber, int cellNumber) {
  	return getCell(rowNumber, cellNumber).getBooleanCellValue();
  }
  
  public String getStringCellValue(int rowNumber, int cellNumber) {
  	return getCell(rowNumber, cellNumber).getStringCellValue();
  }
  
  public void recalculateSheetAfterOpening() {
  	sheet.setForceFormulaRecalculation(true);
  }
  
  public Cell getCell(int rowNumber, int cellNumber) {
  	return getCell(rowNumber, cellNumber, false);
  }
  
  private Cell getCell(int rowNumber, int cellNumber, boolean create) {
  	Row row = sheet.getRow(rowNumber);
  	Cell cell = null;
  	if(create) {
  		cell = row.createCell(cellNumber);
  	} else {
  		cell = row.getCell(cellNumber);
  	}
  	return cell;
  }
    
  public Document saveWorkbookToFolder(String folderName, String fileSuffix, String mimeType) {
  	documentFolder = getFolder(folderName);
    ByteArrayOutputStream customerSheetOutputStream = new ByteArrayOutputStream(1024);
    try {
	    excelWorkbook.write(customerSheetOutputStream);
	    byte[] content = customerSheetOutputStream.toByteArray();
	    
	    return CmisUtil.createDocument(session, documentFolder, folderName +
	    		"-" + new SimpleDateFormat("yyyyMMddhhmmss").format(new Date()) + fileSuffix, 
	    		mimeType, content);
    } catch(Exception e) {
    	throw new ActivitiException("Error storing document in CMIS repository", e);
    } finally {
    	try {
		    customerSheetOutputStream.close();
		    sheetInputStream.close();
    	} catch(Exception e) {}
    }
	}
  
  private Folder getFolder(String folderName) {
  	Folder parentFolder = CmisUtil.getFolder(session, "loanapplication");
    Folder folder = containsFolderWithName(folderName, parentFolder);
    if(folder == null) {
    	folder = CmisUtil.createFolder(session, parentFolder, folderName);
    }
    return folder;
  }
  
  public Document saveDocumentToFolder(ByteArrayOutputStream documentStream, String folderId, String name, 
  		String fileSuffix, String mimeType) {
  	
  	try {
	    byte[] content = documentStream.toByteArray();
	    Folder folder = (Folder) session.getObject(folderId);
	    return CmisUtil.createDocument(session, folder, name +
	    		"-" + new SimpleDateFormat("yyyyMMddhhmmss").format(new Date()) + fileSuffix, 
	    		mimeType, content);
    } catch(Exception e) {
    	throw new ActivitiException("Error storing document in CMIS repository", e);
    } finally {
    	try {
    		documentStream.close();
    	} catch(Exception e) {}
    }
  }
  
  public void attachDocumentToProcess(String processInstanceId, Document document, String fileSuffix, String fileDescription) {
  	ProcessEngine processEngine = ProcessEngines.getProcessEngines().get(ProcessEngines.NAME_DEFAULT);
    processEngine.getTaskService().createAttachment(fileSuffix, null, processInstanceId, 
    		document.getName().substring(0, document.getName().lastIndexOf(".")), 
    		fileDescription, document.getContentStream().getStream());
  }
	
	private Folder containsFolderWithName(String name, Folder parentFolder) {
		Folder found = null;
		for(CmisObject cmisObject : parentFolder.getChildren()) {
			System.out.println("name " + name + " cmis " + cmisObject.getName());
			if(cmisObject.getProperty(PropertyIds.OBJECT_TYPE_ID).getValueAsString().equals(ObjectType.FOLDER_BASETYPE_ID) &&
					name.equals(cmisObject.getName())) {
				
				found = (Folder) cmisObject;
				break;
			}
		}
		return found;
	}

}
