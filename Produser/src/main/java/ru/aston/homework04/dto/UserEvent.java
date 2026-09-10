package ru.aston.homework04.dto;

public class UserEvent {
    private String action;
    private String email;

    public UserEvent() {}

    public UserEvent(String action, String email) {
        this.action = action;
        this.email = email;
    }

    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
