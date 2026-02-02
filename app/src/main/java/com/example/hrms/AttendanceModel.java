package com.example.hrms;

public class AttendanceModel {

    private String userId;
    private String date;
    private String punchIn;
    private String punchOut;

    public AttendanceModel() {}

    public String getUserId() { return userId; }
    public String getDate() { return date; }
    public String getPunchIn() { return punchIn; }
    public String getPunchOut() { return punchOut; }
}
