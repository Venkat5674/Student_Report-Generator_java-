package util;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import model.AttendanceRecord.AttendanceStatus;
import model.Student;
import model.Subject;

/**
 * Utility class for exporting student reports to text files
 */
public class TextExporter {
    
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    
    // Export student report to a text file
    public static void exportToText(Student student, String filePath) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            // Header
            String border = "=".repeat(60);
            writer.println(border);
            writer.println("\t\tSTUDENT REPORT CARD");
            writer.println(border);
            writer.println();
            
            // Student Information
            writer.println("Student Information:");
            String line = "-".repeat(60);
            writer.println(line);
            writer.println("ID: " + student.getId());
            writer.println("Name: " + student.getName());
            writer.println("Age: " + student.getAge());
            writer.println("Grade/Class: " + student.getGrade());
            writer.println();
            
            // Academic Performance
            writer.println("Academic Performance:");
            writer.println(line);
            writer.println("Subject\t\tMark\t\tGrade");
            writer.println(line);
            
            for (Subject subject : student.getSubjectList()) {
                writer.printf("%-15s\t%.2f\t\t%s%n", 
                        subject.getName(), subject.getMark(), subject.getGrade());
            }
            
            writer.println(line);
            writer.printf("Overall Average: %.2f%n", student.calculateOverallAverage());
            writer.printf("Overall Grade: %s%n", student.calculateOverallGrade());
            writer.println();
            
            // Attendance Information
            writer.println("Attendance Information:");
            writer.println(line);
            writer.printf("Present: %d days%n", student.getAttendanceRecord().countPresent());
            writer.printf("Absent: %d days%n", student.getAttendanceRecord().countAbsent());
            writer.printf("Late: %d days%n", student.getAttendanceRecord().countLate());
            writer.printf("Attendance Percentage: %.2f%%%n", 
                    student.getAttendanceRecord().calculateAttendancePercentage());
            writer.println();
            
            // Footer
            writer.println(border);
            writer.println("Report Generated: " + DATE_FORMAT.format(new Date()));
            writer.println(border);
        }
    }
}
