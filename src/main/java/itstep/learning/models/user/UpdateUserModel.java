package itstep.learning.models.user;

import itstep.learning.annotations.Optional;

import java.util.Date;

public class UpdateUserModel {
    @Optional
    private String avatarUrl;
    private Date birthDate;

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
