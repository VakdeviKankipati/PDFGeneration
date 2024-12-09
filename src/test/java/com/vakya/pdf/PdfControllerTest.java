package com.vakya.pdf;

import com.lowagie.text.DocumentException;
import com.vakya.pdf.controllers.PDFController;
import com.vakya.pdf.dto.InvoiceRequest;
import com.vakya.pdf.models.Item;
import com.vakya.pdf.service.PDFGenerationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@SpringJUnitConfig
public class PdfControllerTest {

    @InjectMocks
    private PDFController pdfController;

    @Mock
    private PDFGenerationService pdfGenerationService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGeneratePdf_Success() throws DocumentException, IOException {
        // Prepare test data
        InvoiceRequest invoiceRequest = new InvoiceRequest();
        invoiceRequest.setSellerName("XYZ Pvt. Ltd.");
        invoiceRequest.setSellerGstin("29AABBCCDD121ZD");
        invoiceRequest.setSellerAddress("New Delhi, India");
        invoiceRequest.setBuyerName("Vedant Computers");
        invoiceRequest.setBuyerGstin("29AABBCCDD131ZD");
        invoiceRequest.setBuyerAddress("New Delhi, India");

        // Mock Item list
        List<Item> items = Arrays.asList(
                new Item("Product 1", 12, 123.00, 1476.00),
                new Item("Product 2", 8, 50.00, 400.00)
        );
        invoiceRequest.setItems(items);

        // Define PDF generation path
        String generatedPdfPath = "D:/Project/pdf/generated_invoice.pdf";
        when(pdfGenerationService.generatePdf(
                invoiceRequest.getSellerName(),
                invoiceRequest.getSellerGstin(),
                invoiceRequest.getSellerAddress(),
                invoiceRequest.getBuyerName(),
                invoiceRequest.getBuyerGstin(),
                invoiceRequest.getBuyerAddress(),
                invoiceRequest.getItems()
        )).thenReturn(generatedPdfPath);

        // Execute the test
        String response = pdfController.generatePdf(invoiceRequest);

        // Validate the response
        assertTrue(new File(generatedPdfPath).exists(), "Generated PDF file should exist");
        assertTrue(response.contains("PDF generated successfully"), "Response should confirm successful generation");
    }
}
