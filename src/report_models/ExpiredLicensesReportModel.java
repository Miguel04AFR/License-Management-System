package report_models;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;

public class ExpiredLicensesReportModel {
    public static void saveExpiredLicensesReport(String filePath, String[] columns, Object[][] rows) throws Exception {
        Document document = new Document(PageSize.A4.rotate(), 36, 36, 36, 36);
        PdfWriter.getInstance(document, new FileOutputStream(filePath));
        document.open();

        // Título
        Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
        Paragraph title = new Paragraph("Drivers with Expired Licenses", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(20);
        document.add(title);

        // Tabla
        PdfPTable pdfTable = new PdfPTable(columns.length);
        pdfTable.setWidthPercentage(100);

        // Cabeceras
        Font headerFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
        for (String col : columns) {
            PdfPCell headerCell = new PdfPCell(new Phrase(col, headerFont));
            headerCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            pdfTable.addCell(headerCell);
        }

        // Filas
        Font cellFont = new Font(Font.FontFamily.HELVETICA, 11, Font.NORMAL);
        for (Object[] row : rows) {
            for (Object cell : row) {
                PdfPCell pdfCell = new PdfPCell(new Phrase(cell != null ? cell.toString() : "", cellFont));
                pdfCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                pdfTable.addCell(pdfCell);
            }
        }
        document.add(pdfTable);
        document.close();
    }
}
