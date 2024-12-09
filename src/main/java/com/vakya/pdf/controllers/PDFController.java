package com.vakya.pdf.controllers;

import com.lowagie.text.*;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.vakya.pdf.dto.InvoiceRequest;
import com.vakya.pdf.models.Item;
import com.vakya.pdf.service.PDFGenerationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.awt.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api/pdf")
public class PDFController {

    @Autowired
    private PDFGenerationService pdfGenerationService;

    @PostMapping("/generate")
    public String generatePdf(@RequestBody InvoiceRequest invoiceRequest) throws DocumentException, IOException {
        // Create a document
        Document document = new Document();
        // Specify the path where the PDF will be saved
        String pdfPath = "D:/Project/pdf/generated_invoice.pdf";

        // Initialize PdfWriter
        PdfWriter.getInstance(document, new FileOutputStream(pdfPath));

        // Open the document
        document.open();

        // Create a main table
        PdfPTable mainTable = new PdfPTable(2); // 2 columns for seller and buyer
        mainTable.setWidthPercentage(100); // Table width as a percentage of page width
        mainTable.setSpacingBefore(10f);
        mainTable.setSpacingAfter(10f);
        mainTable.setWidths(new float[]{1, 1}); // Equal width for both columns

        // Add Seller Information
        PdfPCell sellerCell = new PdfPCell();
        sellerCell.addElement(new Paragraph("Seller:", FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
        sellerCell.addElement(new Paragraph(invoiceRequest.getSellerName()));
        sellerCell.addElement(new Paragraph(invoiceRequest.getSellerAddress()));
        sellerCell.addElement(new Paragraph("GSTIN: " + invoiceRequest.getSellerGstin()));
        sellerCell.setBorder(Rectangle.NO_BORDER); // Remove borders
        mainTable.addCell(sellerCell);

        // Add Buyer Information
        PdfPCell buyerCell = new PdfPCell();
        buyerCell.addElement(new Paragraph("Buyer:", FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
        buyerCell.addElement(new Paragraph(invoiceRequest.getBuyerName()));
        buyerCell.addElement(new Paragraph(invoiceRequest.getBuyerAddress()));
        buyerCell.addElement(new Paragraph("GSTIN: " + invoiceRequest.getBuyerGstin()));
        buyerCell.setBorder(Rectangle.NO_BORDER); // Remove borders
        mainTable.addCell(buyerCell);

        // Add the header for items
        PdfPCell itemsHeaderCell = new PdfPCell(new Paragraph("Items", FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
        itemsHeaderCell.setColspan(2); // Span across both columns
        itemsHeaderCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        itemsHeaderCell.setBackgroundColor(new Color(211, 211, 211));
        itemsHeaderCell.setPadding(5f);
        mainTable.addCell(itemsHeaderCell);

        // Create item table with 4 columns (inside the main table)
        PdfPTable itemTable = new PdfPTable(4);
        itemTable.setWidthPercentage(100);
        itemTable.setWidths(new float[]{3, 2, 2, 2}); // Adjust column widths

        // Add item table headers
        addTableHeader(itemTable);

        // Add items
        for (Item item : invoiceRequest.getItems()) {
            addTableRow(itemTable, item);
        }

        // Add item table to the main table
        PdfPCell itemTableCell = new PdfPCell(itemTable);
        itemTableCell.setColspan(2); // Span across both columns
        itemTableCell.setPadding(0);
        mainTable.addCell(itemTableCell);

        // Add the main table to the document
        document.add(mainTable);

        // Close the document
        document.close();

        return "PDF generated successfully! Path: " + pdfPath;
    }

    private void addTableHeader(PdfPTable table) {
        Stream.of("Item", "Quantity", "Rate", "Amount")
                .forEach(columnTitle -> {
                    PdfPCell header = new PdfPCell();
                    header.setBackgroundColor(new Color(211, 211, 211));
                    header.setBorderWidth(1);
                    header.setPhrase(new Phrase(columnTitle));
                    header.setHorizontalAlignment(Element.ALIGN_CENTER);
                    header.setPadding(5f);
                    table.addCell(header);
                });
    }

    private void addTableRow(PdfPTable table, Item item) {
        table.addCell(new Phrase(item.getName()));
        table.addCell(new Phrase(item.getQuantity() + " Nos"));
        table.addCell(new Phrase(String.format("%.2f", item.getRate())));
        table.addCell(new Phrase(String.format("%.2f", item.getAmount())));
    }

}

