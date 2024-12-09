package com.vakya.pdf.service;


import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import com.vakya.pdf.models.Item;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class PDFGenerationService {

    public String generatePdf(String seller, String sellerGstin, String sellerAddress,
                              String buyer, String buyerGstin, String buyerAddress,
                              List<Item> items) throws DocumentException, IOException {

        // Create a new document
        Document document = new Document();
        File pdfFile = new File("generated_invoice.pdf");
        PdfWriter.getInstance(document, new FileOutputStream(pdfFile));

        // Open the document for writing
        document.open();

        // Add content to the document (seller, buyer, and items)
        document.add(new Paragraph("Seller: " + seller));
        document.add(new Paragraph("GSTIN: " + sellerGstin));
        document.add(new Paragraph("Address: " + sellerAddress));
        document.add(new Paragraph("\n"));
        document.add(new Paragraph("Buyer: " + buyer));
        document.add(new Paragraph("GSTIN: " + buyerGstin));
        document.add(new Paragraph("Address: " + buyerAddress));
        document.add(new Paragraph("\n"));

        // Add items (table format could be added later)
        document.add(new Paragraph("Items:"));
        for (Item item : items) {
            document.add(new Paragraph(item.getName() + " | " + item.getQuantity() + " | " + item.getRate() + " | " + item.getAmount()));
        }

        // Close the document
        document.close();

        // Return the path to the generated PDF
        return pdfFile.getAbsolutePath();
    }
}

