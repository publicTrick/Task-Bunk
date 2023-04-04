package ru.danil.task.models;

import java.time.LocalDateTime;

public class Task {
    private Long id;
    private String title;
    private String description;
    private LocalDateTime time;
    private String status;
    private Long performerId;

    public Task() {}

    public Long getId() {return id;}

    public void setId(Long id) {this.id = id;}

    public String getTitle() {return title;}

    public void setTitle(String title) {this.title = title;}

    public String getDescription() {return description;}

    public void setDescription(String description) {this.description = description;}

    public LocalDateTime getTime() {return time;}

    public void setTime(LocalDateTime time) {this.time = time;}

    public String getStatus() {return status;}

    public void setStatus(String status) {this.status = status;}

    public Long getPerformerId() {return performerId;}

    public void setPerformerId(Long performerId) {this.performerId = performerId;}
}
