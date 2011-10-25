package org.bpmnwithactiviti.chapter13.cmis;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

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
import org.junit.Test;

public class ExcelCMISTest {

	private static final String ALFRESCO_CMIS_URL = "http://localhost:9090/alfresco/service/cmis";
	
	@Test
	public void doCreditCheck() throws Exception {
		Session session = CmisUtil.createCmisSession("admin", "rademakers", ALFRESCO_CMIS_URL);
		Document doc = (Document) session.getObject("workspace://SpacesStore/a5715b04-7422-4e8c-bb8f-def83031103a");
		InputStream inputStream = doc.getContentStream().getStream();
		XSSFWorkbook wb = new XSSFWorkbook(inputStream);
    Sheet sheet = wb.getSheetAt(0);
    
    // Get income cell
    Row row = sheet.getRow(1);
    Cell cell = row.getCell(0);
    cell.setCellValue(500.0);
    
    // Get loan amount cell
    row = sheet.getRow(1);
    cell = row.getCell(1);
    cell.setCellValue(100.0);
    
    XSSFFormulaEvaluator evaluator = new XSSFFormulaEvaluator(wb);
    
    // Get loan amount double formula cell
    cell = row.getCell(2);
    evaluator.evaluateFormulaCell(cell);
    
    // Get credit check formula cell
    row = sheet.getRow(3);
    cell = row.getCell(1);
    evaluator.evaluateFormulaCell(cell);
    
    sheet.setForceFormulaRecalculation(true);
    
    System.out.println(cell.getBooleanCellValue());
    
    Folder parentFolder = CmisUtil.getFolder(session, "loanapplication");
    Folder folder = containsFolderWithName("test", parentFolder);
    if(folder == null) {
    	folder = CmisUtil.createFolder(session, parentFolder, "test");
    }
    
    ByteArrayOutputStream ssOut = new ByteArrayOutputStream(1024);
    wb.write(ssOut);
    byte[] content = ssOut.toByteArray();
    
    CmisUtil.createDocument(session, folder, "test-" + new SimpleDateFormat("yyyyMMddhhmmss").format(new Date()) + ".xls", 
    		"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", content);
    
    ssOut.close();
    inputStream.close();
	}
	
	private Folder containsFolderWithName(String name, Folder parentFolder) {
		Folder found = null;
		for(CmisObject cmisObject : parentFolder.getChildren()) {
			if(cmisObject.getProperty(PropertyIds.OBJECT_TYPE_ID).getValueAsString().equals(ObjectType.FOLDER_BASETYPE_ID) &&
					name.equals(cmisObject.getName())) {
				
				found = (Folder) cmisObject;
				break;
			}
		}
		return found;
	}
}
