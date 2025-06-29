package model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents the attendance record for a student
 */
public class AttendanceRecord implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private Map<Date, AttendanceStatus> attendanceMap;
    
    public AttendanceRecord() {
        this.attendanceMap = new HashMap<>();
    }
    
    // Record attendance for a specific date
    public void recordAttendance(Date date, AttendanceStatus status) {
        attendanceMap.put(date, status);
    }
    
    // Get the attendance status for a specific date
    public AttendanceStatus getStatusForDate(Date date) {
        return attendanceMap.getOrDefault(date, AttendanceStatus.UNKNOWN);
    }
    
    // Calculate the attendance percentage
    public double calculateAttendancePercentage() {
        if (attendanceMap.isEmpty()) {
            return 0.0;
        }
        
        int presentCount = 0;
        for (AttendanceStatus status : attendanceMap.values()) {
            if (status == AttendanceStatus.PRESENT) {
                presentCount++;
            }
        }
        
        return (double) presentCount / attendanceMap.size() * 100;
    }
    
    // Get all attendance records
    public Map<Date, AttendanceStatus> getAttendanceMap() {
        return attendanceMap;
    }
    
    // Count total days present
    public int countPresent() {
        int count = 0;
        for (AttendanceStatus status : attendanceMap.values()) {
            if (status == AttendanceStatus.PRESENT) {
                count++;
            }
        }
        return count;
    }
    
    // Count total days absent
    public int countAbsent() {
        int count = 0;
        for (AttendanceStatus status : attendanceMap.values()) {
            if (status == AttendanceStatus.ABSENT) {
                count++;
            }
        }
        return count;
    }
    
    // Count total days late
    public int countLate() {
        int count = 0;
        for (AttendanceStatus status : attendanceMap.values()) {
            if (status == AttendanceStatus.LATE) {
                count++;
            }
        }
        return count;
    }
    
    // Enum for attendance status
    public enum AttendanceStatus {
        PRESENT,
        ABSENT,
        LATE,
        EXCUSED,
        UNKNOWN
    }
}
