package com.app.locker.model;

import com.app.locker.utils.classes.logic.Crypto;

public class Entry {

    private String service;
    private String username;
    private String password;
    private String email;
    private String created;
    private Crypto crypto;

    public Entry(String service, String username, String password, String email, String created) {
        this();
        this.service = service;
        this.username = username;
        setPassword(password);
        this.email = email;
        this.created = created;
    }

    public Entry(){
        crypto = new Crypto();
    }

    public void updateEntry(Entry entry){
        setUsername(entry.getUsername());
        setPassword(entry.getPassword());
        setEmail(entry.getEmail());
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return crypto.decrypt(password);
    }

    public void setPassword(String password) {
        this.password = crypto.encrypt(password);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    @Override
    public String toString() {
        return "Entry{" +
                "service='" + service + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", created='" + created + '\'' +
                '}';
    }
}