package com.example.AttendanceSystem.func;

public class AttendanceHistoryModel {
    private String attendancedate;
    private String attendanceid;
    private String attendancelocation;
    private String attendancestatus;
    private String attendancetime;
    private String notes;
    private String userid;

    public AttendanceHistoryModel(){

    }

    public AttendanceHistoryModel(String attendancedate, String attendanceid, String attendancelocation, String attendancestatus, String attendancetime, String notes, String userid) {
        this.attendancedate = attendancedate;
        this.attendanceid = attendanceid;
        this.attendancelocation = attendancelocation;
        this.attendancestatus = attendancestatus;
        this.attendancetime = attendancetime;
        this.notes = notes;
        this.userid = userid;
    }

    public String getAttendancedate() {
        return attendancedate;
    }

    public void setAttendancedate(String attendancedate) {
        this.attendancedate = attendancedate;
    }

    public String getAttendanceid() {
        return attendanceid;
    }

    public void setAttendanceid(String attendanceid) {
        this.attendanceid = attendanceid;
    }

    public String getAttendancelocation() {
        return attendancelocation;
    }

    public void setAttendancelocation(String attendancelocation) {
        this.attendancelocation = attendancelocation;
    }

    public String getAttendancestatus() {
        return attendancestatus;
    }

    public void setAttendancestatus(String attendancestatus) {
        this.attendancestatus = attendancestatus;
    }

    public String getAttendancetime() {
        return attendancetime;
    }

    public void setAttendancetime(String attendancetime) {
        this.attendancetime = attendancetime;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }
}
