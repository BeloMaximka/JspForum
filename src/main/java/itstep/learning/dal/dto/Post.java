package itstep.learning.dal.dto;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.UUID;

public class Post {
    private UUID id;
    private UUID themeId;
    private UUID authorId;
    private String title;
    private String description;
    private Date createDate;
    private Date deleteDate;

    public Post(ResultSet rs) throws SQLException {
        id = UUID.fromString(rs.getString("Id"));
        themeId = UUID.fromString(rs.getString("ThemeID"));
        authorId = UUID.fromString(rs.getString("AuthorId"));
        title = rs.getString("Title");
        description = rs.getString("Description");
        createDate = rs.getDate("CreateDate");
        deleteDate = rs.getDate("DeleteDate");
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getThemeId() {
        return themeId;
    }

    public void setThemeId(UUID themeId) {
        this.themeId = themeId;
    }

    public UUID getAuthorId() {
        return authorId;
    }

    public void setAuthorId(UUID authorId) {
        this.authorId = authorId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getDeleteDate() {
        return deleteDate;
    }

    public void setDeleteDate(Date deleteDate) {
        this.deleteDate = deleteDate;
    }
}
