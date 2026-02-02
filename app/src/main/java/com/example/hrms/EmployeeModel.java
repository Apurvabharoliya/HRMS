package com.example.hrms;

public class EmployeeModel {

    private String name;
    private String role;
    private String status;

    public EmployeeModel() {}

    public String getName() { return name; }
    public String getRole() { return role; }
    public String getStatus() { return status == null ? "Active" : status; }
}
