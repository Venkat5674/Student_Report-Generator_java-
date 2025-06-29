package util;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.File;

import javax.swing.JOptionPane;

import model.Student;
import model.Subject;
import model.AttendanceRecord;

// iText imports
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.pdf.draw.LineSeparator;

/**
 * Utility class for exporting student reports to PDF files using iText library
 */
public class PdfExporter {
    
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    
    // Fonts
    private static final Font TITLE_FONT = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, BaseColor.DARK_GRAY);
    private static final Font HEADER_FONT = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.DARK_GRAY);
    private static final Font NORMAL_FONT = FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.BLACK);
    private static final Font BOLD_FONT = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, BaseColor.BLACK);
    private static final Font FOOTER_FONT = FontFactory.getFont(FontFactory.HELVETICA_ITALIC, 8, BaseColor.GRAY);
    
    /**
     * Export student report to a PDF file using iText
     * 
     * @param student Student object containing report data
     * @param filePath Path where the PDF file will be saved
     * @throws IOException If there's an error writing to the file
     */
    public static void exportToPdf(Student student, String filePath) throws IOException {
        // Try to create PDF with iText
        try {
            File jarFile = new File("lib/itextpdf-5.5.13.3.jar");
            
            if (!jarFile.exists()) {
                // If iText library is not found, show message and fall back to text export
                int response = JOptionPane.showConfirmDialog(null,
                        "The iText library was not found in the lib directory.\n" +
                        "Would you like to download it now?\n\n" +
                        "If you select 'No', a simple text-based PDF will be created instead.",
                        "iText Library Not Found",
                        JOptionPane.YES_NO_OPTION);
                
                if (response == JOptionPane.YES_OPTION) {
                    // Open the download page
                    if (java.awt.Desktop.isDesktopSupported()) {
                        java.awt.Desktop.getDesktop().browse(
                            new java.net.URI("https://github.com/itext/itextpdf/releases"));
                    }
                    JOptionPane.showMessageDialog(null,
                            "After downloading, place the JAR file in the 'lib' directory\n" +
                            "and rename it to 'itextpdf-5.5.13.3.jar' or update the code to match your version.",
                            "Download Instructions",
                            JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                
                // Fall back to text-based export
                exportToTextBasedPdf(student, filePath);
                return;
            }
            
            // Create PDF document with iText
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();
            
            // Add title
            Paragraph title = new Paragraph("STUDENT REPORT CARD", TITLE_FONT);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph(" ")); // Space
            
            // Add student information section
            document.add(new Paragraph("Student Information:", HEADER_FONT));
            document.add(new LineSeparator());
            document.add(new Paragraph("ID: " + student.getId(), NORMAL_FONT));
            document.add(new Paragraph("Name: " + student.getName(), NORMAL_FONT));
            document.add(new Paragraph("Age: " + student.getAge(), NORMAL_FONT));
            document.add(new Paragraph("Grade/Class: " + student.getGrade(), NORMAL_FONT));
            document.add(new Paragraph(" ")); // Space
            
            // Add academic performance section
            document.add(new Paragraph("Academic Performance:", HEADER_FONT));
            document.add(new LineSeparator());
            
            // Create a table for subjects
            PdfPTable table = new PdfPTable(3); // 3 columns
            table.setWidthPercentage(100);
            
            // Set table headers
            PdfPCell cell1 = new PdfPCell(new Phrase("Subject", BOLD_FONT));
            PdfPCell cell2 = new PdfPCell(new Phrase("Mark", BOLD_FONT));
            PdfPCell cell3 = new PdfPCell(new Phrase("Grade", BOLD_FONT));
            
            cell1.setBackgroundColor(BaseColor.LIGHT_GRAY);
            cell2.setBackgroundColor(BaseColor.LIGHT_GRAY);
            cell3.setBackgroundColor(BaseColor.LIGHT_GRAY);
            
            table.addCell(cell1);
            table.addCell(cell2);
            table.addCell(cell3);
            
            // Add subject data to the table
            for (Subject subject : student.getSubjectList()) {
                table.addCell(new Phrase(subject.getName(), NORMAL_FONT));
                table.addCell(new Phrase(String.format("%.2f", subject.getMark()), NORMAL_FONT));
                table.addCell(new Phrase(subject.getGrade(), NORMAL_FONT));
            }
            
            document.add(table);
            document.add(new Paragraph(" ")); // Space
            
            // Add overall performance
            document.add(new Paragraph("Overall Average: " + 
                    String.format("%.2f", student.calculateOverallAverage()), BOLD_FONT));
            document.add(new Paragraph("Overall Grade: " + 
                    student.calculateOverallGrade(), BOLD_FONT));
            document.add(new Paragraph(" ")); // Space
            
            // Add attendance information
            document.add(new Paragraph("Attendance Information:", HEADER_FONT));
            document.add(new LineSeparator());
            document.add(new Paragraph("Present: " + 
                    student.getAttendanceRecord().countPresent() + " days", NORMAL_FONT));
            document.add(new Paragraph("Absent: " + 
                    student.getAttendanceRecord().countAbsent() + " days", NORMAL_FONT));
            document.add(new Paragraph("Late: " + 
                    student.getAttendanceRecord().countLate() + " days", NORMAL_FONT));
            document.add(new Paragraph("Attendance Percentage: " + 
                    String.format("%.2f%%", student.getAttendanceRecord().calculateAttendancePercentage()), NORMAL_FONT));
            document.add(new Paragraph(" ")); // Space
            
            // Add footer
            document.add(new LineSeparator());
            document.add(new Paragraph("Report Generated: " + DATE_FORMAT.format(new Date()), FOOTER_FONT));
            
            document.close();
        } catch (DocumentException e) {
            // If there's an error with iText, fall back to text-based export
            JOptionPane.showMessageDialog(null, 
                    "Error creating PDF with iText: " + e.getMessage() + "\nFalling back to text-based export.",
                    "PDF Generation Error",
                    JOptionPane.WARNING_MESSAGE);
            
            exportToTextBasedPdf(student, filePath);
        } catch (NoClassDefFoundError e) {
            // If iText classes are not found in classpath, fall back to text-based export
            JOptionPane.showMessageDialog(null,
                    "The iText library is not properly loaded.\nFalling back to text-based export.",
                    "Library Not Loaded",
                    JOptionPane.WARNING_MESSAGE);
            
            exportToTextBasedPdf(student, filePath);
        } catch (Exception e) {
            // Handle any other exceptions
            JOptionPane.showMessageDialog(null, 
                    "Error: " + e.getMessage() + "\nFalling back to text-based export.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            
            exportToTextBasedPdf(student, filePath);
        }
        
        // Offer to open the file after export
        int response = JOptionPane.showConfirmDialog(null,
                "Report exported successfully to:\n" + filePath + "\n\nWould you like to open it now?",
                "Export Complete", 
                JOptionPane.YES_NO_OPTION);
        
        if (response == JOptionPane.YES_OPTION) {
            try {
                java.awt.Desktop.getDesktop().open(new java.io.File(filePath));
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null,
                        "Could not open the file. Please open it manually.",
                        "Error Opening File",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    /**
     * Fall back method to export as a formatted text file with .pdf extension
     * Used when iText is not available
     */
    private static void exportToTextBasedPdf(Student student, String filePath) throws FileNotFoundException {
        // Show notification about fallback mode
        JOptionPane.showMessageDialog(null,
                "Using fallback mode: Creating a text-based PDF.\n\n" +
                "For proper PDF functionality, please install the iText library.",
                "Fallback Mode",
                JOptionPane.INFORMATION_MESSAGE);
        
        // Create a well-formatted text file with .pdf extension
        try (java.io.PrintWriter writer = new java.io.PrintWriter(new FileOutputStream(filePath))) {
            // Header
            writer.println("======================== STUDENT REPORT CARD ========================");
            writer.println();
            
            // Student Information
            writer.println("Student Information:");
            writer.println("------------------------------------------------------------");
            writer.println("ID: " + student.getId());
            writer.println("Name: " + student.getName());
            writer.println("Age: " + student.getAge());
            writer.println("Grade/Class: " + student.getGrade());
            writer.println();
            
            // Academic Performance
            writer.println("Academic Performance:");
            writer.println("------------------------------------------------------------");
            writer.println(String.format("%-20s %-10s %-10s", "Subject", "Mark", "Grade"));
            writer.println("------------------------------------------------------------");
            
            for (Subject subject : student.getSubjectList()) {
                writer.println(String.format("%-20s %-10.2f %-10s", 
                        subject.getName(), subject.getMark(), subject.getGrade()));
            }
            
            writer.println("------------------------------------------------------------");
            writer.printf("Overall Average: %.2f%n", student.calculateOverallAverage());
            writer.printf("Overall Grade: %s%n", student.calculateOverallGrade());
            writer.println();
            
            // Attendance Information
            writer.println("Attendance Information:");
            writer.println("------------------------------------------------------------");
            writer.printf("Present: %d days%n", student.getAttendanceRecord().countPresent());
            writer.printf("Absent: %d days%n", student.getAttendanceRecord().countAbsent());
            writer.printf("Late: %d days%n", student.getAttendanceRecord().countLate());
            writer.printf("Attendance Percentage: %.2f%%%n", 
                    student.getAttendanceRecord().calculateAttendancePercentage());
            writer.println();
            
            // Footer
            writer.println("======================== END OF REPORT ========================");
            writer.println("Report Generated: " + DATE_FORMAT.format(new Date()));
            
            // Add installation instructions
            writer.println();
            writer.println("NOTE: For proper PDF functionality, install the iText library:");
            writer.println("1. Download iText JAR from https://github.com/itext/itextpdf/releases");
            writer.println("2. Place the JAR file in the lib directory of this application");
        }
    }
}
