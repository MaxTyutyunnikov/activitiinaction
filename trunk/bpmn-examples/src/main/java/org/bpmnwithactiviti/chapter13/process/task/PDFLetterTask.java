package org.bpmnwithactiviti.chapter13.process.task;

import java.io.ByteArrayOutputStream;
import java.net.URL;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.bpmnwithactiviti.chapter13.process.model.LoanApplication;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

public class PDFLetterTask implements JavaDelegate {

	public void execute(DelegateExecution execution) throws Exception {
		LoanApplication loanApplication = (LoanApplication) execution.getVariable("loanApplication");
		
		com.itextpdf.text.Document pdf = new com.itextpdf.text.Document();
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream(1024);
		PdfWriter writer = PdfWriter.getInstance(pdf, outputStream);
		writer.setBoxSize("art", new Rectangle(36, 54, 559, 788));
    HeaderFooter event = new HeaderFooter();
    writer.setPageEvent(event);
    pdf.addAuthor("Loan Sharks");
    pdf.addTitle("Subject: loan request");
    pdf.addSubject("Reference number 100898");
    pdf.open();
    URL imageUrl = PDFLetterTask.class.getClassLoader().getResource("chapter13/cmis/tshark3.gif");
		Image image = Image.getInstance(imageUrl);
		pdf.add(image);
		pdf.add(new Paragraph(" "));
		pdf.add(new Paragraph(" "));
		pdf.add(new Paragraph(" "));
		pdf.add(new Paragraph("Dear Mr/Mrs " + loanApplication.getApplicant().getName() + ","));
		pdf.add(new Paragraph(" "));
		
		if("approved".equalsIgnoreCase(loanApplication.getStatus()) ||
				"approved by manager".equalsIgnoreCase(loanApplication.getStatus())) {
			
			pdf.add(new Paragraph("After analysis regarding your loan request we are happy to inform you that your loan request for $"
					+ loanApplication.getApplicant().getLoanAmount() + " is approved. Enclosed, you'll find all the details regarding the next steps in the process of your loan request."));
		} else {
			pdf.add(new Paragraph("After analysis regarding your loan request we regret to inform you that your loan request for $"
					+ loanApplication.getApplicant().getLoanAmount() + " is denied."));
		}
		pdf.add(new Paragraph(" "));
		pdf.add(new Paragraph(" "));
		pdf.add(new Paragraph(" "));
		pdf.add(new Paragraph("With regards,"));
		pdf.add(new Paragraph(" "));
		pdf.add(new Paragraph(" "));
		pdf.add(new Paragraph("John Shark"));
		pdf.add(new Paragraph("Manager Loan Sharks"));
		pdf.close();
		
		POICMISHelper helper = new POICMISHelper();
		helper.createCmisSession();
    helper.saveDocumentToFolder(outputStream, (String) execution.getVariable("documentFolderId"),
    		loanApplication.getApplicant().getName(), ".pdf", "application/pdf");
	}
	
	/** Inner class to add a header and a footer. */
  static class HeaderFooter extends PdfPageEventHelper {

    public void onEndPage (PdfWriter writer, Document document) {
      Rectangle rect = writer.getBoxSize("art");
      ColumnText.showTextAligned(writer.getDirectContent(),
              Element.ALIGN_RIGHT, new Phrase("Loan Sharks"),
              rect.getRight(), rect.getTop(), 0);
      
      ColumnText.showTextAligned(writer.getDirectContent(),
              Element.ALIGN_RIGHT, new Phrase("4543 1st Street"),
              rect.getRight(), rect.getTop() - 15, 0);
      
      ColumnText.showTextAligned(writer.getDirectContent(),
              Element.ALIGN_RIGHT, new Phrase("Bay City, 38989 "),
              rect.getRight(), rect.getTop() - 30, 0);
      
      ColumnText.showTextAligned(writer.getDirectContent(),
              Element.ALIGN_RIGHT, new Phrase("E-mail: info@loansharks.com"),
              rect.getRight(), rect.getTop() - 60, 0);
    }
  }
}
