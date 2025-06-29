package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a student in the Student Report Generator system
 */
public class Student implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private String id;
    private String name;
    private int age;
    private String grade; // Class/Grade level
    private Map<String, Subject> subjects;
    private AttendanceRecord attendanceRecord;
    
    public Student(String id, String name, int age, String grade) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.grade = grade;
        this.subjects = new HashMap<>();
        this.attendanceRecord = new AttendanceRecord();
    }
    
    // Add a subject to the student's record
    public void addSubject(Subject subject) {
        subjects.put(subject.getName(), subject);
    }
    
    // Calculate the average mark across all subjects
    public double calculateOverallAverage() {
        if (subjects.isEmpty()) {
            return 0.0;
        }
        
        double totalMarks = 0.0;
        for (Subject subject : subjects.values()) {
            totalMarks += subject.getMark();
        }
        
        return totalMarks / subjects.size();
    }
    
    // Calculate the grade based on the mark (A, B, C, D, F)
    public String calculateOverallGrade() {
        double average = calculateOverallAverage();
        
        if (average >= 90) return "A";
        if (average >= 80) return "B";
        if (average >= 70) return "C";
        if (average >= 60) return "D";
        return "F";
    }
    
    // Get all subjects as a list
    public List<Subject> getSubjectList() {
        return new ArrayList<>(subjects.values());
    }
    
    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public Map<String, Subject> getSubjects() {
        return subjects;
    }

    public void setSubjects(Map<String, Subject> subjects) {
        this.subjects = subjects;
    }
    
    public AttendanceRecord getAttendanceRecord() {
        return attendanceRecord;
    }

    public void setAttendanceRecord(AttendanceRecord attendanceRecord) {
        this.attendanceRecord = attendanceRecord;
    }
    
    @Override
    public String toString() {
        return name + " (ID: " + id + ")";
    }
}
