package lk.ijse.gdse.backend.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import lk.ijse.gdse.backend.entity.Booking;
import lk.ijse.gdse.backend.entity.Payment;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;

@Component
public class InvoiceGenerator {

    public byte[] generateInvoicePdf(Booking booking, Payment payment) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            Document document = new Document();
            PdfWriter.getInstance(document, baos);
            document.open();

            // Title
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20);
            Paragraph title = new Paragraph("MarineX Invoice", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            document.add(new Paragraph(" ")); // empty line

            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

            // Booking & Payment info
            PdfPTable infoTable = new PdfPTable(2);
            infoTable.setWidthPercentage(100);

            infoTable.addCell(createCell("Invoice No:", PdfPCell.ALIGN_LEFT, true));
            infoTable.addCell(createCell("INV-" + (payment.getId()==null?"TEMP":payment.getId()) + "-" + (booking.getBookingId()==null?"X":booking.getBookingId()), PdfPCell.ALIGN_LEFT, false));

            infoTable.addCell(createCell("Booking ID:", PdfPCell.ALIGN_LEFT, true));
            infoTable.addCell(createCell(String.valueOf(booking.getBookingId()), PdfPCell.ALIGN_LEFT, false));

            infoTable.addCell(createCell("Agent ID:", PdfPCell.ALIGN_LEFT, true));
            infoTable.addCell(createCell(booking.getAgent()!=null ? String.valueOf(booking.getAgent().getUserId()) : "N/A", PdfPCell.ALIGN_LEFT, false));

            infoTable.addCell(createCell("Vessel:", PdfPCell.ALIGN_LEFT, true));
            infoTable.addCell(createCell(booking.getVessel()!=null ? booking.getVessel().getName() : "N/A", PdfPCell.ALIGN_LEFT, false));

            infoTable.addCell(createCell("Berth:", PdfPCell.ALIGN_LEFT, true));
            infoTable.addCell(createCell(booking.getBerth()!=null ? booking.getBerth().getBerthNumber() : "N/A", PdfPCell.ALIGN_LEFT, false));

            infoTable.addCell(createCell("Purpose:", PdfPCell.ALIGN_LEFT, true));
            infoTable.addCell(createCell(booking.getPurpose()!=null ? booking.getPurpose().name() : "", PdfPCell.ALIGN_LEFT, false));

            infoTable.addCell(createCell("Amount Paid:", PdfPCell.ALIGN_LEFT, true));
            infoTable.addCell(createCell("$" + String.format("%.2f", payment.getAmount()), PdfPCell.ALIGN_LEFT, false));

            infoTable.addCell(createCell("Payment Method:", PdfPCell.ALIGN_LEFT, true));
            infoTable.addCell(createCell(payment.getMethod(), PdfPCell.ALIGN_LEFT, false));

            infoTable.addCell(createCell("Transaction ID:", PdfPCell.ALIGN_LEFT, true));
            infoTable.addCell(createCell(payment.getTransactionId()!=null ? payment.getTransactionId() : "N/A", PdfPCell.ALIGN_LEFT, false));

            infoTable.addCell(createCell("Payment Date:", PdfPCell.ALIGN_LEFT, true));
            infoTable.addCell(createCell(payment.getPaymentDate()!=null ? dtf.format(payment.getPaymentDate()) : "", PdfPCell.ALIGN_LEFT, false));

            document.add(infoTable);

            document.add(new Paragraph(" ")); // empty line

            // Services Table
            if (booking.getBookingServices()!=null && !booking.getBookingServices().isEmpty()) {
                document.add(new Paragraph("Services:", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14)));
                PdfPTable servicesTable = new PdfPTable(2);
                servicesTable.setWidthPercentage(100);
                servicesTable.addCell(createCell("Service Name", PdfPCell.ALIGN_CENTER, true));
                servicesTable.addCell(createCell("Price", PdfPCell.ALIGN_CENTER, true));

                booking.getBookingServices().forEach(bs -> {
                    servicesTable.addCell(createCell(bs.getService()!=null ? bs.getService().getName() : "Service", PdfPCell.ALIGN_LEFT, false));
                    servicesTable.addCell(createCell("$" + (bs.getService()!=null ? String.format("%.2f", bs.getService().getPrice()) : "0.0"), PdfPCell.ALIGN_RIGHT, false));
                });

                // Total row
                servicesTable.addCell(createCell("Total", PdfPCell.ALIGN_LEFT, true));
                servicesTable.addCell(createCell("$" + String.format("%.2f", booking.getTotalPrice()==null ? 0.0 : booking.getTotalPrice()), PdfPCell.ALIGN_RIGHT, true));

                document.add(servicesTable);
            }

            document.close();
            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate invoice PDF", e);
        }
    }

    private PdfPCell createCell(String text, int alignment, boolean bold) {
        Font font = bold ? FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12) : FontFactory.getFont(FontFactory.HELVETICA, 12);
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setHorizontalAlignment(alignment);
        cell.setBorder(PdfPCell.NO_BORDER);
        return cell;
    }
}
