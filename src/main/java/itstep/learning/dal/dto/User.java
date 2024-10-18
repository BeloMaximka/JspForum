package itstep.learning.dal.dto;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.UUID;

public class User {
    private UUID userId;
    private String userName;
    private String email;
    private String avatarUrl;
    private Date birthdate;
    private Date   deleteDt;
    private String passwordHash;

    public User(ResultSet rs) throws SQLException {
     setUserId(UUID.fromString(rs.getString("Id")));
     userName = rs.getString("UserName");
     email = rs.getString("Email");
     avatarUrl = rs.getString("AvatarUrl");
     birthdate = rs.getDate("Birthdate");
     deleteDt = rs.getDate("DeleteDate");
     passwordHash = rs.getString("Password");
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
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

    public Date getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    public Date getDeleteDt() {
        return deleteDt;
    }

    public void setDeleteDt(Date deleteDt) {
        this.deleteDt = deleteDt;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }
}
