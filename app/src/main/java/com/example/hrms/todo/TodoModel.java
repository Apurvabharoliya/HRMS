package com.example.hrms.todo;

public class TodoModel {

    private String title;
    private String status; // PENDING / DONE

    public TodoModel() {}

    public TodoModel(String title, String status) {
        this.title = title;
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public String getStatus() {
        return status;
    }
}
