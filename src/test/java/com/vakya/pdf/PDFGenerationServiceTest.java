package com.vakya.pdf;

import com.lowagie.text.DocumentException;
import com.vakya.pdf.models.Item;
import com.vakya.pdf.service.PDFGenerationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PDFGenerationServiceTest {
    private PDFGenerationService pdfGenerationService;

    @BeforeEach
    void setUp() {
        pdfGenerationService = new PDFGenerationService();
    }

    @Test
    void testGeneratePdf_Success() throws DocumentException, IOException {
        // Arrange: Define sample data
        String seller = "XYZ Pvt. Ltd.";
        String sellerGstin = "29AABBCCDD121ZD";
        String sellerAddress = "New Delhi, India";
        String buyer = "Vedant Computers";
        String buyerGstin = "29AABBCCDD131ZD";
        String buyerAddress = "Mumbai, India";

        List<Item> items = Arrays.asList(
                new Item("Product 1", 12, 123.00, 1476.00),
                new Item("Product 2", 8, 50.00, 400.00)
        );

        // Act: Call the method to generate the PDF
        String pdfPath = pdfGenerationService.generatePdf(seller, sellerGstin, sellerAddress, buyer, buyerGstin, buyerAddress, items);

        // Assert: Verify the PDF is generated and exists
        File pdfFile = new File(pdfPath);
        assertTrue(pdfFile.exists(), "Generated PDF file should exist");
        assertTrue(pdfFile.length() > 0, "Generated PDF file should not be empty");

        // Cleanup: Delete the generated file
        pdfFile.delete();
    }

    @Test
    void testGeneratePdf_EmptyItems() throws DocumentException, IOException {
        // Arrange: Define sample data with no items
        String seller = "XYZ Pvt. Ltd.";
        String sellerGstin = "29AABBCCDD121ZD";
        String sellerAddress = "New Delhi, India";
        String buyer = "Vedant Computers";
        String buyerGstin = "29AABBCCDD131ZD";
        String buyerAddress = "Mumbai, India";

        List<Item> items = Arrays.asList(); // Empty item list

        // Act: Call the method to generate the PDF
        String pdfPath = pdfGenerationService.generatePdf(seller, sellerGstin, sellerAddress, buyer, buyerGstin, buyerAddress, items);

        // Assert: Verify the PDF is generated and exists
        File pdfFile = new File(pdfPath);
        assertTrue(pdfFile.exists(), "Generated PDF file should exist even with empty items");
        assertTrue(pdfFile.length() > 0, "Generated PDF file should not be empty");

        // Cleanup: Delete the generated file
        pdfFile.delete();
    }

    @Test
    void testGeneratePdf_ExceptionHandling() {
        // Arrange: Define invalid file path to simulate IOException
        String invalidPath = "/invalid/directory/generated_invoice.pdf";

        // Modify the PDFGenerationService to accept file path
        pdfGenerationService = new PDFGenerationService() {
            @Override
            public String generatePdf(String seller, String sellerGstin, String sellerAddress,
                                      String buyer, String buyerGstin, String buyerAddress,
                                      List<Item> items) throws IOException {
                throw new IOException("Invalid directory");
            }
        };

        // Act and Assert: Verify exception is thrown
        assertThrows(IOException.class, () -> {
            pdfGenerationService.generatePdf("Seller", "GSTIN", "Address", "Buyer", "GSTIN", "Address", Arrays.asList());
        });
    }
}
