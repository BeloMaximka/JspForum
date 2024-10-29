package itstep.learning.dal.dto;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class Theme {
    private UUID id;
    private UUID sectionId;
    private String title;
    private String description;
    private Date deleteDate;

    public Theme(ResultSet rs) throws SQLException {
        id = UUID.fromString(rs.getString("Id"));
        sectionId = UUID.fromString(rs.getString("SectionId"));
        title = rs.getString("Title");
        description = rs.getString("Description");
        deleteDate = rs.getDate("DeleteDate");
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getSectionId() {
        return sectionId;
    }

    public void setSectionId(UUID sectionId) {
        this.sectionId = sectionId;
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

    public Date getDeleteDate() {
        return deleteDate;
    }

    public void setDeleteDate(Date deleteDate) {
        this.deleteDate = deleteDate;
    }
}
