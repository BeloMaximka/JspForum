package itstep.learning.dal.dto;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.UUID;

public class Section {
    private UUID id;
    private String title;
    private Date deleteDate;

    public Section(ResultSet rs) throws SQLException {
        id = UUID.fromString(rs.getString("Id"));
        title = rs.getString("Title");
        deleteDate = rs.getDate("DeleteDate");
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDeleteDate() {
        return deleteDate;
    }

    public void setDeleteDate(Date deleteDate) {
        this.deleteDate = deleteDate;
    }
}
