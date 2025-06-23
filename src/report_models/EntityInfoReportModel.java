package report_models;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import model.AssociatedEntity;

import java.io.FileOutputStream;

public class EntityInfoReportModel {
    public static void saveEntityInfoReport(AssociatedEntity entity, String filePath) throws Exception {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(filePath));
        document.open();
        document.add(new Paragraph("Associated Entity Information"));
        document.add(new Paragraph(" "));
        document.add(new Paragraph("Entity Name: " + entity.getEntityName()));
        document.add(new Paragraph("Type: " + entity.getEntityType()));
        document.add(new Paragraph("Address: " + entity.getAddress()));
        document.add(new Paragraph("Phone: " + entity.getPhoneNumber()));
        document.add(new Paragraph("Email: " + entity.getContactEmail()));
        document.add(new Paragraph("Director: " + entity.getDirectorName()));
        document.close();
    }
}
