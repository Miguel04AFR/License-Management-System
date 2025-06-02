package report_models;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;

public class InfractionsInPeriodReportModel {
    public static void saveInfractionsInPeriod(String filePath, String[] columns, Object[][] rows) throws Exception {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(filePath));
        document.open();

        // Título
        Font titleFont = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD);
        Paragraph title = new Paragraph("Infractions Registered in Period", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);
        document.add(Chunk.NEWLINE);

        // Tabla
        PdfPTable pdfTable = new PdfPTable(columns.length);
        pdfTable.setWidthPercentage(100);

        // Encabezados
        Font headerFont = new Font(Font.FontFamily.HELVETICA, 12, com.itextpdf.text.Font.BOLD);
        for (String col : columns) {
            PdfPCell cell = new PdfPCell(new Phrase(col, headerFont));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            pdfTable.addCell(cell);
        }

        // Filas
        Font cellFont = new Font(Font.FontFamily.HELVETICA, 10);
        for (Object[] row : rows) {
            for (Object value : row) {
                pdfTable.addCell(new Phrase(value != null ? value.toString() : "", cellFont));
            }
        }

        document.add(pdfTable);
        document.close();
    }


}
