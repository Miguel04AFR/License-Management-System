package report_models;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class LicensesIssuedReportModel {
    public static void saveLicensesIssuedReport(java.io.File file, String[] columns, Object[][] rows) throws Exception {
        Document document = new Document(PageSize.A4.rotate());
        PdfWriter.getInstance(document, new java.io.FileOutputStream(file));
        document.open();

        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
        Paragraph title = new Paragraph("Licenses Issued in Period", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);
        document.add(Chunk.NEWLINE);

        PdfPTable pdfTable = new PdfPTable(columns.length);
        pdfTable.setWidthPercentage(100);

        Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        for (String col : columns) {
            PdfPCell cell = new PdfPCell(new Phrase(col, headFont));
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            pdfTable.addCell(cell);
        }

        for (Object[] row : rows) {
            for (Object value : row) {
                PdfPCell cell = new PdfPCell(new Phrase(value != null ? value.toString() : ""));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                pdfTable.addCell(cell);
            }
        }

        document.add(pdfTable);
        document.close();
    }
}
