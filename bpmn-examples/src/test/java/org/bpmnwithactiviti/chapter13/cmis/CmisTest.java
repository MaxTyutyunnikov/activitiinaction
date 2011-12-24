package org.bpmnwithactiviti.chapter13.cmis;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.List;

import org.apache.chemistry.opencmis.client.api.CmisObject;
import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.client.api.Session;
import org.junit.Test;

public class CmisTest {
	
	private static final String ALFRESCO_CMIS_URL = "http://localhost:9090/alfresco/service/cmis";

	@Test
	public void retrieveFolder() throws Exception {
		Session session = CmisUtil.createCmisSession("admin", "secret", ALFRESCO_CMIS_URL);
		Folder folder = CmisUtil.getFolder(session, "myExpenses");
		assertNotNull(folder);
		assertEquals(1, folder.getChildren().getTotalNumItems());
		CmisObject cmisObject = folder.getChildren().iterator().next();
		assertTrue(cmisObject instanceof Document);
		Document document = (Document) cmisObject;
		System.out.println("document name " + document.getName());
		System.out.println("document type " + document.getType().getDisplayName());
		System.out.println("created by " + document.getCreatedBy());
		System.out.println("created date " + document.getCreationDate().getTime());
		FileOutputStream output = new FileOutputStream(document.getName());
		InputStream repoDocument = document.getContentStream().getStream();
		byte[] buffer = new byte[1024];
		while(repoDocument.read(buffer) != -1) {
			output.write(buffer);
		}
		output.close();
		repoDocument.close();
	}
	
	@Test
	public void versionDocument() {
		// Create CMIS session with Alfresco
		Session session = CmisUtil.createCmisSession("admin", "secret", ALFRESCO_CMIS_URL);
		
		Document doc = (Document) session.getObject("workspace://SpacesStore/007df67f-28a8-4973-a39b-459c835c0712");
		
		Document pwc = (Document) session.getObject(doc.checkOut());
		
		// Check in the pwc
		try {
			pwc.checkIn(true, null, pwc.getContentStream(), "New version after group review");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("checkin failed, trying to cancel the checkout");
			pwc.cancelCheckOut();
		}

		System.out.println("Document version history");
		
		List<Document> versions = doc.getAllVersions();
		for (Document version : versions) {
			System.out.println("\tname: " + version.getName());
			System.out.println("\tversion label: " + version.getVersionLabel());
			System.out.println("\tlast modified by: "
			    + version.getLastModifiedBy());
			System.out.println("\tlatest version: " + version.isLatestVersion());
			System.out.println("\tcheckin comment: "
			    + version.getCheckinComment() + "\n");
		}
	}
}
