package controller;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import model.Student;

/**
 * Controller class for managing student data
 */
public class StudentController {
    
    private List<Student> students;
    private static final String DATA_FILE = "students.ser";
    
    public StudentController() {
        this.students = new ArrayList<>();
        loadStudents();
    }
    
    // Add a new student
    public void addStudent(Student student) {
        students.add(student);
        saveStudents();
    }
    
    // Update an existing student
    public void updateStudent(Student student) {
        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).getId().equals(student.getId())) {
                students.set(i, student);
                break;
            }
        }
        saveStudents();
    }
    
    // Remove a student
    public void removeStudent(Student student) {
        students.removeIf(s -> s.getId().equals(student.getId()));
        saveStudents();
    }
    
    // Get a student by ID
    public Student getStudentById(String id) {
        for (Student student : students) {
            if (student.getId().equals(id)) {
                return student;
            }
        }
        return null;
    }
    
    // Get all students
    public List<Student> getAllStudents() {
        return new ArrayList<>(students);
    }
    
    // Save students to file
    private void saveStudents() {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(DATA_FILE))) {
            oos.writeObject(students);
        } catch (IOException e) {
            System.err.println("Error saving students: " + e.getMessage());
        }
    }
    
    // Load students from file
    @SuppressWarnings("unchecked")
    private void loadStudents() {
        File file = new File(DATA_FILE);
        if (!file.exists()) {
            students = new ArrayList<>();
            return;
        }
        
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(DATA_FILE))) {
            students = (List<Student>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading students: " + e.getMessage());
            students = new ArrayList<>();
        }
    }
}
