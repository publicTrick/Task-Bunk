package ru.danil.task.models;

public class Worker {
    private Long id;
    private String name;
    private String position;
    private String avatar;

    public Worker() {}

    public Long getId() {return id;}

    public void setId(Long id) {this.id = id;}

    public String getName() {return name;}

    public void setName(String name) {this.name = name;}

    public String getPosition() {return position;}

    public void setPosition(String position) {this.position = position;}

    public String getAvatar() {return avatar;}

    public void setAvatar(String avatar) {this.avatar = avatar;}
}
