package by.ziziko.fitboard.Entities;

import java.io.Serializable;

public class User implements Serializable {

    private String Id;
    private String email;
    private String login;
    private String password;
    private boolean isAdmin;
    private boolean isInBlacklist;

    public User()
    {

    }

    public User(String id, String email, String login, String password, boolean isAdmin, boolean isInBlacklist) {
        Id = id;
        this.email = email;
        this.login = login;
        this.password = password;
        this.isAdmin = isAdmin;
        this.isInBlacklist = isInBlacklist;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public boolean isInBlacklist() {
        return isInBlacklist;
    }

    public void setInBlacklist(boolean inBlacklist) {
        isInBlacklist = inBlacklist;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
