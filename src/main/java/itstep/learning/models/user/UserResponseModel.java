package itstep.learning.models.user;

import itstep.learning.dal.dto.User;

import java.util.Date;
import java.util.UUID;

public class UserResponseModel {
    private UUID id;
    private String userName;
    private String email;
    private String avatarUrl;
    private Date birthDate;

    public UserResponseModel(User user) {
        id = user.getUserId();
        userName = user.getUserName();
        email = user.getEmail();
        avatarUrl = user.getAvatarUrl();
        birthDate = user.getBirthdate();
    }
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }
}
