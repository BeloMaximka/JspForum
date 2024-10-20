package itstep.learning.models.auth;

import itstep.learning.annotations.Optional;
import org.apache.commons.fileupload.FileItem;

import java.util.Date;

public class RegisterRequest {
    private String username;
    private String email;
    private String password;
    private String confirmPassword;
    private Date birthdate;
    @Optional
    private FileItem avatar;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    public FileItem getAvatar() {
        return avatar;
    }

    public void setAvatar(FileItem avatar) {
        this.avatar = avatar;
    }
}
