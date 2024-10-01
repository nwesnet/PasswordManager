package pmproject.passwordmanager;

import java.time.LocalDate;

public class Account {
    private String resource;
    private String login;
    private String password;
    private LocalDate date;

    public Account (String resource, String login, String password, LocalDate date) {
        this.resource = resource;
        this.login = login;
        this.password = password;
        this.date = date;

    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getResource() {
        return resource;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public LocalDate getDate() {
        return date;
    }
}
