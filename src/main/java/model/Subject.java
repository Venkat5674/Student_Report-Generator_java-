package model;

import java.io.Serializable;

/**
 * Represents a subject with name and mark
 */
public class Subject implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private String name;
    private double mark;
    
    public Subject(String name, double mark) {
        this.name = name;
        this.mark = mark;
    }
    
    // Calculate the letter grade based on the mark
    public String getGrade() {
        if (mark >= 90) return "A";
        if (mark >= 80) return "B";
        if (mark >= 70) return "C";
        if (mark >= 60) return "D";
        return "F";
    }
    
    // Getters and Setters
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public double getMark() {
        return mark;
    }
    
    public void setMark(double mark) {
        this.mark = mark;
    }
    
    @Override
    public String toString() {
        return name + ": " + mark + " (" + getGrade() + ")";
    }
}
