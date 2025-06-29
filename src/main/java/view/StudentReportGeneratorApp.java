package view;

import controller.StudentController;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import model.AttendanceRecord.AttendanceStatus;
import model.Student;
import model.Subject;
import util.PdfExporter;
import util.TextExporter;

/**
 * Main application class for the Student Report Generator
 */
public class StudentReportGeneratorApp extends JFrame {
    
    private static final long serialVersionUID = 1L;
    
    // Controller
    private StudentController controller;
    
    // UI Components
    private JList<Student> studentList;
    private DefaultListModel<Student> listModel;
    private JButton addButton;
    private JButton editButton;
    private JButton deleteButton;
    private JButton generateReportButton;
    
    // Date formatter
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    
    public StudentReportGeneratorApp() {
        // Initialize controller
        controller = new StudentController();
        
        // Set up the frame
        setTitle("Student Report Generator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        
        // Create and set up the UI
        setupUI();
        
        // Display the window
        setVisible(true);
    }
    
    private void setupUI() {
        // Create main panel with border layout
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // Create left panel for student list
        JPanel leftPanel = new JPanel(new BorderLayout(5, 5));
        leftPanel.setBorder(BorderFactory.createTitledBorder("Students"));
        
        // Create list model and JList
        listModel = new DefaultListModel<>();
        studentList = new JList<>(listModel);
        studentList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        studentList.addListSelectionListener(e -> updateButtonStates());
        
        // Add all students to the list model
        refreshStudentList();
        
        // Add list to a scroll pane
        JScrollPane scrollPane = new JScrollPane(studentList);
        leftPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Create button panel
        JPanel buttonPanel = new JPanel(new GridLayout(1, 4, 5, 0));
        
        // Create buttons
        addButton = new JButton("Add");
        editButton = new JButton("Edit");
        deleteButton = new JButton("Delete");
        generateReportButton = new JButton("Generate Report");
        
        // Add action listeners
        addButton.addActionListener(e -> showAddStudentDialog());
        editButton.addActionListener(e -> showEditStudentDialog());
        deleteButton.addActionListener(e -> deleteSelectedStudent());
        generateReportButton.addActionListener(e -> showGenerateReportDialog());
        
        // Add buttons to panel
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(generateReportButton);
        
        // Update button states
        updateButtonStates();
        
        // Add button panel to main panel
        mainPanel.add(leftPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Set content pane
        setContentPane(mainPanel);
    }
    
    // Enable/disable buttons based on selection
    private void updateButtonStates() {
        boolean isStudentSelected = !studentList.isSelectionEmpty();
        editButton.setEnabled(isStudentSelected);
        deleteButton.setEnabled(isStudentSelected);
        generateReportButton.setEnabled(isStudentSelected);
    }
    
    // Refresh the student list
    private void refreshStudentList() {
        listModel.clear();
        for (Student student : controller.getAllStudents()) {
            listModel.addElement(student);
        }
    }
    
    // Show dialog to add a new student
    private void showAddStudentDialog() {
        // Create dialog
        JDialog dialog = new JDialog(this, "Add New Student", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout(10, 10));
        
        // Create form panel
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        formPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // Add form fields
        JTextField idField = new JTextField();
        JTextField nameField = new JTextField();
        JTextField ageField = new JTextField();
        JTextField gradeField = new JTextField();
        
        formPanel.add(new JLabel("ID:"));
        formPanel.add(idField);
        formPanel.add(new JLabel("Name:"));
        formPanel.add(nameField);
        formPanel.add(new JLabel("Age:"));
        formPanel.add(ageField);
        formPanel.add(new JLabel("Grade/Class:"));
        formPanel.add(gradeField);
        
        // Create button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");
        
        // Add action listeners
        saveButton.addActionListener(e -> {
            // Validate input
            if (idField.getText().trim().isEmpty() || 
                    nameField.getText().trim().isEmpty() ||
                    ageField.getText().trim().isEmpty() ||
                    gradeField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(dialog, 
                        "All fields are required!", 
                        "Validation Error", 
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            try {
                int age = Integer.parseInt(ageField.getText().trim());
                if (age <= 0) {
                    throw new NumberFormatException("Age must be positive");
                }
                
                // Create new student
                Student student = new Student(
                        idField.getText().trim(),
                        nameField.getText().trim(),
                        age,
                        gradeField.getText().trim());
                
                // Add student to controller
                controller.addStudent(student);
                
                // Refresh list and close dialog
                refreshStudentList();
                dialog.dispose();
                
                // Show add subjects dialog
                showAddSubjectsDialog(student);
                
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, 
                        "Age must be a positive number!", 
                        "Validation Error", 
                        JOptionPane.ERROR_MESSAGE);
            }
        });
        
        cancelButton.addActionListener(e -> dialog.dispose());
        
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        
        // Add panels to dialog
        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        
        // Show dialog
        dialog.setVisible(true);
    }
    
    // Show dialog to edit a student
    private void showEditStudentDialog() {
        // Get selected student
        Student student = studentList.getSelectedValue();
        if (student == null) {
            return;
        }
        
        // Create dialog
        JDialog dialog = new JDialog(this, "Edit Student", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout(10, 10));
        
        // Create form panel
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        formPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // Add form fields
        JTextField idField = new JTextField(student.getId());
        idField.setEditable(false); // ID cannot be changed
        JTextField nameField = new JTextField(student.getName());
        JTextField ageField = new JTextField(String.valueOf(student.getAge()));
        JTextField gradeField = new JTextField(student.getGrade());
        
        formPanel.add(new JLabel("ID:"));
        formPanel.add(idField);
        formPanel.add(new JLabel("Name:"));
        formPanel.add(nameField);
        formPanel.add(new JLabel("Age:"));
        formPanel.add(ageField);
        formPanel.add(new JLabel("Grade/Class:"));
        formPanel.add(gradeField);
        
        // Create button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveButton = new JButton("Save");
        JButton editSubjectsButton = new JButton("Edit Subjects");
        JButton editAttendanceButton = new JButton("Edit Attendance");
        JButton cancelButton = new JButton("Cancel");
        
        // Add action listeners
        saveButton.addActionListener(e -> {
            // Validate input
            if (nameField.getText().trim().isEmpty() ||
                    ageField.getText().trim().isEmpty() ||
                    gradeField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(dialog, 
                        "All fields are required!", 
                        "Validation Error", 
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            try {
                int age = Integer.parseInt(ageField.getText().trim());
                if (age <= 0) {
                    throw new NumberFormatException("Age must be positive");
                }
                
                // Update student
                student.setName(nameField.getText().trim());
                student.setAge(age);
                student.setGrade(gradeField.getText().trim());
                
                // Update in controller
                controller.updateStudent(student);
                
                // Refresh list and close dialog
                refreshStudentList();
                dialog.dispose();
                
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, 
                        "Age must be a positive number!", 
                        "Validation Error", 
                        JOptionPane.ERROR_MESSAGE);
            }
        });
        
        editSubjectsButton.addActionListener(e -> {
            dialog.dispose();
            showAddSubjectsDialog(student);
        });
        
        editAttendanceButton.addActionListener(e -> {
            dialog.dispose();
            showAttendanceDialog(student);
        });
        
        cancelButton.addActionListener(e -> dialog.dispose());
        
        buttonPanel.add(saveButton);
        buttonPanel.add(editSubjectsButton);
        buttonPanel.add(editAttendanceButton);
        buttonPanel.add(cancelButton);
        
        // Add panels to dialog
        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        
        // Show dialog
        dialog.setVisible(true);
    }
    
    // Show dialog to add/edit subjects for a student
    private void showAddSubjectsDialog(Student student) {
        // Create dialog
        JDialog dialog = new JDialog(this, "Manage Subjects", true);
        dialog.setSize(500, 400);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout(10, 10));
        
        // Create subject list panel
        JPanel subjectListPanel = new JPanel(new BorderLayout(5, 5));
        subjectListPanel.setBorder(BorderFactory.createTitledBorder("Current Subjects"));
        
        // Create subject list model and JList
        DefaultListModel<Subject> subjectListModel = new DefaultListModel<>();
        JList<Subject> subjectJList = new JList<>(subjectListModel);
        
        // Add existing subjects to the list
        for (Subject subject : student.getSubjectList()) {
            subjectListModel.addElement(subject);
        }
        
        // Add list to scroll pane
        JScrollPane scrollPane = new JScrollPane(subjectJList);
        subjectListPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Create form panel for adding new subjects
        JPanel formPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createTitledBorder("Add Subject"));
        
        JTextField subjectNameField = new JTextField();
        JTextField subjectMarkField = new JTextField();
        
        formPanel.add(new JLabel("Subject Name:"));
        formPanel.add(subjectNameField);
        formPanel.add(new JLabel("Mark:"));
        formPanel.add(subjectMarkField);
        
        // Create button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton addSubjectButton = new JButton("Add Subject");
        JButton removeSubjectButton = new JButton("Remove Subject");
        JButton doneButton = new JButton("Done");
        
        // Add action listeners
        addSubjectButton.addActionListener(e -> {
            // Validate input
            if (subjectNameField.getText().trim().isEmpty() ||
                    subjectMarkField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(dialog, 
                        "Subject name and mark are required!", 
                        "Validation Error", 
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            try {
                double mark = Double.parseDouble(subjectMarkField.getText().trim());
                if (mark < 0 || mark > 100) {
                    throw new NumberFormatException("Mark must be between 0 and 100");
                }
                
                // Create and add new subject
                Subject subject = new Subject(
                        subjectNameField.getText().trim(),
                        mark);
                
                student.addSubject(subject);
                subjectListModel.addElement(subject);
                
                // Update in controller
                controller.updateStudent(student);
                
                // Clear fields
                subjectNameField.setText("");
                subjectMarkField.setText("");
                
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, 
                        "Mark must be a number between 0 and 100!", 
                        "Validation Error", 
                        JOptionPane.ERROR_MESSAGE);
            }
        });
        
        removeSubjectButton.addActionListener(e -> {
            // Get selected subject
            Subject selectedSubject = subjectJList.getSelectedValue();
            if (selectedSubject == null) {
                JOptionPane.showMessageDialog(dialog, 
                        "Please select a subject to remove!", 
                        "Selection Required", 
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Remove subject
            student.getSubjects().remove(selectedSubject.getName());
            subjectListModel.removeElement(selectedSubject);
            
            // Update in controller
            controller.updateStudent(student);
        });
        
        doneButton.addActionListener(e -> dialog.dispose());
        
        buttonPanel.add(addSubjectButton);
        buttonPanel.add(removeSubjectButton);
        buttonPanel.add(doneButton);
        
        // Add panels to dialog
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        mainPanel.add(subjectListPanel, BorderLayout.CENTER);
        mainPanel.add(formPanel, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        dialog.add(mainPanel);
        
        // Show dialog
        dialog.setVisible(true);
    }
    
    // Show dialog to manage attendance
    private void showAttendanceDialog(Student student) {
        // Create dialog
        JDialog dialog = new JDialog(this, "Manage Attendance", true);
        dialog.setSize(500, 400);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout(10, 10));
        
        // Create form panel
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        formPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // Add form fields
        JTextField dateField = new JTextField();
        JComboBox<String> statusComboBox = new JComboBox<>(
                new String[]{"Present", "Absent", "Late", "Excused"});
        
        formPanel.add(new JLabel("Date (yyyy-MM-dd):"));
        formPanel.add(dateField);
        formPanel.add(new JLabel("Status:"));
        formPanel.add(statusComboBox);
        
        // Create button for adding attendance
        JButton addAttendanceButton = new JButton("Add Attendance Record");
        formPanel.add(addAttendanceButton);
        
        // Create attendance summary panel
        JPanel summaryPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        summaryPanel.setBorder(BorderFactory.createTitledBorder("Attendance Summary"));
        
        JLabel presentLabel = new JLabel("Present: " + student.getAttendanceRecord().countPresent() + " days");
        JLabel absentLabel = new JLabel("Absent: " + student.getAttendanceRecord().countAbsent() + " days");
        JLabel lateLabel = new JLabel("Late: " + student.getAttendanceRecord().countLate() + " days");
        JLabel percentageLabel = new JLabel(String.format("Attendance Percentage: %.2f%%", 
                student.getAttendanceRecord().calculateAttendancePercentage()));
        
        summaryPanel.add(presentLabel);
        summaryPanel.add(absentLabel);
        summaryPanel.add(lateLabel);
        summaryPanel.add(percentageLabel);
        
        // Add action listener for add button
        addAttendanceButton.addActionListener(e -> {
            try {
                // Parse date
                Date date = DATE_FORMAT.parse(dateField.getText().trim());
                
                // Get selected status
                AttendanceStatus status;
                switch (statusComboBox.getSelectedIndex()) {
                    case 0:
                        status = AttendanceStatus.PRESENT;
                        break;
                    case 1:
                        status = AttendanceStatus.ABSENT;
                        break;
                    case 2:
                        status = AttendanceStatus.LATE;
                        break;
                    case 3:
                        status = AttendanceStatus.EXCUSED;
                        break;
                    default:
                        status = AttendanceStatus.UNKNOWN;
                }
                
                // Add attendance record
                student.getAttendanceRecord().recordAttendance(date, status);
                
                // Update in controller
                controller.updateStudent(student);
                
                // Update summary labels
                presentLabel.setText("Present: " + student.getAttendanceRecord().countPresent() + " days");
                absentLabel.setText("Absent: " + student.getAttendanceRecord().countAbsent() + " days");
                lateLabel.setText("Late: " + student.getAttendanceRecord().countLate() + " days");
                percentageLabel.setText(String.format("Attendance Percentage: %.2f%%", 
                        student.getAttendanceRecord().calculateAttendancePercentage()));
                
                // Clear date field
                dateField.setText("");
                
            } catch (ParseException ex) {
                JOptionPane.showMessageDialog(dialog, 
                        "Please enter date in yyyy-MM-dd format!", 
                        "Date Format Error", 
                        JOptionPane.ERROR_MESSAGE);
            }
        });
        
        // Create button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton doneButton = new JButton("Done");
        doneButton.addActionListener(e -> dialog.dispose());
        buttonPanel.add(doneButton);
        
        // Add panels to dialog
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.add(formPanel, BorderLayout.NORTH);
        mainPanel.add(summaryPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        dialog.add(mainPanel);
        
        // Show dialog
        dialog.setVisible(true);
    }
    
    // Delete the selected student
    private void deleteSelectedStudent() {
        Student student = studentList.getSelectedValue();
        if (student == null) {
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, 
                "Are you sure you want to delete student: " + student.getName() + "?", 
                "Confirm Delete", 
                JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            controller.removeStudent(student);
            refreshStudentList();
        }
    }
    
    // Show dialog to generate and export a report
    private void showGenerateReportDialog() {
        Student student = studentList.getSelectedValue();
        if (student == null) {
            return;
        }
        
        // Create dialog
        JDialog dialog = new JDialog(this, "Generate Report", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout(10, 10));
        
        // Create report preview
        JTextArea previewArea = new JTextArea();
        previewArea.setEditable(false);
        previewArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        
        // Add basic report preview
        previewArea.append("STUDENT REPORT CARD\n\n");
        previewArea.append("Student: " + student.getName() + " (ID: " + student.getId() + ")\n");
        previewArea.append("Grade/Class: " + student.getGrade() + "\n\n");
        previewArea.append("Subjects:\n");
        
        for (Subject subject : student.getSubjectList()) {
            previewArea.append("  " + subject.getName() + ": " + 
                    String.format("%.2f", subject.getMark()) + 
                    " (" + subject.getGrade() + ")\n");
        }
        
        previewArea.append("\nOverall Average: " + 
                String.format("%.2f", student.calculateOverallAverage()) + 
                " (" + student.calculateOverallGrade() + ")\n\n");
        
        previewArea.append("Attendance: " + 
                String.format("%.2f%%", student.getAttendanceRecord().calculateAttendancePercentage()));
        
        // Add preview to scroll pane
        JScrollPane scrollPane = new JScrollPane(previewArea);
        
        // Create export options
        JPanel exportPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        exportPanel.setBorder(BorderFactory.createTitledBorder("Export Options"));
        
        JRadioButton textOption = new JRadioButton("Text File (.txt)");
        JRadioButton pdfOption = new JRadioButton("PDF File (.pdf)");
        
        ButtonGroup exportGroup = new ButtonGroup();
        exportGroup.add(textOption);
        exportGroup.add(pdfOption);
        textOption.setSelected(true);
        
        exportPanel.add(textOption);
        exportPanel.add(pdfOption);
        
        // Create button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton exportButton = new JButton("Export");
        JButton cancelButton = new JButton("Cancel");
        
        // Add action listeners
        exportButton.addActionListener(e -> {
            // Create file chooser
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Save Report");
            
            // Set file filter based on selected export option
            if (textOption.isSelected()) {
                fileChooser.setFileFilter(new FileNameExtensionFilter("Text Files", "txt"));
                fileChooser.setSelectedFile(new File(student.getName().replace(" ", "_") + "_Report.txt"));
            } else {
                fileChooser.setFileFilter(new FileNameExtensionFilter("PDF Files", "pdf"));
                fileChooser.setSelectedFile(new File(student.getName().replace(" ", "_") + "_Report.pdf"));
            }
            
            // Show save dialog
            int result = fileChooser.showSaveDialog(dialog);
            
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                
                try {
                    // Export based on selected option
                    if (textOption.isSelected()) {
                        TextExporter.exportToText(student, selectedFile.getAbsolutePath());
                    } else {
                        PdfExporter.exportToPdf(student, selectedFile.getAbsolutePath());
                    }
                    
                    // Close the dialog (the PDF exporter will show its own success message)
                    if (textOption.isSelected()) {
                        JOptionPane.showMessageDialog(dialog, 
                                "Report successfully exported to:\n" + selectedFile.getAbsolutePath(), 
                                "Export Successful", 
                                JOptionPane.INFORMATION_MESSAGE);
                    }
                    
                    dialog.dispose();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(dialog, 
                            "Error exporting report: " + ex.getMessage(), 
                            "Export Error", 
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        cancelButton.addActionListener(e -> dialog.dispose());
        
        buttonPanel.add(exportButton);
        buttonPanel.add(cancelButton);
        
        // Add panels to dialog
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(exportPanel, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        dialog.add(mainPanel);
        
        // Show dialog
        dialog.setVisible(true);
    }
    
    // Main method
    public static void main(String[] args) {
        // Set look and feel to system
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Launch application
        SwingUtilities.invokeLater(() -> new StudentReportGeneratorApp());
    }
}
