package com.example.hrms;

public class LeaveModel {

    private String id;          // 🔹 REQUIRED
    private String userId;
    private String leaveType;
    private String fromDate;
    private String toDate;
    private String reason;
    private String status;

    public LeaveModel() {}

    // 🔹 ID
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    // 🔹 OTHER FIELDS
    public String getUserId() {
        return userId;
    }

    public String getLeaveType() {
        return leaveType;
    }

    public String getFromDate() {
        return fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public String getReason() {
        return reason;
    }

    public String getStatus() {
        return status;
    }
}
