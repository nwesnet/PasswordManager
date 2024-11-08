package pmproject.passwordmanager;

public class Login implements Comparable<Login>{
    private String username;
    private String password;
    public Login (String username, String password) {
        this.username = username;
        this.password = password;
    }
    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }
    @Override
    public int compareTo(Login otherLogin) {
        // Compare username and password
        if (this.username.equals(otherLogin.getUsername()) && this.password.equals(otherLogin.getPassword())) {
            return 0;
        }
        else {
            return -1;
        }
    }
}
